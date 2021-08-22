package ro.code4.deurgenta.ui.courses

import android.os.Bundle
import android.view.View
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Course
import ro.code4.deurgenta.databinding.FragmentCoursesDetailsBinding
import ro.code4.deurgenta.helper.dateFormatter
import ro.code4.deurgenta.helper.takeUserTo
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class CourseDetailsFragment : BaseFragment(R.layout.fragment_courses_details) {
    override val screenName: Int
        get() = R.string.analytics_title_courses_details

    private val binding: FragmentCoursesDetailsBinding by viewBinding(FragmentCoursesDetailsBinding::bind)
    private lateinit var course: Course

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = arguments?.getParcelable(KEY_COURSE) ?: error("Must provide a course reference to see its details!")
        binding.provider.text = course.provider
        binding.date.text = dateFormatter.format(course.date)
        binding.location.text = course.location
        binding.info.text = course.description
        binding.btnCourseRegister.setOnClickListener { requireActivity().takeUserTo(course.url) }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(course.name)
    }

    companion object {
        const val KEY_COURSE = "key_course"
    }
}