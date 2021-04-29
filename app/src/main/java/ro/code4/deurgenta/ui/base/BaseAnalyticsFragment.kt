package ro.code4.deurgenta.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.analytics.Event
import ro.code4.deurgenta.analytics.Param
import ro.code4.deurgenta.analytics.ParamKey
import ro.code4.deurgenta.helper.logD
import ro.code4.deurgenta.helper.logW
import ro.code4.deurgenta.interfaces.AnalyticsScreenName

abstract class BaseAnalyticsFragment : Fragment, AnalyticsScreenName {

    private val firebaseAnalytics: FirebaseAnalytics by inject()

    protected constructor() : super()

    protected constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logAnalyticsEvent(
            Event.SCREEN_OPEN,
            Param(ParamKey.NAME, this.javaClass.simpleName))
    }

    override fun onResume() {
        super.onResume()

        firebaseAnalytics.setCurrentScreen(requireActivity(), getString(screenName), null)
    }

    fun logAnalyticsEvent(event: Event, vararg params: Param) {
        logD("logAnalyticsEvent: ${event.name}")
        val bundle = Bundle()
        for ((k, v) in params ) {
            when (v) {
                is String -> bundle.putString(k.title, v)
                is Int -> bundle.putInt(k.title, v)
                else -> logW("Not implemented bundle params for ${v.javaClass}")
            }
        }
        firebaseAnalytics.logEvent(event.title, bundle)
    }
}
