package ro.code4.deurgenta.ui.register

import android.os.Bundle
import android.os.Debug
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.helper.startActivityWithoutTrace

class RegisterActivity : BaseAnalyticsActivity<RegisterViewModel>() {

    override val layout: Int
        get() = R.layout.activity_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clickListenersSetup()
        registerUserObservable()
    }

    private fun clickListenersSetup() {

        submitButton.setOnClickListener {
            val registerData = getRegisterData()
            viewModel
                .register(registerData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewModel.onRegisterSuccess()
                }, {
                    val errorMessage = getString(R.string.error_generic)
                    viewModel.onRegisterFail(it, errorMessage)
                })
        }

        termsAndConditionsSetup()

    }

    private fun termsAndConditionsSetup() {
        val htmlString = getString(R.string.register_terms)

        termsCheckBox.text = "";
        termsTextView.text = Html.fromHtml(htmlString);
        termsTextView.isClickable = true;
        termsTextView.movementMethod = LinkMovementMethod.getInstance();

    }

    private fun getRegisterData(): Register {

        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        return Register(
            firstName,
            lastName,
            email,
            password
        )
    }

    private fun registerUserObservable() {
        viewModel.registered().observe(this, Observer {
            it.handle(
                onSuccess = { activity ->
                    activity?.let(::startActivityWithoutTrace)
                },
                onFailure = { error ->
                    showDefaultErrorSnackBar(submitButton)

                    submitButton.isEnabled = true
                }
            )
        })
    }
}