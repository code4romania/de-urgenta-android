package ro.code4.deurgenta.ui.register

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import org.koin.core.inject
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.response.RegisterResponse
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val repository: Repository by inject()
    private val registerLiveData = SingleLiveEvent<Result<Class<*>>>()
    private val registerData = Register("", "", "", "")
    private var _hasAgreedTerms = false

    var hasCompletedForm = MutableLiveData<Boolean>(false)


    var firstName: String
        @Bindable
        get() = registerData.firstName
        set(value) {
            registerData.firstName = value
            checkFormCompleted()
        }

    var lastName: String
        @Bindable
        get() = registerData.lastName
        set(value) {
            registerData.lastName = value
            checkFormCompleted()
        }

    var email: String
        @Bindable
        get() = registerData.email
        set(value) {
            registerData.email = value
            checkFormCompleted()
        }

    var password: String
        @Bindable
        get() = registerData.password
        set(value) {
            registerData.password = value
            checkFormCompleted()
        }

    var termsAgreed: Boolean
        @Bindable
        get() = _hasAgreedTerms
        set(value) {
            _hasAgreedTerms = value
            checkFormCompleted()
        }


    private fun checkFormCompleted() {
        val isFormCompleted =
            firstName.isNotEmpty() &&
                    lastName.isNotEmpty() &&
                    email.isNotEmpty() &&
                    password.length > 4 &&
                    termsAgreed

        hasCompletedForm.postValue(isFormCompleted)
    }

    fun getRegisterData(): Register {
        return registerData
    }

    fun register(data: Register): Observable<RegisterResponse> {
        return repository.register(data)
    }

    fun registered(): LiveData<Result<Class<*>>> = registerLiveData


    fun onRegisterSuccess() {
        registerLiveData.postValue(Result.Success())
    }

    fun onRegisterFail(error: Throwable, message: String = "") {
        registerLiveData.postValue(Result.Failure(error, message))
    }

}