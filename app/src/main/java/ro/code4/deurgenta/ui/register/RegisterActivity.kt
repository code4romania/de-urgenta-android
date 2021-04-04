package ro.code4.deurgenta.ui.register

import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseAnalyticsActivity
import ro.code4.deurgenta.ui.login.LoginActivity

class RegisterActivity : BaseAnalyticsActivity<RegisterViewModel>() {

    override val layout: Int
        get() = R.layout.activity_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override val viewModel: RegisterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<RegisterFragment>(R.id.fragment_container_view)
            }
        }

        registerObservables()
        clickListenersSetup()

    }

    private fun clickListenersSetup() {
        closeImageView.setOnClickListener {
            val nextActivity = LoginActivity::class.java
            startActivityWithoutTrace(nextActivity)

        }
    }

    private fun registerObservables() {
        viewModel.registered().observe(this, {
            it.handle(
                onSuccess = {
                    showRegistrationCompletedFragment()
                },
                onFailure = { error ->
                    showDefaultErrorSnackBar(submitButton)
                }
            )
        })

        viewModel.isSubmitEnabled.observe(this, {})
    }

    private fun showRegistrationCompletedFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RegisterCompletedFragment>(R.id.fragment_container_view)
        }
    }
}