package ro.code4.deurgenta.ui.auth.reset

import androidx.lifecycle.LiveData
import ro.code4.deurgenta.helper.Resource
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.plusAssign
import ro.code4.deurgenta.repositories.AccountRepository
import ro.code4.deurgenta.ui.auth.reset.FieldValidationStatus.Valid
import ro.code4.deurgenta.ui.base.BaseViewModel

class ResetPasswordViewModel(
    private val accountRepository: AccountRepository,
    private val schedulersProvider: SchedulersProvider
) : BaseViewModel() {

    private val _emailValidation = SingleLiveEvent<FieldValidationStatus>()
    val emailValidationStatus: LiveData<FieldValidationStatus> = _emailValidation
    private val _passwordValidation = SingleLiveEvent<FieldValidationStatus>()
    val passwordValidationStatus: LiveData<FieldValidationStatus> = _passwordValidation
    private val _resetInitiated = SingleLiveEvent<Boolean>()
    val resetInitiated: LiveData<Boolean> = _resetInitiated
    private val _resetStatus = SingleLiveEvent<Resource<Unit>>()
    val resetStatus: LiveData<Resource<Unit>> = _resetStatus
    private val emailValidator: FieldValidator = EmailFieldValidator()
    private val passwordValidator: FieldValidator = PasswordFieldValidator()

    init {
        _emailValidation.value = Valid
        _passwordValidation.value = Valid
    }

    fun resetPassword(email: String, newPassword: String) {
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(newPassword)
        if (isEmailValid && isPasswordValid) {
            _resetInitiated.value = true
            _resetStatus.value = Resource.loading()
            disposables += accountRepository
                .resetPassword(email, newPassword)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.main())
                .subscribe({
                    _resetStatus.value = Resource.success()
                }, {
                    _resetStatus.value = Resource.error(it)
                })
        }
    }

    fun validateEmail(email: String): Boolean {
        val result = emailValidator.validate(email)
        _emailValidation.value = result
        return result == Valid
    }

    fun validatePassword(password: String): Boolean {
        val result = passwordValidator.validate(password)
        _passwordValidation.value = result
        return result == Valid
    }
}
