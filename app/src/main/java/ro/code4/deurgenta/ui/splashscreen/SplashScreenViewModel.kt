package ro.code4.deurgenta.ui.splashscreen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import org.koin.core.inject
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.getToken
import ro.code4.deurgenta.helper.hasCompletedOnboarding
import ro.code4.deurgenta.ui.base.BaseViewModel

class SplashScreenViewModel : BaseViewModel() {
    private val sharedPreferences: SharedPreferences by inject()
    private val loginLiveData = SingleLiveEvent<LoginStatus>()

    fun loginLiveData(): LiveData<LoginStatus> = loginLiveData

    init {
        checkLogin()
    }

    private fun checkLogin() {
        val isLoggedIn = sharedPreferences.getToken() != null

        loginLiveData.postValue(
            LoginStatus(
                isLoggedIn,
                sharedPreferences.hasCompletedOnboarding()
            )
        )
    }

    data class LoginStatus(
        val isLoggedIn: Boolean,
        val onboardingCompleted: Boolean
    )

}