package ro.code4.deurgenta.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_about.*
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.takeUserTo
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class AboutFragment : BaseAnalyticsFragment() {
    override val screenName: Int
        get() = R.string.analytics_title_about

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        organization_facebook.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_FACEBOOK_URL) }
        organization_instagram.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_INSTAGRAM_URL) }
        organization_url.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_URL) }
        organization_github.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_GITHUB_URL) }
        organization_donate.setOnClickListener { requireActivity().takeUserTo(BuildConfig.CODE4RO_DONATE_URL) }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = getString(R.string.about_title)
    }

}