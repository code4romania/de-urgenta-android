package ro.code4.deurgenta.ui.group

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.fragment_group_create_info.*
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class GroupCreateInfoFragment : BaseAnalyticsFragment(R.layout.fragment_group_create_info) {
    override val screenName: Int = R.string.analytics_title_group

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        input_group_name.addTextChangedListener {
            // Allow submission only if name is not blank
            button_create_new_group_continue.isEnabled = !it.isNullOrEmpty()
        }

        button_create_new_group_continue.setOnClickListener {
            val name = input_group_name.text.toString()

            // TODO: create new group with the given name
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.group_create_info_title)
    }
}
