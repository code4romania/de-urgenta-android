package ro.code4.deurgenta.ui.register

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.BuildConfig.TERMS_AND_CONDITIONS
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentRegisterBinding
import ro.code4.deurgenta.ui.base.ViewModelFragment
import ro.code4.deurgenta.ui.login.LoginViewModel

class RegisterFragment : ViewModelFragment<RegisterViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_register
    override val screenName: Int
        get() = R.string.analytics_title_register

    override val viewModel: RegisterViewModel by inject()
    lateinit var authViewModel: LoginViewModel;
    var registerRequestDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = activity?.run {
            ViewModelProvider(this)[LoginViewModel::class.java]
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

    private fun registerObservable() {
        viewModel.isSubmitEnabled.observe(viewLifecycleOwner, {})
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerRequestDisposable?.dispose()
    }

}