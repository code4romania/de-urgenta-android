package ro.code4.deurgenta.ui.auth.reset

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_reset_password.btn_reset_password
import kotlinx.android.synthetic.main.fragment_reset_password.confirm_password_input
import kotlinx.android.synthetic.main.fragment_reset_password.confirm_password_input_layout
import kotlinx.android.synthetic.main.fragment_reset_password.email_input
import kotlinx.android.synthetic.main.fragment_reset_password.email_input_layout
import kotlinx.android.synthetic.main.fragment_reset_password.new_password_input
import kotlinx.android.synthetic.main.fragment_reset_password.new_password_input_layout
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.EmailNotValid
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.PasswordTooShort
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.Valid
import ro.code4.deurgenta.ui.base.ViewModelFragment

class ResetPasswordFragment : ViewModelFragment<ResetPasswordViewModel>() {

    override val screenName: Int
        get() = R.string.analytics_title_reset_password
    override val layout: Int
        get() = R.layout.fragment_reset_password
    override val viewModel: ResetPasswordViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        email_input.doOnTextChanged { email, _, _, _ ->
            viewModel.validateEmail(email.toString())
        }
        new_password_input.doOnTextChanged { password, _, _, _ ->
            if (password != null && password.isEmpty()) {
                new_password_input_layout.error = null
                return@doOnTextChanged
            }
            viewModel.validatePassword(password.toString())
        }
        confirm_password_input.doOnTextChanged { confirmedPassword, _, _, _ ->
            val chosenPassword = new_password_input.text.toString()
            if (chosenPassword.isEmpty() || (confirmedPassword != null && confirmedPassword.isEmpty())) {
                confirm_password_input_layout.error = null
                return@doOnTextChanged
            }
            confirm_password_input_layout.error =
                if (confirmedPassword != null && confirmedPassword.toString() != chosenPassword) {
                    getString(R.string.reset_password_different_passwords)
                } else {
                    null
                }
        }
        viewModel.resetInitiated.observe(viewLifecycleOwner) {
            val dialog = childFragmentManager.findFragmentByTag(RESET_PASSWORD_DIALOG) as? DialogFragment
            if (dialog != null && !dialog.isVisible) {
                dialog.dismiss()
            }
            ResetPasswordInfoDialogFragment().show(childFragmentManager, RESET_PASSWORD_DIALOG)
        }
        viewModel.emailValidationStatus.observe(viewLifecycleOwner) { status ->
            email_input_layout.error = when (status) {
                Valid -> null
                EmailNotValid -> getString(R.string.reset_password_invalid_email)
                else -> throw IllegalArgumentException("Unknown validation status for field: reset email")
            }
        }
        viewModel.passwordValidationStatus.observe(viewLifecycleOwner) { status ->
            new_password_input_layout.error = when (status) {
                Valid -> null
                PasswordTooShort -> getString(R.string.reset_password_password_too_short)
                else -> throw IllegalArgumentException("Unknown validation status for field: reset password")
            }
        }
        btn_reset_password.setOnClickListener {
            val email = email_input.text.toString()
            val newPassword = new_password_input.text.toString()
            val confirmedPassword = confirm_password_input.text.toString()
            if (newPassword == confirmedPassword) {
                viewModel.resetPassword(email, newPassword)
            } else {
                confirm_password_input_layout.error = getString(R.string.reset_password_different_passwords)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        toolbar.title = resources.getString(R.string.reset_password_title)
    }

    companion object {
        private const val RESET_PASSWORD_DIALOG = "RESET_PASSWORD_DIALOG"

        fun newInstance() = ResetPasswordFragment()
    }
}



