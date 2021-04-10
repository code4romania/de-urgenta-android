package ro.code4.deurgenta.ui.splashscreen

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.setToRotateIndefinitely
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.login.LoginActivity
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.register.RegisterActivity

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
        logo.setToRotateIndefinitely().start()
        viewModel.loginStatus.observe(this) { loginStatus ->
            val activity = when {
                loginStatus.isUserLoggedIn && loginStatus.onboardCompleted -> MainActivity::class.java
                loginStatus.isUserLoggedIn -> RegisterActivity::class.java
                else -> LoginActivity::class.java
            }
            startActivityWithoutTrace(activity)
        }
    }
}