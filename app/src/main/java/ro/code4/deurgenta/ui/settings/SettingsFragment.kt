package ro.code4.deurgenta.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.databinding.FragmentSettingsBinding
import ro.code4.deurgenta.ui.base.BaseFragment

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override val screenName: Int
        get() = R.string.analytics_title_settings
    private val binding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)

    private val contactEmailUri by lazy {
        Uri.fromParts("mailto", BuildConfig.SUPPORT_EMAIL, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reportProblem.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO, contactEmailUri)
            startActivity(Intent.createChooser(emailIntent, getString(R.string.report_problem)))
        }
    }
}