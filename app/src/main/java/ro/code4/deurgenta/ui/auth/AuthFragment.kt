package ro.code4.deurgenta.ui.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentAuthBinding
import ro.code4.deurgenta.ui.base.BaseFragment

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

    override val screenName: Int
        get() = R.string.analytics_title_login
    private val viewModel: AuthViewModel by activityViewModels()
    private val binding: FragmentAuthBinding by viewBinding(FragmentAuthBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener { viewModel.onLoginClicked() }
        binding.signupButton.setOnClickListener { viewModel.onSignUpClicked() }
    }
}
