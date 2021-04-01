package ro.code4.deurgenta.ui.onboarding

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_onboarding.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.main.MainActivity

class OnboardingActivity : BaseAnalyticsActivity<OnboardingViewModel>() {

    override val layout: Int
        get() = R.layout.activity_onboarding
    override val screenName: Int
        get() = R.string.analytics_title_onboarding
    override val viewModel: OnboardingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        onboarding.setOnClickListener {
            viewModel.onboardingCompleted()

            startActivityWithoutTrace(MainActivity::class.java)
        }
    }

}