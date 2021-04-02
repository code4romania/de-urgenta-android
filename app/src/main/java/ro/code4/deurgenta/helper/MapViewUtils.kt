package ro.code4.deurgenta.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.here.sdk.core.*
import com.here.sdk.core.threading.TaskHandle
import com.here.sdk.mapview.*
import com.here.sdk.search.*
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.MapAddress

class MapViewUtils(
    private val mapView: MapView,
    private val activity: Activity,
    private val callback: MapViewCallback
) {

    private val locationRequest: LocationRequest = LocationRequest.create()

    /**
     * Map that stores a map of map address and map markers.
     */
    private val mapMarkerList = mutableMapOf<MapAddress, MapMarker>()

    /**
     * Here sdk search engine.
     */
    private val searchEngine: SearchEngine = SearchEngine()

    /**
     * Variable to check if the location searvices are enabled or not.
     */
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

    /**
     * Geocoordinates on the map.
     */
    private lateinit var geoCoordinates: GeoCoordinates

    /**
     * Location client used to localize the user.
     */
    private var fusedLocationClient: FusedLocationProviderClient

    /**
     * Last query string.
     */
    private var queryString = ""

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

    fun loadMapSceneForLocation(location: Location) {
        geoCoordinates = GeoCoordinates(location.latitude, location.longitude)
        loadMapSceneForGeoCoordinates(geoCoordinates)
    }

    private fun loadMapSceneForGeoCoordinates(geoCoordinates: GeoCoordinates?) {
        if (geoCoordinates == null) {
            callback.onError(MapDataError(R.string.unknown_error, MapErrorType.UNKNOWN))
            return
        }

        mapView.mapScene
            .loadScene(MapScheme.NORMAL_DAY) { errorCode ->
                if (errorCode == null) {

                    val distanceInMeters = DEFAULT_DISTANCE_IN_METERS.toDouble()
                    mapView.camera.lookAt(geoCoordinates, distanceInMeters)
                    callback.onLoaded()
                } else {
                    Log.e(TAG, "error loading results:$errorCode")
                    if (errorCode != MapError.OPERATION_IN_PROGRESS) {
                        callback.onError(
                            MapDataError(
                                R.string.operation_in_progress,
                                MapErrorType.NOT_INITIALIZED
                            )
                        )
                    }
                }
            }
    }


    private fun createAndAddMarker(searchResult: Place) {
        clearMap()

        if (searchResult.geoCoordinates == null) {
            Log.d(TAG, "no coordinates to add")
            return
        }

        val mapMarker = createPoiMapMarker(searchResult.geoCoordinates!!)
        mapView.mapScene.addMapMarker(mapMarker)

        val mapAddress = MapAddress(
            latitude = searchResult.geoCoordinates!!.latitude,
            longitude = searchResult.geoCoordinates!!.longitude,
            fullAddress = searchResult.address.addressText
        )
        mapAddress.streetAddress = getStreetAddress(searchResult.address)

        mapMarkerList[mapAddress] = mapMarker
    }

    private fun getStreetAddress(address: Address): String {
        return formatAddress(address)
    }

    private fun getMapViewGeoCircle(): GeoCircle {
        return GeoCircle(geoCoordinates, 50000.0)
    }

    fun searchOnMap(
        query: String,
        searchType: SearchType = SearchType.STANDARD,
        maxSearchResults: Int = DEFAULT_MAX_ITEMS
    ) {
        clearMap()
        if (query.length < 2) {
            queryString = ""
            return
        }

        if (areLocationServicesInitialized) {
            queryString = query

            val circleBox = getMapViewGeoCircle()
            val textQuery = TextQuery(query, circleBox)

            if (searchType == SearchType.STANDARD) {
                val searchOptions = SearchOptions(LanguageCode.RO_RO, 1)
                searchEngine.search(textQuery, searchOptions, querySearchCallback)
            } else {
                val searchOptions = SearchOptions(LanguageCode.RO_RO, maxSearchResults)
                searchEngine.suggest(textQuery, searchOptions, autosuggestCallback)
            }
        } else {
            if (searchType == SearchType.STANDARD) {
                callback.onError(
                    MapDataError(
                        R.string.map_not_initialized,
                        MapErrorType.NOT_INITIALIZED
                    )
                )
            }
        }
    }

    private val querySearchCallback = SearchCallback { searchError, list ->
        if (searchError != null) {
            Log.d(TAG, searchError.toString())
            callback.onError(MapDataError(R.string.no_results_found, MapErrorType.NO_RESULTS_FOUND))
            return@SearchCallback
        }

        // Add new marker for each search result on map.
        for (searchResult in list!!) {
            createAndAddMarker(searchResult)
        }

        if (list.size == 1) {
            loadMapSceneForGeoCoordinates(list[0].geoCoordinates)
            callback.onSearchSuccess()
        } else if (list.size > 1) {
            callback.onError(
                MapDataError(
                    R.string.more_results_found,
                    MapErrorType.MORE_RESULTS_FOUND
                )
            );
        }
    }

    private var autosuggestCallback = SuggestCallback { searchError, list ->
        if (searchError != null) {
            callback.onSuggestError(searchError.toString())
            return@SuggestCallback
        }
        callback.onSuggestSuccess(queryString, list!!)
    }

    private fun clearMap() {
        for (address in mapMarkerList.keys) {
            mapMarkerList[address]?.let {
                mapView.mapScene.removeMapMarker(it)
            }
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

    fun onPause() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        mapView.onPause()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                Log.d(TAG, "success in checking location settings.")
                callback.onLoading()
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
                        callback.onError(
                            MapDataError(
                                R.string.resolution_required,
                                MapErrorType.PERMISSION_ERROR,
                                e
                            )
                        )
                        Log.i(
                            TAG,
                            "Location settings are not satisfied. Attempting to upgrade location settings."
                        )
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        val errorMessage =
                            "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                        Log.e(TAG, errorMessage)
                        callback.onError(
                            MapDataError(
                                R.string.settings_change_unavailable,
                                MapErrorType.PERMISSION_SETTING_CHANGE_UNAVAILABLE
                            )
                        )
                        areLocationServicesInitialized = false
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    fun loadLastKnownLocation(searchAddress: Boolean = false) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    loadMapSceneForLocation(location)
                    if (searchAddress) {
                        searchAddressForGeoCoordinates()
                    }
                }

                if (location == null) {
                    startLocationUpdates()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "failed to localize.$it")
            }
    }

    private fun searchAddressForGeoCoordinates(): TaskHandle {
        val searchOptions = SearchOptions(LanguageCode.RO_RO, 1)
        return searchEngine.search(geoCoordinates, searchOptions, querySearchCallback)
    }

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "location update:" + locationResult.lastLocation)
            super.onLocationResult(locationResult)
            areLocationServicesInitialized = true
            loadMapSceneForLocation(location = locationResult.lastLocation)
        }
    }

    fun getCurrentAddress(): MapAddress? {
        if (mapMarkerList.isEmpty()) {
            return null
        }

        return mapMarkerList.keys.toTypedArray()[0]
    }

    interface MapViewCallback {

        /**
         * Callback for when the map is loading.
         */
        fun onLoading()

        /**
         * Callback for when then map has finished loading.
         */
        fun onLoaded()

        /**
         * Callback when an error occurs loading results or the map.
         */
        fun onError(error: MapDataError)

        /**
         * Callback after a marker has been added to the map.
         */
        fun onSearchSuccess()

        /**
         * Callback for when suggest api is called.
         */
        fun onSuggestSuccess(query: String, suggestions: List<Suggestion>)

        /**
         * Callback for when suggestion api does not return any results.
         */
        fun onSuggestError(errorMessage: String)
    }

    companion object {
        private const val TAG = "MapViewUtils"
        private const val DEFAULT_DISTANCE_IN_METERS = 100 * 100
        private const val DEFAULT_MAX_ITEMS = 50
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
        MORE_RESULTS_FOUND,
        UNKNOWN,
    }

    data class MapDataError(
        val errorStringId: Int = 0,
        val errorType: MapErrorType = MapErrorType.UNKNOWN,
        val exception: Throwable? = null
    )
}