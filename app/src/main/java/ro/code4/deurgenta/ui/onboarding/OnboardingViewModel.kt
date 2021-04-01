package ro.code4.deurgenta.ui.onboarding

import android.app.Application
import android.content.SharedPreferences
import org.koin.core.inject
import ro.code4.deurgenta.helper.completedOnboarding
import ro.code4.deurgenta.ui.base.BaseViewModel

class OnboardingViewModel : BaseViewModel() {
    private val app: Application by inject()
    private val preferences: SharedPreferences by inject()

    fun onboardingCompleted() {
        preferences.completedOnboarding()
    }
}