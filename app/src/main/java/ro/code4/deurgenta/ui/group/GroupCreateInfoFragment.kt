package ro.code4.deurgenta.ui.group

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentGroupCreateInfoBinding
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class GroupCreateInfoFragment : BaseFragment(R.layout.fragment_group_create_info) {

    override val screenName: Int
        get() = R.string.analytics_title_group
    private val binding: FragmentGroupCreateInfoBinding by viewBinding(FragmentGroupCreateInfoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputGroupName.addTextChangedListener {
            // Allow submission only if name is not blank
            binding.buttonCreateNewGroupContinue.isEnabled = !it.isNullOrEmpty()
        }

        binding.buttonCreateNewGroupContinue.setOnClickListener {
            val name = binding.inputGroupName.text.toString()
            // TODO: create new group with the given name
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.group_create_info_title)
    }
}
