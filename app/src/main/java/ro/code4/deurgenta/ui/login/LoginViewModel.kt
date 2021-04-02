package ro.code4.deurgenta.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import org.koin.core.inject
import ro.code4.deurgenta.helper.Result
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.hasCompletedOnboarding
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.register.RegisterActivity

class LoginViewModel : BaseViewModel() {

    private val repository: Repository by inject()
    private val sharedPreferences: SharedPreferences by inject()

    private val loginLiveData = SingleLiveEvent<Result<Class<*>>>()

    fun loggedIn(): LiveData<Result<Class<*>>> = loginLiveData

    fun login() {
        val nextActivity = when (sharedPreferences.hasCompletedOnboarding()) {
            true -> MainActivity::class.java
            false -> RegisterActivity::class.java
        }
        loginLiveData.postValue(Result.Success(nextActivity))
    }

    fun signup() {
        val nextActivity = RegisterActivity::class.java
        loginLiveData.postValue(Result.Success(nextActivity))
    }

}
