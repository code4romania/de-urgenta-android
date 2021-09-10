package ro.code4.deurgenta.ui.about

import android.os.Bundle
import android.view.View
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentAboutBinding
import ro.code4.deurgenta.helper.takeUserTo
import ro.code4.deurgenta.ui.base.BaseFragment

class AboutFragment : BaseFragment(R.layout.fragment_about) {

    private val binding: FragmentAboutBinding by viewBinding(FragmentAboutBinding::bind)

    override val screenName: Int
        get() = R.string.analytics_title_about

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.organizationFacebook.setOnClickListener {
            requireActivity().takeUserTo(BuildConfig.CODE4RO_FACEBOOK_URL)
        }
        binding.organizationInstagram.setOnClickListener {
            requireActivity().takeUserTo(BuildConfig.CODE4RO_INSTAGRAM_URL)
        }
        binding.organizationUrl.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_URL) }
        binding.organizationGithub.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_GITHUB_URL) }
        binding.organizationDonate.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_DONATE_URL) }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = getString(R.string.about_title)
    }
}