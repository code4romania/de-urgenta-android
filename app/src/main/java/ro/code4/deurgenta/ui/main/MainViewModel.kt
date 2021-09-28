package ro.code4.deurgenta.ui.main

import android.content.SharedPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ro.code4.deurgenta.helper.SingleLiveEvent
import ro.code4.deurgenta.helper.deleteToken
import ro.code4.deurgenta.ui.base.BaseViewModel

class MainViewModel : BaseViewModel(), KoinComponent {
    private val sharedPreferences: SharedPreferences by inject()

    private val onLogoutLiveData = SingleLiveEvent<Void>()

    fun onLogoutLiveData(): SingleLiveEvent<Void> = onLogoutLiveData

    fun logout() {
        sharedPreferences.deleteToken()
        onLogoutLiveData.call()
    }

}