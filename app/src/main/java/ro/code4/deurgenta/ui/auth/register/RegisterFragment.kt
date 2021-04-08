package ro.code4.deurgenta.ui.auth.register

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.BuildConfig.TERMS_AND_CONDITIONS
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentRegisterBinding
import ro.code4.deurgenta.ui.base.ViewModelFragment
import ro.code4.deurgenta.ui.auth.AuthViewModel

class RegisterFragment : ViewModelFragment<RegisterViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_register
    override val screenName: Int
        get() = R.string.analytics_title_register

    override val viewModel: RegisterViewModel by inject()
    lateinit var authViewModel: AuthViewModel;
    var registerRequestDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.run {
            ViewModelProvider(this)[AuthViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register, container, false
        )
        binding.lifecycleOwner = this;
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListenersSetup()
        registerObservable()
    }

    private fun clickListenersSetup() {
        submitButton.setOnClickListener {
            viewModel.register()
        }

        termsAndConditionsSetup()

    }

    private fun showFormErrors() {

        val firstNameError = viewModel.getFirstNameError()
        val lastNameError = viewModel.getLastNameError()
        val emailError = viewModel.getEmailError()
        val passwordError = viewModel.getPasswordError()
        val termsError = viewModel.getTermsError()

        if (!termsError.isNullOrBlank()) termsCheckBox.error = termsError
        if (!passwordError.isNullOrBlank()) passwordEditText.error = passwordError
        if (!emailError.isNullOrBlank()) emailEditText.error = emailError
        if (!lastNameError.isNullOrBlank()) lastNameEditText.error = lastNameError
        if (!firstNameError.isNullOrBlank()) firstNameEditText.error = firstNameError

    }

    private fun registerObservable() {
        viewModel.isSubmitEnabled.observe(viewLifecycleOwner, {})

        viewModel.formValid().observe(viewLifecycleOwner, {
            if (!it) {
                showFormErrors()
            }
        })

        viewModel.registered().observe(viewLifecycleOwner, {
            it.handle(
                onSuccess = {
                    authViewModel.onRegisterSuccess()
                },
                onFailure = { error ->
                    authViewModel.onRegisterFail(error)
                }
            )
        })
    }

    private fun termsAndConditionsSetup() {
        val termsLink = TERMS_AND_CONDITIONS
        val termsLinkText = getString(R.string.register_terms_link_text)
        val termsLinkHtml = "<a href='$termsLink'>$termsLinkText</a>"
        val termsText = getString(R.string.register_terms_text).replace("{link}", termsLinkHtml)
        termsTextView.text = Html.fromHtml(termsText);
        termsTextView.isClickable = true;
        termsTextView.movementMethod = LinkMovementMethod.getInstance();

        termsCheckBox.setOnClickListener {
            termsCheckBox.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerRequestDisposable?.dispose()
    }

}