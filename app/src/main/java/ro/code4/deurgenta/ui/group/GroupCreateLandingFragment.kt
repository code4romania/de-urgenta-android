package ro.code4.deurgenta.ui.group

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_group_create_landing.*
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class GroupCreateLandingFragment : BaseAnalyticsFragment(R.layout.fragment_group_create_landing) {
    override val screenName: Int = R.string.analytics_title_group

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_reject_new_group.paintFlags = button_reject_new_group.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        button_reject_new_group.setOnClickListener {
            findNavController().navigateUp()
        }

        button_create_new_group.setOnClickListener {
            findNavController().navigate(R.id.action_groupCreateLandingFragment_to_groupCreateInfoFragment)
        }
    }
}
