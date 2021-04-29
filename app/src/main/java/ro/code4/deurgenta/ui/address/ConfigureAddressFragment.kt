package ro.code4.deurgenta.ui.address

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.content.IntentSender
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.here.sdk.mapview.MapView
import com.here.sdk.search.Suggestion
import kotlinx.android.synthetic.main.layout_mapview.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.MapAddressType
import ro.code4.deurgenta.databinding.FragmentConfigureAddressBinding
import ro.code4.deurgenta.helper.*
import ro.code4.deurgenta.interfaces.ClickButtonCallback
import ro.code4.deurgenta.ui.base.PermissionsViewModelFragment

@SuppressLint("LongLogTag")
class ConfigureAddressFragment : PermissionsViewModelFragment<ConfigureAddressViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_configure_address

    override val screenName: Int
        get() = R.string.configure_addresses

    override val viewModel: ConfigureAddressViewModel by viewModel()
    private lateinit var mapView: MapView
    private var mapViewUtils: MapViewUtils? = null
    lateinit var viewBinding: FragmentConfigureAddressBinding
    var mapAddressType: MapAddressType = MapAddressType.HOME
    private var loadingAnimator: ObjectAnimator? = null
    private fun searchView() = viewBinding.appbarSearch.querySearch

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
        viewBinding.appbarSearch.toolbarSearch.setOnClickListener {
            findNavController().navigate(R.id.back_to_configure_profile)
        }

        viewModel.saveResult().observe(viewLifecycleOwner, { result ->
            result.handle(onSuccess = {
                val direction =
                    ConfigureAddressFragmentDirections.actionNavigateSaveAddress(mapAddress = it!!)
                findNavController().navigate(direction)
            })
        })

        arguments?.let { bundle ->
            bundle.getParcelable<MapAddressType>("mapAddressType")?.let { it ->
                mapAddressType = it
            }
        }

        arguments?.let { bundle ->
            bundle.getInt("titleResourceId").let { resId ->
                viewBinding.toolbarTitle = getString(resId)
            }
        }

        mapView = viewBinding.mapViewLayout.mapView
        mapView.onCreate(savedInstanceState)
        mapViewUtils = MapViewUtils(mapView, requireActivity(), mapViewCallback)

        initCallbacks()
        initSearchView()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback()
        )
    }

    override fun onResume() {
        super.onResume()
        mapViewUtils?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapViewUtils?.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PermissionManager.PERMISSION_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    logI("User agreed to make required location settings changes.", TAG)
                    mapViewUtils?.startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                    logI("User chose not to make required location settings changes.", TAG)
                }
            }
        }
    }

    private fun initCallbacks() {
        viewBinding.saveAddressCallback = object : ClickButtonCallback {
            override fun call() {
                mapViewUtils?.getCurrentAddress()
                    ?.let { mapAddress ->
                        mapAddress.type = mapAddressType
                        viewModel.saveAddress(mapAddress)
                    }
            }
        }

        viewBinding.locateMeCallback = object : ClickButtonCallback {
            override fun call() {
                searchView().clearFocus()
                setQuery("", false)
                mapViewUtils?.loadLastKnownLocation(true)
            }
        }
    }

    private fun initSearchView() {

        searchView().setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    view?.let { hideSoftInput(it) }
                    mapViewUtils?.searchOnMap(query, MapViewUtils.SearchType.STANDARD)
                    return true
                }

                override fun onQueryTextChange(queryString: String): Boolean {
                    // Hack
                    if (queryString.isNullOrEmpty()) {
                        updateSaveButtonVisibility(View.GONE)
                    }
                    mapViewUtils?.searchOnMap(queryString, MapViewUtils.SearchType.AUTOSUGGEST)
                    return true
                }
            }
        )

        val cursorAdapter = initSearchViewAdapter()

        searchView().suggestionsAdapter = cursorAdapter
        searchView().setOnSuggestionListener(onSuggestionListener())
    }

    private fun initSearchViewAdapter(): CursorAdapter {
        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)

        return SimpleCursorAdapter(
            context,
            R.layout.layout_search_item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )
    }

    private fun onSuggestionListener() =
        object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = searchView().suggestionsAdapter.cursor
                cursor.moveToPosition(position)
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                setQuery(selection, true)
                return true
            }
        }

    private val mapViewCallback = object : MapViewUtils.MapViewCallback {

        override fun onLoaded() {
            setAsLoading(false)
        }

        override fun onLoading() {
            setAsLoading(true)
        }

        override fun onError(error: MapViewUtils.MapDataError) {
            if (loadingAnimator?.isRunning == true) {
                setAsLoading(false)
            }
            if (error.errorType == MapViewUtils.MapErrorType.PERMISSION_ERROR) {
                try {
                    // shows the permission request settings dialog.
                    val rae = error.exception as ResolvableApiException
                    startIntentSenderForResult(
                        rae.resolution.intentSender,
                        PermissionManager.PERMISSION_CHECK_SETTINGS
                    )
                } catch (sie: IntentSender.SendIntentException) {
                    logI(TAG, "PendingIntent unable to execute request.")
                }
            } else {
                Toast.makeText(requireContext(), getString(error.errorStringId), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        override fun onSuggestError(errorMessage: String) {
            logD("error loading autosuggestion results:$errorMessage", TAG)
        }

        override fun onSearchSuccess() {
            updateSaveButtonVisibility(View.VISIBLE)
        }

        override fun onSuggestSuccess(query: String, suggestions: List<Suggestion>) {
            val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
            query.let {
                suggestions.forEachIndexed { index, suggestion ->
                    logD("autosuggestion text.$suggestion", TAG)
                    if (suggestion.place?.title?.contains(query, true) == true) {
                        cursor.addRow(arrayOf(index, suggestion.place?.title))
                    }
                }
            }

            searchView().suggestionsAdapter.changeCursor(cursor)
        }
    }

    override fun onPermissionsGranted() {
        logD("permission granted.", TAG)
        mapViewUtils?.startLocationUpdates()
    }

    override fun onPermissionDenied() {
        logE("Permissions denied by user, return back to the configure profile.", TAG)
        // todo if permission was denied before notify the user, maybe one changes its mind.
        findNavController().navigate(R.id.configure_account)
    }

    private fun updateSaveButtonVisibility(flag: Int) {
        viewBinding.mapViewLayout.saveAddress.visibility = flag
    }

    fun setQuery(query: String, submit: Boolean) {
        searchView().setQuery(query, submit)
    }

    private fun setAsLoading(isLoading: Boolean) {
        mapLoadingIndicator?.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            loadingAnimator?.cancel()
            loadingAnimator = mapLoadingIndicator.setToRotateIndefinitely()
            loadingAnimator?.start()
        }
    }

    override fun handleOnBackPressedInternal() {
        val directions = ConfigureAddressFragmentDirections.backToConfigureProfile()
        findNavController().navigate(directions)
    }

    companion object {
        const val TAG: String = "ConfigureAccountFragment"
    }
}