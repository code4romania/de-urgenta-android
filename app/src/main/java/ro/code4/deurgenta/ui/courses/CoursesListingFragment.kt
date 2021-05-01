package ro.code4.deurgenta.ui.courses

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_courses_listing.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.parceler.Parcels
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.Status
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.ViewModelFragment
import java.io.IOException

class CoursesListingFragment : ViewModelFragment<CoursesViewModel>() {

    override val screenName: Int
        get() = R.string.analytics_title_courses_listing
    override val layout: Int
        get() = R.layout.fragment_courses_listing
    override val viewModel: CoursesViewModel by viewModel()
    private var loadingAnimator: ObjectAnimator? = null
    private val coursesAdapter: CoursesAdapter by lazy {
        CoursesAdapter(requireContext()) {
            findNavController().navigate(
                R.id.action_coursesListing_to_courseDetails,
                bundleOf(CourseDetailsFragment.KEY_COURSE to Parcels.wrap(it))
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getString(KEY_TYPE) ?: error("Must set a type to filter courses!")
        val location = arguments?.getString(KEY_CITY) ?: error("Must set a city to filter courses!")
        viewModel.filter(type, location)
        with(courses) {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            adapter = coursesAdapter
        }
        viewModel.filteredCourses.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.Loading -> showAsLoading(true)
                Status.Success -> {
                    it.data?.let { courses ->
                        showAsLoading(false)
                        if (courses.isEmpty()) {
                            showInfoDisplay(getString(R.string.courses_empty))
                        } else {
                            coursesAdapter.submitList(courses)
                        }
                    }
                }
                Status.Error -> {
                    showAsLoading(false)
                    // implement proper error handling after deciding how to handle errors in the app
                    val errorText = if (it.error is IOException) {
                        getString(R.string.courses_error_missing_connection)
                    } else {
                        "Terrible error: ${it.error}"
                    }
                    showInfoDisplay(errorText)
                }
            }
        }
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
        courses_info_label.visibility = View.GONE
        courses.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun showInfoDisplay(msg: String) {
        courses_info_label.visibility = View.VISIBLE
        courses_info_label.text = msg
        courses.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.courses_title)
    }

    companion object {
        const val KEY_TYPE = "key_type"
        const val KEY_CITY = "key_city"
    }
}