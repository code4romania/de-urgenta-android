package ro.code4.deurgenta.ui.register

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity

class RegisterActivity : BaseAnalyticsActivity<RegisterViewModel>() {

    override val layout: Int
        get() = R.layout.activity_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        clickListenersSetup()
    }

    private fun clickListenersSetup() {
        termsAndConditionsSetup()
    }

    private fun termsAndConditionsSetup() {
        val htmlString = getString(R.string.register_terms)

        termsCheckBox.text = "";
        termsTextView.text = Html.fromHtml(htmlString);
        termsTextView.isClickable = true;
        termsTextView.movementMethod = LinkMovementMethod.getInstance();

    }

}