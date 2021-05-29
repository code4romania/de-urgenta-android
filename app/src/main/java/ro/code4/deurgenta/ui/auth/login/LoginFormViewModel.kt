package ro.code4.deurgenta.ui.auth.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.inject
import ro.code4.deurgenta.data.model.User
import ro.code4.deurgenta.helper.*
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.base.BaseViewModel
import ro.code4.deurgenta.ui.main.MainActivity
import ro.code4.deurgenta.ui.onboarding.OnboardingActivity

class LoginFormViewModel : BaseViewModel() {
    private val repository: Repository by inject()
    private val sharedPreferences: SharedPreferences by inject()

    private val loginLiveData = SingleLiveEvent<Result<Class<*>>>()

    fun loggedIn(): LiveData<Result<Class<*>>> = loginLiveData

    fun login(email: String, password: String) {
        loginLiveData.postValue(Result.Loading)
        repository.login(User(email, password))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            // Wait for a response from the API
            .subscribe(
                {
                    if (it.success) {
                        logI("Logged in successfully")

                        // Save the auth token
                        sharedPreferences.saveToken(it.token)

                        onSuccessfulLogin()
                    } else {
                        val errorMessage = it.errors.joinToString(", ")
                        onError(Throwable(errorMessage))
                    }
                },
                {
                    logE("Network error when trying to log in with email and password")
                    onError(it)
                }
            )
            .run { disposables.add(this) }
    }

    fun onSuccessfulLogin() {
        val nextActivity = when (sharedPreferences.hasCompletedOnboarding()) {
            true -> MainActivity::class.java
            false -> OnboardingActivity::class.java
        }
        loginLiveData.postValue(Result.Success(nextActivity))
    }

    override fun onError(throwable: Throwable) {
        logE("Login failed: ${throwable.message}")
        loginLiveData.postValue(Result.Failure(throwable))
    }
}
