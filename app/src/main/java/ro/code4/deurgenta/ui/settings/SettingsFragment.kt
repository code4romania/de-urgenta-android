package ro.code4.deurgenta.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_settings.*
import retrofit2.http.HTTP
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {

    private val contactEmailUri by lazy {
        Uri.fromParts(    "mailto",
            BuildConfig.SUPPORT_EMAIL,
            null    )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        report_problem.setOnClickListener {reportProblemClicked(it)}
    /* report_problem.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                // The intent does not have a URI, so declare the "text/plain" MIME type
                type = "text/html"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("jan@example.com")) // recipients
                putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                putExtra(Intent.EXTRA_TEXT, "Email message text")
            }
        }*/
    }

    private fun reportProblemClicked(view: View?) {
        val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                contactEmailUri
        )
        startActivity(
                Intent.createChooser(
                        emailIntent,
                        "Send email via"
                )
        )

    }

}