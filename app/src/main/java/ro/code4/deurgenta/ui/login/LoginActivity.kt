package ro.code4.deurgenta.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity

class LoginActivity : BaseAnalyticsActivity<LoginViewModel>() {

    override val layout: Int
        get() = R.layout.activity_login
    override val screenName: Int
        get() = R.string.analytics_title_login

    override val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clickListenersSetup()
        loginUserObservable()
    }

    private fun clickListenersSetup() {
        loginButton.setOnClickListener {
            viewModel.login()
        }
    }

    private fun loginUserObservable() {
        viewModel.loggedIn().observe(this, Observer {
            it.handle(
                onSuccess = { activity ->
                     activity?.let(::startActivityWithoutTrace)
                },
                onFailure = {error ->
                    showDefaultErrorSnackBar(loginButton)

                    loginButton.isEnabled = true
                }
            )
        })
    }
}
