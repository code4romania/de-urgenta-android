package ro.code4.deurgenta.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import ro.code4.deurgenta.analytics.Event
import ro.code4.deurgenta.analytics.Param
import ro.code4.deurgenta.analytics.ParamKey
import ro.code4.deurgenta.interfaces.AnalyticsScreenName
import ro.code4.deurgenta.services.AnalyticsService

abstract class BaseAnalyticsFragment : Fragment, AnalyticsScreenName {

    private val analyticsService: AnalyticsService by inject()

    protected constructor() : super()

    protected constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyticsService.logEvent(Event.SCREEN_OPEN, Param(ParamKey.NAME, this.javaClass.simpleName))
    }
}
