package ro.code4.deurgenta.ui.auth.register

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.BuildConfig.TERMS_AND_CONDITIONS
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentRegisterBinding
import ro.code4.deurgenta.ui.auth.AuthViewModel
import ro.code4.deurgenta.ui.base.ViewModelFragment

class RegisterFragment : ViewModelFragment<RegisterViewModel>() {

    override val layout: Int
        get() = R.layout.fragment_register
    override val screenName: Int
        get() = R.string.analytics_title_register

    override val viewModel: RegisterViewModel by inject()
    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding
    private var registerRequestDisposable: Disposable? = null

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
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        @Suppress("USELESS_CAST") // cast needed
        binding.includedToolbar.toolbar.setTitle(R.string.register_title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListenersSetup()
        registerObservable()
    }

    private fun clickListenersSetup() {
        binding.submitButton.setOnClickListener {
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

        if (!termsError.isNullOrBlank()) {
            binding.termsCheckBox.error = termsError
        }
        if (!passwordError.isNullOrBlank()) {
            binding.passwordEditText.error = passwordError
        }
        if (!emailError.isNullOrBlank()) {
            binding.emailEditText.error = emailError
        }
        if (!lastNameError.isNullOrBlank()) {
            binding.lastNameEditText.error = lastNameError
        }
        if (!firstNameError.isNullOrBlank()) {
            binding.firstNameEditText.error = firstNameError
        }
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
        binding.termsTextView.text = Html.fromHtml(termsText)
        binding.termsTextView.isClickable = true;
        binding.termsTextView.movementMethod = LinkMovementMethod.getInstance()
        binding.termsCheckBox.setOnClickListener {
            binding.termsCheckBox.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerRequestDisposable?.dispose()
    }
}