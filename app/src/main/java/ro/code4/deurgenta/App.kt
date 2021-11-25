package ro.code4.deurgenta

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ro.code4.deurgenta.di.analyticsModule
import ro.code4.deurgenta.di.apiModule
import ro.code4.deurgenta.di.dbModule
import ro.code4.deurgenta.di.viewModelsModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(listOf(apiModule, dbModule, viewModelsModule, analyticsModule))
        }
    }
}
