package ro.code4.deurgenta.ui.courses

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_courses_filter.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.Status
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.ViewModelFragment

class CoursesFilteringFragment : ViewModelFragment<CoursesFilterViewModel>() {

    override val screenName: Int
        get() = R.string.analytics_title_courses_filtering
    override val layout: Int
        get() = R.layout.fragment_courses_filter
    override val viewModel: CoursesFilterViewModel by viewModel()
    private var loadingAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingIndicator.setToRotateIndefinitely().start()
        viewModel.filterOptions.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.Loading -> showAsLoading(true)
                Status.Success -> {
                    showAsLoading(false)
                    setupAdapters(resource.data!!)
                }
                Status.Error -> {
                    showAsLoading(false)
                    selectors.visibility = View.GONE
                    btn_to_courses_listing.isEnabled = false
                    // TODO implement this after we decide how we manage errors
                    Toast.makeText(requireContext(), "Unable to load options for filtering!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_to_courses_listing.setOnClickListener {
            val selectedType = course_type_selector.selectedItem
            val selectedLocation = course_location_selector.selectedItem
            if (selectedType == null || selectedLocation == null) {
                Toast.makeText(requireContext(), getString(R.string.courses_no_selection), Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(
                    R.id.action_coursesFilter_to_coursesListing, bundleOf(
                        CoursesListingFragment.KEY_TYPE to selectedType,
                        CoursesListingFragment.KEY_CITY to selectedLocation
                    )
                )
            }
        }
    }

    private fun setupAdapters(filterOptions: Pair<List<String>, List<String>>) {
        course_type_selector.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, filterOptions.first
        )
        course_location_selector.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, filterOptions.second
        )
    }

    private fun showAsLoading(isLoading: Boolean) {
        loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            loadingAnimator?.cancel()
            loadingAnimator = loadingIndicator.setToRotateIndefinitely()
            loadingAnimator?.start()
        } else {
            loadingAnimator?.end()
            loadingAnimator = null
        }
        selectors.visibility = if (isLoading) View.GONE else View.VISIBLE
        btn_to_courses_listing.isEnabled = !isLoading
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.courses_title)
    }
}