package ro.code4.deurgenta.ui.base

import ro.code4.deurgenta.interfaces.AnalyticsScreenName

abstract class BaseAnalyticsActivity<out T : BaseViewModel> : BaseActivity<T>(), AnalyticsScreenName {

    override fun onResume() {
        super.onResume()
        // TODO find a replacement for this(also implement it in the fragment!)
        //firebaseAnalytics.setCurrentScreen(this, getString(screenName), null)
    }
}