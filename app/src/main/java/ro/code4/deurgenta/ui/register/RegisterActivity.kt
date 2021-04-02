package ro.code4.deurgenta.ui.register

import android.os.Bundle
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
    }

}