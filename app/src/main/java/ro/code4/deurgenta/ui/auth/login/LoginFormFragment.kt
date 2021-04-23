package ro.code4.deurgenta.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.R
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

        btn_forgot_password.setOnClickListener {
            // TODO handle lost password
            Toast.makeText(requireContext(), "Your password is lost forever!", Toast.LENGTH_SHORT).show()
        }

        callbackManager = CallbackManager.Factory.create()

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

    override fun onResume() {
        super.onResume()
        toolbar.title = resources.getString(R.string.login_auth_title)
    }

}