package ro.code4.deurgenta.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class CoursesInfoFragment : BaseAnalyticsFragment() {
    override val screenName: Int
        get() = R.string.analytics_title_courses_info

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses_info, container, false).also {
            it.findViewById<Button>(R.id.btn_to_course_filtering).setOnClickListener {
                findNavController().navigate(R.id.action_coursesInfo_to_coursesFiltering)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.courses_title)
    }
}