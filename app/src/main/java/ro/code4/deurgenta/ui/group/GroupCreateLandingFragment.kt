package ro.code4.deurgenta.ui.group

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentGroupCreateLandingBinding
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class GroupCreateLandingFragment : BaseFragment(R.layout.fragment_group_create_landing) {

    override val screenName: Int
        get() = R.string.analytics_title_group
    private val binding: FragmentGroupCreateLandingBinding by viewBinding(FragmentGroupCreateLandingBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRejectNewGroup.paintFlags = binding.buttonRejectNewGroup.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.buttonRejectNewGroup.setOnClickListener { findNavController().navigateUp() }

        binding.buttonCreateNewGroup.setOnClickListener {
            findNavController().navigate(R.id.action_groupLanding_to_groupCreateInfo)
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.group_create_title)
    }
}
