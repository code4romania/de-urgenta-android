package ro.code4.deurgenta.ui.auth.register

import android.os.Bundle
import android.view.View
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentCompletedRegisterBinding
import ro.code4.deurgenta.ui.base.BaseFragment

class RegisterCompletedFragment : BaseFragment(R.layout.fragment_completed_register) {

    private val binding: FragmentCompletedRegisterBinding by viewBinding(FragmentCompletedRegisterBinding::bind)

    override val screenName: Int
        get() = R.string.analytics_title_register

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includedToolbar.toolbar.title = resources.getString(R.string.register_completed_title)
        binding.toLoginScreen.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}