package ro.code4.deurgenta.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.replaceFragment
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.auth.login.LoginFormFragment
import ro.code4.deurgenta.ui.auth.register.RegisterCompletedFragment
import ro.code4.deurgenta.ui.auth.register.RegisterFragment
import ro.code4.deurgenta.ui.base.ViewModelFragment

class AuthFragment : ViewModelFragment<AuthViewModel>()  {

    override val layout: Int
        get() = R.layout.fragment_auth
    override val screenName: Int
        get() = R.string.analytics_title_login

    override lateinit var viewModel: AuthViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this)[AuthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListenersSetup()
    }


    private fun clickListenersSetup() {
        loginButton.setOnClickListener {
            viewModel.onLoginClicked()
        }
        signupButton.setOnClickListener {
            viewModel.onSignUpClicked()
        }
    }
}
