package ro.code4.deurgenta.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ro.code4.deurgenta.helper.logD
import ro.code4.deurgenta.helper.logW

class FirebaseAnalyticsService(context: Context) : AnalyticsService {

    private val firebaseService = FirebaseAnalytics.getInstance(context)

    override fun logEvent(event: Event, vararg params: Param) {
        logD("logAnalyticsEvent: ${event.name}")
        val bundle = Bundle()
        for ((k, v) in params) {
            when (v) {
                is String -> bundle.putString(k.title, v)
                is Int -> bundle.putInt(k.title, v)
                else -> logW("Not implemented bundle params for ${v.javaClass}")
            }
        }
        firebaseService.logEvent(event.title, bundle)
    }
}