package ro.code4.deurgenta.ui.auth.reset

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentResetPasswordBinding
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.EmailNotValid
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.PasswordTooShort
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.Valid
import ro.code4.deurgenta.ui.base.BaseFragment

class ResetPasswordFragment : BaseFragment(R.layout.fragment_reset_password) {

    override val screenName: Int
        get() = R.string.analytics_title_reset_password
    private val viewModel: ResetPasswordViewModel by viewModel()
    private val binding: FragmentResetPasswordBinding by viewBinding(FragmentResetPasswordBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailInput.doOnTextChanged { email, _, _, _ ->
            viewModel.validateEmail(email.toString())
        }
        binding.newPasswordInput.doOnTextChanged { password, _, _, _ ->
            if (password != null && password.isEmpty()) {
                binding.newPasswordInputLayout.error = null
                return@doOnTextChanged
            }
            viewModel.validatePassword(password.toString())
        }
        binding.confirmPasswordInput.doOnTextChanged { confirmedPassword, _, _, _ ->
            val chosenPassword = binding.newPasswordInput.text.toString()
            if (chosenPassword.isEmpty() || (confirmedPassword != null && confirmedPassword.isEmpty())) {
                binding.confirmPasswordInputLayout.error = null
                return@doOnTextChanged
            }
            binding.confirmPasswordInputLayout.error =
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
            binding.emailInputLayout.error = when (status) {
                Valid -> null
                EmailNotValid -> getString(R.string.reset_password_invalid_email)
                else -> throw IllegalArgumentException("Unknown validation status for field: reset email")
            }
        }
        viewModel.passwordValidationStatus.observe(viewLifecycleOwner) { status ->
            binding.newPasswordInputLayout.error = when (status) {
                Valid -> null
                PasswordTooShort -> getString(R.string.reset_password_password_too_short)
                else -> throw IllegalArgumentException("Unknown validation status for field: reset password")
            }
        }
        binding.btnResetPassword.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val newPassword = binding.newPasswordInput.text.toString()
            val confirmedPassword = binding.confirmPasswordInput.text.toString()
            if (newPassword == confirmedPassword) {
                viewModel.resetPassword(email, newPassword)
            } else {
                binding.confirmPasswordInputLayout.error = getString(R.string.reset_password_different_passwords)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.includedToolbar.toolbar.title = resources.getString(R.string.reset_password_title)
    }

    companion object {
        private const val RESET_PASSWORD_DIALOG = "RESET_PASSWORD_DIALOG"

        fun newInstance() = ResetPasswordFragment()
    }
}



