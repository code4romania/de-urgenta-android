package ro.code4.deurgenta.ui.auth.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentLoginBinding
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.auth.reset.ResetPasswordFragment
import ro.code4.deurgenta.ui.base.BaseFragment
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.onboarding.OnboardingActivity

class LoginFormFragment : BaseFragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    val viewModel: LoginFormViewModel by inject()

    override val screenName: Int
        get() = R.string.login_fragment_name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnForgotPassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_container, ResetPasswordFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        binding.loginBtn.setOnClickListener {
            binding.loginBtn.isEnabled = false
            val email = binding.username.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(email, password)
        }
        loginUserObservable()

        // TODO only for testing, to be removed for the final release
        if (BuildConfig.DEBUG) {
            binding.skipToMain.setOnClickListener {
                startActivityWithoutTrace(MainActivity::class.java)
            }
            binding.skipToOnboarding.setOnClickListener {
                startActivityWithoutTrace(OnboardingActivity::class.java)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.includedToolbar.toolbar.title = resources.getString(R.string.login_auth_title)
    }

    private fun loginUserObservable() {
        viewModel.loggedIn().observe(viewLifecycleOwner, Observer {
            it.handle(
                onSuccess = { activity ->
                    activity?.let(::startActivityWithoutTrace)
                },
                onFailure = { error ->
                    // TODO: display some friendlier errors
                    Snackbar.make(binding.loginBtn, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()
                    binding.loginBtn.isEnabled = true
                },
                onLoading = {
                    Snackbar.make(binding.loginBtn, "Loading", Snackbar.LENGTH_INDEFINITE).show()
                }
            )
        })
    }
}
