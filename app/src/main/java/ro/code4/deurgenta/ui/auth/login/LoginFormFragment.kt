package ro.code4.deurgenta.ui.auth.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.ViewModelFragment

class LoginFormFragment : ViewModelFragment<LoginFormViewModel>() {

    companion object {
        private const val RC_SIGN_IN = 7
    }

    //private lateinit var callbackManager: CallbackManager
    /*
    private val appleLoginConfig = SignInWithAppleConfiguration(
        clientId = "ro.code5.deurgenta.ui.login",
        redirectUri = "www.deurgenta.ro/callback",
        scope = "name email"
    )
    */

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

        //callbackManager = CallbackManager.Factory.create()

        view.login_btn.setOnClickListener {
            login_btn.isEnabled = false

            val email = username.text.toString()
            val password = password.text.toString()
            viewModel.login(email, password)
        }

        /* Commenting for now as they are not included in MVP
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
                    // TODO: set auth token
                    viewModel.onSuccessfulLogin()
                }
                is SignInWithAppleResult.Failure -> {
                    // Handle failure
                }
                is SignInWithAppleResult.Cancel -> {
                    // Handle user cancel
                }
            }
        }
        */

        loginUserObservable()
    }

    override fun onResume() {
        super.onResume()
        toolbar.title = resources.getString(R.string.login_auth_title)
    }

    /* Commenting for now as they are not included in MVP
    private fun setFacebookLoginListener() {
        view?.facebook_login?.setOnClickListener {
            // TODO: initiate Facebook login
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
            // TODO: use sign in result
            val account = completedTask.getResult(ApiException::class.java)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google", " signInResult : failed code = ${e.statusCode}")
        }
    }
    */

    private fun loginUserObservable() {
        viewModel.loggedIn().observe(viewLifecycleOwner, Observer {
            it.handle(
                onSuccess = { activity ->
                    activity?.let(::startActivityWithoutTrace)
                },
                onFailure = { error ->
                    // TODO: display some friendlier errors
                    Snackbar.make(login_btn, "Error: ${error.message}", Snackbar.LENGTH_LONG).show()

                    login_btn.isEnabled = true
                },
                onLoading = {
                    Snackbar.make(login_btn, "Loading", Snackbar.LENGTH_INDEFINITE).show()
                }
            )
        })
    }
}
