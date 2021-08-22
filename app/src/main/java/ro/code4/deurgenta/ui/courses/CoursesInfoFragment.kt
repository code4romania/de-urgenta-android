package ro.code4.deurgenta.ui.courses

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.navigation.fragment.findNavController
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class CoursesInfoFragment : BaseFragment(R.layout.fragment_courses_info) {

    override val screenName: Int
        get() = R.string.analytics_title_courses_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btn_to_course_filtering).setOnClickListener {
            findNavController().navigate(R.id.action_coursesInfo_to_coursesFiltering)
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.courses_title)
    }
}