package ro.code4.deurgenta.ui.splashscreen

import android.os.Bundle
import androidx.lifecycle.Observer
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.login.LoginActivity
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.onboarding.OnboardingActivity
import ro.code4.deurgenta.widget.ProgressDialogFragment

class SplashScreenActivity : BaseAnalyticsActivity<SplashScreenViewModel>() {

    override val layout: Int
        get() = R.layout.activity_splash_screen
    override val screenName: Int
        get() = R.string.analytics_title_splash

    private val progressDialog: ProgressDialogFragment by lazy {
        ProgressDialogFragment().also {
            it.isCancelable = false
        }
    }
    override val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog.showNow(supportFragmentManager, ProgressDialogFragment.TAG)

        viewModel.loginLiveData().observe(this, Observer { loginStatus ->
            progressDialog.dismiss()
            val activity = when {
                loginStatus.isLoggedIn && loginStatus.onboardingCompleted -> MainActivity::class.java
                loginStatus.isLoggedIn -> OnboardingActivity::class.java
                else -> LoginActivity::class.java
            }

            startActivityWithoutTrace(activity)
        })
    }

    override fun onDestroy() {
        if (progressDialog.isResumed) progressDialog.dismissAllowingStateLoss()
        super.onDestroy()
    }
}