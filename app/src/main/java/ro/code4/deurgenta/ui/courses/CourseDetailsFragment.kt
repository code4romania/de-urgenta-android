package ro.code4.deurgenta.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_courses_details.*
import org.parceler.Parcels
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Course
import ro.code4.deurgenta.helper.dateFormatter
import ro.code4.deurgenta.helper.takeUserTo
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class CourseDetailsFragment : BaseAnalyticsFragment() {
    override val screenName: Int
        get() = R.string.analytics_title_courses_details

    private lateinit var course: Course

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        course = Parcels.unwrap(arguments?.getParcelable(KEY_COURSE))
        provider.text = course.provider
        date.text = dateFormatter.format(course.date)
        location.text = course.location
        info.text = course.description
        btn_course_register.setOnClickListener {
            requireActivity().takeUserTo(course.url)
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(course.name)
    }

    companion object {
        const val KEY_COURSE = "key_course"
    }
}