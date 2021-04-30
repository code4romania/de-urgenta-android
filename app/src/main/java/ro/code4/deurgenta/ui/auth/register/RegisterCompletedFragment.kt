package ro.code4.deurgenta.ui.auth.register

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_completed_register.*
import kotlinx.android.synthetic.main.include_toolbar.toolbar
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.ViewModelFragment

class RegisterCompletedFragment : ViewModelFragment<RegisterViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_completed_register
    override val screenName: Int
        get() = R.string.analytics_title_register
    override lateinit var viewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = resources.getString(R.string.register_completed_title)
        to_login_screen.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}