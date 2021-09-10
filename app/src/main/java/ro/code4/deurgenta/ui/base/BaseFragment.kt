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

/**
 * The base class for all fragments in the application, with the exception(for now) of fragments that currently use
 * data binding or require additional features(like system permissions).
 */
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId), AnalyticsScreenName {

    private val analyticsService: AnalyticsService by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        analyticsService.logEvent(Event.SCREEN_OPEN, Param(ParamKey.NAME, this.javaClass.simpleName))
    }
}