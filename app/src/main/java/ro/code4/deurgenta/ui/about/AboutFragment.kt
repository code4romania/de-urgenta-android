package ro.code4.deurgenta.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_about.*
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.BaseAnalyticsFragment

class AboutFragment : BaseAnalyticsFragment() {
    override val screenName: Int
        get() = R.string.analytics_title_about

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        organization_facebook.setOnClickListener { takeUserTo(BuildConfig.CODE4RO_FACEBOOK_URL) }
        organization_instagram.setOnClickListener { takeUserTo(BuildConfig.CODE4RO_INSTAGRAM_URL) }
        organization_url.setOnClickListener { takeUserTo(BuildConfig.CODE4RO_URL) }
        organization_github.setOnClickListener { takeUserTo(BuildConfig.CODE4RO_GITHUB_URL) }
        organization_donate.setOnClickListener { takeUserTo(BuildConfig.CODE4RO_DONATE_URL) }
    }

    private fun takeUserTo(url: String) {
        val outIntent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
        if (requireActivity().packageManager.resolveActivity(outIntent, 0) != null) {
            requireActivity().startActivity(outIntent)
        } else {
            Toast.makeText(requireActivity(), R.string.about_no_url_handler, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().title = getString(R.string.about_title)
    }

}