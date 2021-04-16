package ro.code4.deurgenta.ui.address

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.here.sdk.mapview.*
import kotlinx.android.synthetic.main.onboarding_configure_addresses.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.OnboardingConfigureAddressesBinding
import ro.code4.deurgenta.helper.*
import ro.code4.deurgenta.interfaces.LocateMeCallback
import ro.code4.deurgenta.interfaces.SaveProgressCallback
import ro.code4.deurgenta.ui.base.ViewModelFragment
import java.util.*

@SuppressLint("LongLogTag")
class ConfigureAddressFragment : ViewModelFragment<ConfigureAddressViewModel>(),
    PermissionManager.PermissionListener,
    PermissionUiCallback {

    override val layout: Int
        get() = R.layout.onboarding_configure_addresses

    override val screenName: Int
        get() = R.string.configure_addresses

    override val viewModel: ConfigureAddressViewModel by viewModel()
    private lateinit var mapView: MapView
    private lateinit var permissionsUtils: PermissionManager

    private lateinit var mapViewUtils: MapViewUtils

    lateinit var viewBinding: OnboardingConfigureAddressesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.lifecycleOwner = viewLifecycleOwner
        viewBinding.appbar.toolbar.setOnClickListener {
            findNavController().navigate(R.id.back_to_configure_profile)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView = viewBinding.mapView
        mapView.onCreate(savedInstanceState)
        permissionsUtils = PermissionManager(requireActivity(), this)
        mapViewUtils = MapViewUtils(mapView, requireActivity(), mapViewCallback)

        initMapButtonCallbacks()
    }

    private fun initMapButtonCallbacks() {
        viewBinding.appbar.query_search.apply {
            setOnQueryTextListener(onQueryTextListener(this@apply))
        }

        viewBinding.saveCallback = object : SaveProgressCallback {
            override fun save() {
                Log.d(TAG, "save address")
            }
        }

        viewBinding.locateMeCallback = object : LocateMeCallback {
            override fun locateMe() {
                mapViewUtils.loadLastKnownLocation(true)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PermissionManager.PERMISSION_REQUEST && grantResults.isNotEmpty()) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED } && grantResults.size == permissions.size) {
                onPermissionsGranted()
            } else {
                checkDeniedPermissions(permissions, grantResults)
            }
        }
    }

    private fun checkDeniedPermissions(permissions: Array<out String>, grantResults: IntArray) {
        val permanentlyDenied = permissions.filterIndexed { index, s ->
            grantResults[index] == PackageManager.PERMISSION_DENIED
                    && !permissionsUtils.checkShouldShowRequestPermissionsRationale(s)
        }


        if (permanentlyDenied.isNotEmpty()) {
            showPermissionRationale(requireActivity(), true, this)
        } else {
            val denied = permissions.filterIndexed { index, s ->
                grantResults[index] == PackageManager.PERMISSION_DENIED
                        && permissionsUtils.checkShouldShowRequestPermissionsRationale(s)
            }
            if (denied.isNotEmpty()) {
                showPermissionRationale(requireActivity(), true, this)
            }
        }
    }

    private val mapViewCallback = object : MapViewUtils.MapViewCallback {
        override fun onError(error: MapViewUtils.MapDataError) {
            if (error.errorType == MapViewUtils.MapErrorType.PERMISSION_ERROR) {
                try {
                    // shows the permission request settings dialog.
                    val rae = error.exception as ResolvableApiException
                    startIntentSenderForResult(rae.resolution.intentSender, PermissionManager.PERMISSION_CHECK_SETTINGS)
                } catch (sie: IntentSender.SendIntentException) {
                    Log.i(TAG, "PendingIntent unable to execute request.")
                }
            } else {
                Toast.makeText(requireContext(), error.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        override fun onSuggestionError(error: String) {
            Log.d(TAG, "error loading autosuggestion results:$error")
        }

        override fun onSuccess() {
            updateSaveButtonVisibility(View.VISIBLE)
        }

        override fun onSuccessSuggest(suggest: String) {
            Log.d(TAG, "autosuggestion text.$suggest")
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissionsUtils.hasPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            mapViewUtils.onResume()
        } else {
            permissionsUtils.checkPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
        }
    }

    override fun onStop() {
        super.onStop()
        mapViewUtils.onPause()
    }

    private fun onQueryTextListener(view: View): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideSoftInput(view)
                mapViewUtils.searchOnMap(query, MapViewUtils.SearchType.STANDARD)
                return true
            }

            override fun onQueryTextChange(queryString: String): Boolean {
                // Hack
                if (queryString.isNullOrEmpty()) {
                    updateSaveButtonVisibility(View.GONE)
                }
                mapViewUtils.searchOnMap(queryString, MapViewUtils.SearchType.AUTOSUGGEST)
                return true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PermissionManager.PERMISSION_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(TAG, "User agreed to make required location settings changes.")
                    mapViewUtils.startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    Log.i(TAG, "User chose not to make required location settings changes.")
                }
            }
        }
    }

    override fun onDialogCallback() {
        permissionsUtils.checkPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    override fun onPermissionsGranted() {
        Log.d(TAG, "permission granted.")
        mapViewUtils.startLocationUpdates()
    }

    override fun onPermissionDenied() {
        Log.e(TAG, "Permissions denied by user, return back to the configure profile.")
        // todo if permission was denied before notify the user, maybe one changes its mind.
        findNavController().navigate(R.id.configure_account)
    }

    private fun updateSaveButtonVisibility(flag: Int) {
        viewBinding.buttonSaveAddress.visibility = flag
    }

    companion object {
        const val TAG: String = "ConfigureAccountFragment"
    }
}