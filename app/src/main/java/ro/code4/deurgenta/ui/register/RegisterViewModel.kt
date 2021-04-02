package ro.code4.deurgenta.ui.register

import android.app.Application
import android.content.SharedPreferences
import org.koin.core.inject
import ro.code4.deurgenta.ui.base.BaseViewModel

class RegisterViewModel : BaseViewModel() {
    private val app: Application by inject()
    private val preferences: SharedPreferences by inject()

}