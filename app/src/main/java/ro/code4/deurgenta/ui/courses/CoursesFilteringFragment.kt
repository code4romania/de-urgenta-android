package ro.code4.deurgenta.ui.courses

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentCoursesFilterBinding
import ro.code4.deurgenta.helper.Status
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class CoursesFilteringFragment : BaseFragment(R.layout.fragment_courses_filter) {

    override val screenName: Int
        get() = R.string.analytics_title_courses_filtering
    private val viewModel: CoursesFilterViewModel by viewModel()
    private val binding: FragmentCoursesFilterBinding by viewBinding(FragmentCoursesFilterBinding::bind)
    private var loadingAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingIndicator.setToRotateIndefinitely().start()
        viewModel.filterOptions.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.Loading -> showAsLoading(true)
                Status.Success -> {
                    showAsLoading(false)
                    setupAdapters(resource.data!!)
                }
                Status.Error -> {
                    showAsLoading(false)
                    binding.selectors.visibility = View.GONE
                    binding.btnToCoursesListing.isEnabled = false
                    // TODO implement this after we decide how we manage errors
                    Toast.makeText(requireContext(), "Unable to load options for filtering!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnToCoursesListing.setOnClickListener {
            val selectedType = binding.courseTypeSelector.selectedItem
            val selectedLocation = binding.courseLocationSelector.selectedItem
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
        binding.courseTypeSelector.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, filterOptions.first
        )
        binding.courseLocationSelector.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1, filterOptions.second
        )
    }

    private fun showAsLoading(isLoading: Boolean) {
        binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            loadingAnimator?.cancel()
            loadingAnimator = binding.loadingIndicator.setToRotateIndefinitely()
            loadingAnimator?.start()
        } else {
            loadingAnimator?.end()
            loadingAnimator = null
        }
        binding.selectors.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.btnToCoursesListing.isEnabled = !isLoading
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.courses_title)
    }
}