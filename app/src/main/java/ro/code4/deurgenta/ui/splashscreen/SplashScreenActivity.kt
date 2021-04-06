package ro.code4.deurgenta.ui.splashscreen

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.login.LoginActivity
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.onboarding.OnboardingActivity

class SplashScreenActivity : BaseAnalyticsActivity<SplashScreenViewModel>() {

    override val layout: Int
        get() = R.layout.activity_splash_screen
    override val screenName: Int
        get() = R.string.analytics_title_splash

    override val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        val logoAnimator = ObjectAnimator.ofFloat(logo, "rotation", 0f, 360f)
        with(logoAnimator) {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            duration = 2000
            interpolator = LinearInterpolator()
            start()
        }
        viewModel.loginStatus.observe(this) { loginStatus ->
            val activity = when {
                loginStatus.isUserLoggedIn && loginStatus.onboardCompleted -> MainActivity::class.java
                loginStatus.isUserLoggedIn -> OnboardingActivity::class.java
                else -> LoginActivity::class.java
            }
            startActivityWithoutTrace(activity)
        }
    }
}