package ro.code4.deurgenta.ui.auth.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import org.koin.core.inject
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.response.RegisterResponse
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.isEmptyField
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.isValidEmail
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val app: Application by inject()
    private val repository: Repository by inject()
    private val registerLiveData = SingleLiveEvent<Result<Class<*>>>()

    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val termsAndConditions = MutableLiveData(false)


    val isRequestPending = MutableLiveData(false)

    val isSubmitEnabled = MediatorLiveData<Boolean>()

    init {
        isSubmitEnabled.addSource(isRequestPending) { checkSubmitEnabled() }
    }

    fun getRegisterData(): Register {
        return Register(firstName.value!!, lastName.value!!, email.value!!, password.value!!)
    }

    fun register(data: Register): Observable<RegisterResponse> {
        isRequestPending.postValue(true)
        return repository.register(data)
    }

    fun registered(): LiveData<Result<Class<*>>> = registerLiveData

    fun onRegisterSuccess() {
        isRequestPending.postValue(false)
        registerLiveData.postValue(Result.Success())
    }

    fun onRegisterFail(error: Throwable, message: String = "") {
        isRequestPending.postValue(false)
        registerLiveData.postValue(Result.Failure(error, message))
    }

    fun checkFormValid(): Boolean {
        return getFirstNameError() == null &&
                getLastNameError() == null &&
                getEmailError() == null &&
                getPasswordError() == null &&
                getTermsError() == null
    }

    fun getFirstNameError(): String? {
        if (firstName.value.isEmptyField()) {
            return app.resources.getString(R.string.error_field_required)
        }
        return null
    }

    fun getLastNameError(): String? {
        if (lastName.value.isEmptyField()) {
            return app.resources.getString(R.string.error_field_required)
        }
        return null
    }

    fun getEmailError(): String? {
        if (email.value.isEmptyField()) {
            return app.resources.getString(R.string.error_field_required)
        }
        if (email.value?.isValidEmail() != true) {
            return app.resources.getString(R.string.error_email_invalid)
        }
        return null
    }

    fun getPasswordError(): String? {
        if (password.value.isEmptyField()) {
            return app.resources.getString(R.string.error_field_required)
        }
        if (password.value!!.trim().length < 5) {
            return app.resources.getString(R.string.error_password_invalid)
        }
        return null
    }

    fun getTermsError(): String? {
        if (termsAndConditions.value != true) {
            return app.resources.getString(R.string.error_field_required)
        }
        return null
    }

    private fun checkSubmitEnabled() {
        val isEnabled = isRequestPending.value != true
        isSubmitEnabled.postValue(isEnabled)
    }

    override fun onCleared() {
        super.onCleared()

        isSubmitEnabled.removeSource(isRequestPending)
    }


}