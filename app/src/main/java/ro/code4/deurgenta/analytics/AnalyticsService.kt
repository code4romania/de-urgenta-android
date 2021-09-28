package ro.code4.deurgenta.analytics

interface AnalyticsService {

    fun logEvent(event: Event, vararg params: Param)
}

