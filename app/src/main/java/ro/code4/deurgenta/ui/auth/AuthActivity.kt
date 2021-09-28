package ro.code4.deurgenta.ui.auth

import android.os.Bundle
import android.widget.FrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.replaceFragment
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.auth.login.LoginFormFragment
import ro.code4.deurgenta.ui.auth.register.RegisterCompletedFragment
import ro.code4.deurgenta.ui.auth.register.RegisterFragment
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity

class AuthActivity : BaseAnalyticsActivity<AuthViewModel>() {

    override val layout: Int
        get() = R.layout.activity_auth
    override val screenName: Int
        get() = R.string.analytics_title_login

    override val viewModel: AuthViewModel by viewModel()
    private lateinit var authContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authContainer = findViewById(R.id.auth_container)
        if (savedInstanceState == null) {
            showAuthFragment()
        }

        loginUserObservable()
        registerObservables()
    }

    private fun showAuthFragment() {
        supportFragmentManager.replaceFragment(
            R.id.auth_container,
            AuthFragment(),
            isPrimaryNavigationFragment = true,
            tag = null
        )
    }

    private fun showLoginFormFragment() {
        supportFragmentManager.replaceFragment(
            R.id.auth_container,
            LoginFormFragment(),
            tag = "loginFragment"
        )
    }

    private fun showRegistrationFormFragment() {
        supportFragmentManager.replaceFragment(
            R.id.auth_container,
            RegisterFragment(),
            tag = "registerFragment"
        )
    }

    private fun showRegistrationCompletedFragment() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.replaceFragment(
            R.id.auth_container,
            RegisterCompletedFragment(),
            tag = "registerFragmentCompleted"
        )
    }

    private fun loginUserObservable() {
        viewModel.loginNavigation().observe(this, { showLoginFormFragment() })
        viewModel.signupNavigation().observe(this, { showRegistrationFormFragment() })

        viewModel.loggedIn().observe(this, {
            it.handle(
                onSuccess = { activity ->
                    activity?.let(::startActivityWithoutTrace)
                },
                onFailure = { error ->
                    showDefaultErrorSnackBar(authContainer)
                }
            )
        })
    }

    private fun registerObservables() {
        viewModel.registered().observe(this, {
            it.handle(
                onSuccess = {
                    showRegistrationCompletedFragment()
                },
                onFailure = { error ->
                    showDefaultErrorSnackBar(authContainer)
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
