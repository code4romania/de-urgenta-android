package ro.code4.deurgenta.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import org.koin.core.inject
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.hasCompletedOnboarding
import ro.code4.deurgenta.ui.base.BaseViewModel
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.onboarding.OnboardingActivity

class LoginFormViewModel : BaseViewModel() {
    private val sharedPreferences: SharedPreferences by inject()

    private val loginLiveData = SingleLiveEvent<Result<Class<*>>>()
    fun loggedIn(): LiveData<Result<Class<*>>> = loginLiveData


    fun login() {
        val nextActivity = when (sharedPreferences.hasCompletedOnboarding()) {
            true -> MainActivity::class.java
            false -> OnboardingActivity::class.java
        }
        loginLiveData.postValue(Result.Success(nextActivity))
    }
}