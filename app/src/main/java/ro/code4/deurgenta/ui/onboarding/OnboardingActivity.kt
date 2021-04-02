package ro.code4.deurgenta.ui.onboarding

import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity

class OnboardingActivity : BaseAnalyticsActivity<OnboardingViewModel>() {

    override val layout: Int
        get() = R.layout.activity_onboarding
    override val screenName: Int
        get() = R.string.analytics_title_onboarding

    override val viewModel: OnboardingViewModel by viewModel()
}