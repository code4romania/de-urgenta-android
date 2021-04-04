package ro.code4.deurgenta.ui.register

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.ActivityRegisterBinding
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.helper.startActivityWithoutTrace

class RegisterActivity : BaseAnalyticsActivity<RegisterViewModel>() {

    override val layout: Int
        get() = R.layout.activity_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override val viewModel: RegisterViewModel by viewModel()

    var registerRequestDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBindingSetup()
        clickListenersSetup()
        registerUserObservable()
    }

    private fun dataBindingSetup() {
        DataBindingUtil.setContentView<ActivityRegisterBinding>(
            this, layout
        ).apply {
            this.lifecycleOwner = this@RegisterActivity
            this.viewmodel = viewModel
        }
    }

    private fun clickListenersSetup() {

        submitButton.setOnClickListener {
            val registerData = viewModel.getRegisterData()
            registerRequestDisposable = viewModel
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

    override fun onDestroy() {
        super.onDestroy()

        registerRequestDisposable?.dispose()
    }
}