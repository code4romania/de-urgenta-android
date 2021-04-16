package ro.code4.deurgenta.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.here.sdk.core.*
import com.here.sdk.mapview.*
import com.here.sdk.search.*
import ro.code4.deurgenta.R

class MapViewUtils(
    private val mapView: MapView,
    private val activity: Activity,
    private val callback: MapViewCallback
) {

    private val locationRequest: LocationRequest = LocationRequest.create()
    private val mapMarkerList = mutableListOf<MapMarker>()
    private val searchEngine: SearchEngine = SearchEngine()
    private lateinit var lastGeoCoordinates: GeoCoordinates
    private var areLocationServicesInitialized = false

    /**
     * Type of location services the client is interested in using.
     * Used for checking settings to determine if the device has optimal location settings.
     */
    private var locationSettingsRequest: LocationSettingsRequest

    /**
     * Provides access to the Location Settings API.
     */
    private var settingsClient: SettingsClient

    private var fusedLocationClient: FusedLocationProviderClient

    init {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
        locationRequest.interval = 10 * 1000; // 10 seconds
        locationRequest.fastestInterval = 5 * 1000; // 5 seconds
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        locationSettingsRequest = builder.build()

        settingsClient = LocationServices.getSettingsClient(activity)
    }

    fun loadMapScene(location: Location, showLocation: Boolean) {
        mapView.mapScene
            .loadScene(MapScheme.NORMAL_DAY) { errorCode ->
                if (errorCode == null) {

                    val distanceInMeters = DEFAULT_DISTANCE_IN_METERS.toDouble()
                    val geoCoordinates = GeoCoordinates(location.latitude, location.longitude)

                    mapView.camera.lookAt(geoCoordinates, distanceInMeters)
                    lastGeoCoordinates = geoCoordinates
                    areLocationServicesInitialized = true
                    if (showLocation) {
                        createAndAddMarker(geoCoordinates)
                    }
                } else {
                    Log.e(TAG, "error loading results:$errorCode")
                    if (errorCode != MapError.OPERATION_IN_PROGRESS) {
                        callback.onError(MapDataError("cannot load map. Error:$errorCode", MapErrorType.NOT_INITIALIZED))
                    }
                }
            }
    }

    private fun createAndAddMarker(geoCoordinates: GeoCoordinates) {
        clearMap()
        val mapMarker = createPoiMapMarker(geoCoordinates)
        mapView.mapScene.addMapMarker(mapMarker)
        mapMarkerList.add(mapMarker)
        callback.onSuccess()
    }

    private fun getMapViewGeoBox(): GeoBox {
        val mapViewWidthInPixels = mapView.width
        val mapViewHeightInPixels = mapView.height
        val bottomLeftPoint2D = Point2D(0.0, mapViewHeightInPixels.toDouble())
        val topRightPoint2D = Point2D(mapViewWidthInPixels.toDouble(), 0.0)
        val southWestCorner = mapView.viewToGeoCoordinates(bottomLeftPoint2D)
        val northEastCorner = mapView.viewToGeoCoordinates(topRightPoint2D)
        if (southWestCorner == null || northEastCorner == null) {
            throw RuntimeException("GeoBox creation failed, corners are null.")
        }

        // Note: This algorithm assumes an unrotated map view.
        return GeoBox(southWestCorner, northEastCorner)
    }

    fun searchOnMap(
        queryString: String,
        searchType: SearchType = SearchType.STANDARD,
        maxSearchResults: Int = DEFAULT_MAX_ITEMS
    ) {
        clearMap()
        if (queryString.length < 2) {
            return
        }

        if (areLocationServicesInitialized) {
            val viewportGeoBox: GeoBox = getMapViewGeoBox()
            val query = TextQuery(queryString, viewportGeoBox)

            val searchOptions = SearchOptions(LanguageCode.RO_RO, maxSearchResults)

            if (searchType == SearchType.STANDARD) {
                searchEngine.search(query, searchOptions, querySearchCallback)
            } else {
                searchEngine.suggest(query, searchOptions, autosuggestCallback)
            }
        } else {
            if (searchType == SearchType.STANDARD) {
                callback.onError(MapDataError("Map is not initialized", MapErrorType.NOT_INITIALIZED));
            }
        }
    }

    private val querySearchCallback = SearchCallback { searchError, list ->
        if (searchError != null) {
            callback.onError(MapDataError(searchError.toString(), MapErrorType.NO_RESULTS_FOUND))
            return@SearchCallback
        }

        // Add new marker for each search result on map.
        for (searchResult in list!!) {
            createAndAddMarker(searchResult.geoCoordinates!!)
        }
    }

    private var autosuggestCallback = SuggestCallback { searchError, list ->
        if (searchError != null) {
            callback.onSuggestionError(searchError.toString())
            return@SuggestCallback
        }

        // If error is null, list is guaranteed to be not empty.
        for (autosuggestResult in list!!) {
            val place = autosuggestResult.place
            if (place != null) {
                callback.onSuccessSuggest(place.address.addressText)
            }
        }
    }

    private fun clearMap() {
        for (mapMarker in mapMarkerList) {
            mapView.mapScene.removeMapMarker(mapMarker)
        }
        mapMarkerList.clear()
    }

    private fun createPoiMapMarker(geoCoordinates: GeoCoordinates): MapMarker {
        val mapImage: MapImage =
            MapImageFactory.fromResource(activity.resources, R.drawable.poi)
        return MapMarker(geoCoordinates, mapImage)
    }

    fun onResume() {
        mapView.onResume()
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                Log.d(TAG, "success in checking location settings.")
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "fail to get a response.$e")

                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        callback.onError(MapDataError("needs more permissions", MapErrorType.PERMISSION_ERROR, e))
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade location settings.")

                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        callback.onError(MapDataError(errorMessage, MapErrorType.PERMISSION_SETTING_CHANGE_UNAVAILABLE))
                        areLocationServicesInitialized = false
                    }
                }
            }
    }

    fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        mapView.onPause()
    }

    @SuppressLint("MissingPermission")
    fun loadLastKnownLocation(showLocation: Boolean) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    loadMapScene(location, showLocation)
                }

                if (location == null) {
                    startLocationUpdates()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "failed to localize.$it")
            }
    }

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "location update:" + locationResult.lastLocation)
            super.onLocationResult(locationResult)
            loadMapScene(location = locationResult.lastLocation, false)
        }
    }


    interface MapViewCallback {
        /**
         * Callback when an error occurs loading results or the map.
         */
        fun onError(error: MapDataError)

        /**
         * Callback after a marker has been added to the map.
         */
        fun onSuccess()

        /**
         * Callback for when suggest api is called.
         */
        fun onSuccessSuggest(suggest: String)

        /**
         * Callback for when suggestion api does not return any results.
         */
        fun onSuggestionError(toString: String)
    }

    companion object {
        private const val TAG = "MapViewUtils"
        private const val DEFAULT_DISTANCE_IN_METERS = 100 * 10
        private const val DEFAULT_MAX_ITEMS = 30
    }

    enum class SearchType {
        STANDARD,
        AUTOSUGGEST
    }

    enum class MapErrorType {
        PERMISSION_ERROR,
        NO_RESULTS_FOUND,
        PERMISSION_SETTING_CHANGE_UNAVAILABLE,
        NOT_INITIALIZED,
        UNKNOWN
    }

    data class MapDataError(val errorMessage: String, val errorType: MapErrorType = MapErrorType.UNKNOWN, val exception: Throwable? = null)
}