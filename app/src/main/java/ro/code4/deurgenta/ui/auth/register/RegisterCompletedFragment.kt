package ro.code4.deurgenta.ui.auth.register

import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.ViewModelFragment

class RegisterCompletedFragment : ViewModelFragment<RegisterViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_completed_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override lateinit var viewModel: RegisterViewModel
}