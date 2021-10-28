package ro.code4.deurgenta.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.data.model.MapAddressType
import ro.code4.deurgenta.databinding.FragmentOnboardingConfigureBinding
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseFragment
import ro.code4.deurgenta.ui.main.MainActivity

class OnboardingConfigureFragment : BaseFragment(R.layout.fragment_onboarding_configure) {

    override val screenName: Int
        get() = R.string.analytics_title_onboarding_setup
    private val binding: FragmentOnboardingConfigureBinding by viewBinding(FragmentOnboardingConfigureBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.onboardingActions) {
            configureAddress.setOnClickListener {
                findNavController().navigate(
                    OnboardingConfigureFragmentDirections.actionConfigureAddress(
                        mapAddressType = MapAddressType.HOME,
                        R.string.add_home_address
                    )
                )
            }
            configureGroup.setOnClickListener {
                findNavController().navigate(OnboardingConfigureFragmentDirections.actionConfigureGroup())
            }
            configureBackpack.setOnClickListener {
                findNavController().navigate(OnboardingConfigureFragmentDirections.actionConfigureBackpack())
            }

            configureCourses.setOnClickListener {
                findNavController().navigate(OnboardingConfigureFragmentDirections.actionConfigureCourses())
            }
        }
        if (BuildConfig.DEBUG) {
            // TODO only for testing, to be removed when onboarding implementation is complete
            binding.skipOnboarding.setOnClickListener {
                startActivityWithoutTrace(MainActivity::class.java)
            }
        }
    }
}
