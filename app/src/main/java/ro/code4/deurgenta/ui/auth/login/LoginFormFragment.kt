package ro.code4.deurgenta.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.ViewModelFragment

class LoginFormFragment : ViewModelFragment<LoginFormViewModel>() {

    companion object {
        private const val RC_SIGN_IN = 7
    }

    private lateinit var callbackManager: CallbackManager
    private val appleLoginConfig = SignInWithAppleConfiguration(
        clientId = "ro.code5.deurgenta.ui.login",
        redirectUri = "www.deurgenta.ro/callback",
        scope = "name email"
    )

    override val layout: Int
        get() = R.layout.fragment_login
    override val viewModel: LoginFormViewModel by inject()

    override val screenName: Int
        get() = R.string.login_fragment_name

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callbackManager = CallbackManager.Factory.create()

        view.close_btn.setOnClickListener {
            activity?.onBackPressed()
        }

        view.login_btn.setOnClickListener {
            viewModel.login()
        }

        view.google_login.setOnClickListener {
            handleGoogleLoginClick()
        }


        setFacebookLoginListener()
        view.apple_login.setOnClickListener {
            view.sign_in_with_apple_button.performClick()
        }

        view.sign_in_with_apple_button.setUpSignInWithAppleOnClick(
            childFragmentManager,
            appleLoginConfig
        ) { result ->
            when (result) {
                is SignInWithAppleResult.Success -> {
                    viewModel.login()
                }
                is SignInWithAppleResult.Failure -> {
                    // Handle failure
                }
                is SignInWithAppleResult.Cancel -> {
                    // Handle user cancel
                }
            }
        }

        loginUserObservable()
    }

    private fun loginUserObservable() {
        viewModel.loggedIn().observe(viewLifecycleOwner, {
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

    private fun setFacebookLoginListener() {
        view?.facebook_login?.setOnClickListener {
            viewModel.login()
        }
    }

    private fun handleGoogleLoginClick() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            viewModel.login()
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google", " signInResult : failed code = ${e.statusCode}")
        }
    }

}