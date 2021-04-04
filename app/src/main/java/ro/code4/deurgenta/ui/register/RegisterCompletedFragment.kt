package ro.code4.deurgenta.ui.register

import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.ViewModelFragment

class RegisterCompletedFragment : ViewModelFragment<RegisterViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_completed_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override val viewModel: RegisterViewModel by viewModel()
}