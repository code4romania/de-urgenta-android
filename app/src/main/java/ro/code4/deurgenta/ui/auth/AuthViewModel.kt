package ro.code4.deurgenta.ui.auth

import androidx.lifecycle.LiveData
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.ui.base.BaseViewModel

class AuthViewModel : BaseViewModel() {

    private val loginLiveData = SingleLiveEvent<Result<Class<*>>>()
    private val registerLiveData = SingleLiveEvent<Result<Class<*>>>()
    private val loginNavigation = SingleLiveEvent<Result<Class<*>>>()
    private val signupNavigation = SingleLiveEvent<Result<Class<*>>>()

    fun loggedIn(): LiveData<Result<Class<*>>> = loginLiveData
    fun registered(): LiveData<Result<Class<*>>> = registerLiveData
    fun loginNavigation(): LiveData<Result<Class<*>>> = loginNavigation
    fun signupNavigation(): LiveData<Result<Class<*>>> = signupNavigation

    fun onRegisterSuccess() {
        registerLiveData.postValue(Result.Success())
    }

    fun onRegisterFail(error: Throwable, message: String = "") {
        registerLiveData.postValue(Result.Failure(error, message))
    }

    fun onLoginClicked() {
        loginNavigation.postValue(Result.Success())
    }

    fun onSignUpClicked() {
        signupNavigation.postValue(Result.Success())
    }

}
