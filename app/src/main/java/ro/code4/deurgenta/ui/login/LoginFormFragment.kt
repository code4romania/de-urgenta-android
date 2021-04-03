package ro.code4.deurgenta.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class LoginFormFragment : BaseAnalyticsFragment() {

    companion object {
        private const val RC_SIGN_IN = 7
    }

    private lateinit var callbackManager: CallbackManager
    private val appleLoginConfig = SignInWithAppleConfiguration(
        clientId = "ro.code5.deurgenta.ui.login",
        redirectUri = "www.deurgenta.ro/callback",
        scope = "name email"
    )


    override val screenName: Int
        get() = R.string.login_fragment_name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        callbackManager = CallbackManager.Factory.create()
        view.facebook_login_button.fragment = this

        view.loginButton.setOnClickListener {

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
                    //Handle success
                }
                is SignInWithAppleResult.Failure -> {
                    // Handle failure
                }
                is SignInWithAppleResult.Cancel -> {
                    // Handle user cancel
                }
            }
        }
        return view
    }

    private fun setFacebookLoginListener() {
        view?.facebook_login?.setOnClickListener {
            view?.facebook_login_button?.performClick()
        }


        // Callback registration
        view?.facebook_login_button?.registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

            }

            override fun onCancel() {
            }

            override fun onError(exception: FacebookException) {
            }
        })
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
            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google", " signInResult : failed code = ${e.statusCode}")
        }
    }

}