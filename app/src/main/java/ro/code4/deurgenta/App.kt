package ro.code4.deurgenta

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ro.code4.deurgenta.modules.*

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AndroidThreeTen.init(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, apiModule, dbModule, viewModelsModule, analyticsModule))
        }
    }
}