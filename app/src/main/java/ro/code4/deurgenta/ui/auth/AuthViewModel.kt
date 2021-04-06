package ro.code4.deurgenta.ui.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import org.koin.core.inject
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.hasCompletedOnboarding
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.onboarding.OnboardingActivity

class AuthViewModel : BaseViewModel() {

    private val repository: Repository by inject()
    private val sharedPreferences: SharedPreferences by inject()

    private val loginLiveData = SingleLiveEvent<Result<Class<*>>>()
    private val registerLiveData = SingleLiveEvent<Result<Class<*>>>()
    private val loginNavigation = SingleLiveEvent<Result<Class<*>>>()
    private val signupNavigation = SingleLiveEvent<Result<Class<*>>>()

    fun loggedIn(): LiveData<Result<Class<*>>> = loginLiveData
    fun registered(): LiveData<Result<Class<*>>> = registerLiveData
    fun loginNavigation(): LiveData<Result<Class<*>>> = loginNavigation
    fun signupNavigation(): LiveData<Result<Class<*>>> = signupNavigation

    fun login() {
        val nextActivity = when (sharedPreferences.hasCompletedOnboarding()) {
            true -> MainActivity::class.java
            false -> OnboardingActivity::class.java
        }
        loginLiveData.postValue(Result.Success(nextActivity))
    }

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
