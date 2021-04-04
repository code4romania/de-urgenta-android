package ro.code4.deurgenta.ui.home

import org.koin.android.ext.android.inject
import ro.code4.deurgenta.R
import ro.code4.deurgenta.ui.base.ViewModelFragment

class HomeFragment: ViewModelFragment<HomeViewModel>() {
    override val layout: Int
        get() = R.layout.fragment_home
    override val screenName: Int
        get() = R.string.analytics_title_home;
    override val viewModel: HomeViewModel by inject()
}