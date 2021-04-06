package ro.code4.deurgenta.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import org.koin.core.inject
import ro.code4.deurgenta.data.model.Register
import ro.code4.deurgenta.data.model.response.RegisterResponse
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.isValidEmail
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {

    private val repository: Repository by inject()
    private val registerLiveData = SingleLiveEvent<Result<Class<*>>>()
    private val isFormCompleted = MutableLiveData(false)

    val firstName = MutableLiveData<String>("")
    val lastName = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val termsAndConditions = MutableLiveData(false)


    val isRequestPending = MutableLiveData(false)

    val isSubmitEnabled = MediatorLiveData<Boolean>()

    init {
        isSubmitEnabled.addSource(firstName) { checkFormCompleted() }
        isSubmitEnabled.addSource(lastName) { checkFormCompleted() }
        isSubmitEnabled.addSource(email) { checkFormCompleted() }
        isSubmitEnabled.addSource(password) { checkFormCompleted() }
        isSubmitEnabled.addSource(termsAndConditions) { checkFormCompleted() }
        isSubmitEnabled.addSource(isFormCompleted) { checkSubmitEnabled() }
        isSubmitEnabled.addSource(isRequestPending) { checkSubmitEnabled() }
    }

    private fun checkFormCompleted() {
        val isFormCompleted = firstName.value?.isNotEmpty() == true &&
                lastName.value?.isNotEmpty() == true &&
                email.value?.isValidEmail() == true &&
                password.value?.length!! > 4 &&
                termsAndConditions.value == true

        this.isFormCompleted.postValue(isFormCompleted)
    }

    private fun checkSubmitEnabled() {
        var isEnabled = isFormCompleted.value == true && isRequestPending.value != true
        isSubmitEnabled.postValue(isEnabled)
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

    override fun onCleared() {
        super.onCleared()

        isSubmitEnabled.removeSource(firstName)
        isSubmitEnabled.removeSource(lastName)
        isSubmitEnabled.removeSource(email)
        isSubmitEnabled.removeSource(password)
        isSubmitEnabled.removeSource(termsAndConditions)
        isSubmitEnabled.removeSource(isFormCompleted)
        isSubmitEnabled.removeSource(isRequestPending)
    }

}