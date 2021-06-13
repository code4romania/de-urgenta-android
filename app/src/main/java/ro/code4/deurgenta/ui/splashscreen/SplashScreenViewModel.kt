package ro.code4.deurgenta.ui.splashscreen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import ro.code4.deurgenta.helper.*
import ro.code4.deurgenta.ui.base.BaseViewModel

class SplashScreenViewModel(sharedPreferences: SharedPreferences) : BaseViewModel() {

    private val _loginStatus = SingleLiveEvent<LoginStatus>()
    val loginStatus: LiveData<LoginStatus> = _loginStatus

    init {
        // TODO: currently, there's no way to refresh the token
        // Uncomment the following line to force the user to sign in again
//        sharedPreferences.deleteToken()

        val isUserLoggedIn = sharedPreferences.getToken() != null
        _loginStatus.value = LoginStatus(
            isUserLoggedIn,
            sharedPreferences.hasCompletedOnboarding()
        )
    }
}

data class LoginStatus(
    val isUserLoggedIn: Boolean,
    val onboardCompleted: Boolean
)
