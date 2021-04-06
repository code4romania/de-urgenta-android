package ro.code4.deurgenta.ui.login

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.replaceFragment
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.register.RegisterCompletedFragment
import ro.code4.deurgenta.ui.register.RegisterFragment
import ro.code4.deurgenta.ui.register.RegisterViewModel

class LoginActivity : BaseAnalyticsActivity<LoginViewModel>() {

    override val layout: Int
        get() = R.layout.activity_login
    override val screenName: Int
        get() = R.string.analytics_title_login

    private val registerViewModel: RegisterViewModel by viewModel()
    override val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clickListenersSetup()
        loginUserObservable()
        registerObservables()
    }

    private fun clickListenersSetup() {
        loginButton.setOnClickListener {
            showLoginFormFragment()
        }
        signupButton.setOnClickListener {
            showRegistrationFormFragment()
        }
    }

    private fun showLoginFormFragment() {
        supportFragmentManager.replaceFragment(
            R.id.login_container,
            LoginFormFragment()
        )
    }

    private fun showRegistrationFormFragment() {
        supportFragmentManager.commit {
            replace<RegisterFragment>(R.id.login_container)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun showRegistrationCompletedFragment() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.commit {
            replace<RegisterCompletedFragment>(R.id.login_container)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun loginUserObservable() {
        viewModel.loggedIn().observe(this, {
            it.handle(
                onSuccess = { activity ->
                    activity?.let(::startActivityWithoutTrace)
                },
                onFailure = { error ->
                    showDefaultErrorSnackBar(loginButton)

                    loginButton.isEnabled = true
                }
            )
        })
    }

    private fun registerObservables() {
        registerViewModel.registered().observe(this, {
            it.handle(
                onSuccess = {
                    showRegistrationCompletedFragment()
                },
                onFailure = { error ->
                    showDefaultErrorSnackBar(submitButton)
                }
            )
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed();
        }
    }
}
