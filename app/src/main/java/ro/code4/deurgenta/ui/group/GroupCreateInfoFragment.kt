package ro.code4.deurgenta.ui.group

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentGroupCreateInfoBinding
import ro.code4.deurgenta.helper.updateActivityTitle
import ro.code4.deurgenta.ui.base.BaseFragment

class GroupCreateInfoFragment : BaseFragment(R.layout.fragment_group_create_info) {

    override val screenName: Int
        get() = R.string.analytics_title_group
    private val binding: FragmentGroupCreateInfoBinding by viewBinding(
        FragmentGroupCreateInfoBinding::bind
    )

    private val viewModel: CreateGroupViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.inputGroupName.addTextChangedListener {
            // Allow submission only if name is not blank
            binding.buttonCreateNewGroupContinue.isEnabled = !it.isNullOrBlank()
        }

        binding.buttonCreateNewGroupContinue.setOnClickListener {
            viewModel.createGroup(binding.inputGroupName.text.toString())
        }

        viewModel.createResult.observe(viewLifecycleOwner) {
            // TODO: process create states
            it.handle(
                { Toast.makeText(context, "Success", Toast.LENGTH_LONG).show() },
                { Toast.makeText(context, "Error", Toast.LENGTH_LONG).show() },
                { Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show() }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityTitle(R.string.group_create_info_title)
    }
}
