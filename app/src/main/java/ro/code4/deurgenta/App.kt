package ro.code4.deurgenta

import android.app.Application
import android.content.Context
import com.sylversky.fontreplacer.FontReplacer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ro.code4.deurgenta.modules.*
import ro.code4.deurgenta.ui.base.BaseActivity

class App : Application() {
    var currentActivity: BaseActivity<*>? = null

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(appModule, apiModule, dbModule, viewModelsModule, analyticsModule))
        }
        replaceFonts()
    }

    private fun replaceFonts() {
        val replacer = FontReplacer.Build(applicationContext)
        replacer.setDefaultFont("fonts/IBMPlexSans-Regular.ttf")
        replacer.setBoldFont("fonts/IBMPlexSans-Bold.ttf")
        replacer.setLightFont("fonts/IBMPlexSans-Light.ttf")
        replacer.setMediumFont("fonts/IBMPlexSans-SemiBold.ttf")
        replacer.applyFont()
    }
}