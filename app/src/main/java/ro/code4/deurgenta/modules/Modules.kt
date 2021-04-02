package ro.code4.deurgenta.modules

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ro.code4.deurgenta.App
import ro.code4.deurgenta.BuildConfig.API_URL
import ro.code4.deurgenta.BuildConfig.DEBUG
import ro.code4.deurgenta.data.AppDatabase
import ro.code4.deurgenta.helper.getToken
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.ui.address.ConfigureAddressFragment
import ro.code4.deurgenta.ui.address.ConfigureAddressViewModel
import ro.code4.deurgenta.ui.address.SaveAddressViewModel
import ro.code4.deurgenta.ui.backpack.edit.EditBackpackItemViewModel
import ro.code4.deurgenta.ui.backpack.items.BackpackItemsViewModel
import ro.code4.deurgenta.ui.backpack.main.BackpackDetailsViewModel
import ro.code4.deurgenta.ui.backpack.main.BackpacksViewModel
import ro.code4.deurgenta.ui.home.HomeViewModel
import ro.code4.deurgenta.ui.auth.login.LoginFormViewModel
import ro.code4.deurgenta.ui.auth.AuthViewModel
import ro.code4.deurgenta.ui.main.MainViewModel
import ro.code4.deurgenta.ui.onboarding.OnboardingViewModel
import ro.code4.deurgenta.ui.auth.register.RegisterViewModel
import ro.code4.deurgenta.ui.splashscreen.SplashScreenViewModel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val gson: Gson by lazy {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.excludeFieldsWithoutExposeAnnotation().create()
}

val appModule = module {
    single { App.instance }
}

val apiModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single {
        Interceptor { chain ->
            val original = chain.request()

            val token = get<SharedPreferences>().getToken()
            val request = original.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json")
                .build()

            chain.proceed(request)
        }
    }

    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level =
            if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        interceptor
    }

    single {
        val httpClient = OkHttpClient.Builder()
        httpClient.readTimeout(10, TimeUnit.SECONDS)
        httpClient.writeTimeout(10, TimeUnit.SECONDS)
        httpClient.connectTimeout(10, TimeUnit.SECONDS)
        get<Interceptor?>()?.let {
            httpClient.addInterceptor(it)
        }
        get<HttpLoggingInterceptor?>()?.let {
            httpClient.addInterceptor(it)
        }
        httpClient.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(get<OkHttpClient>())
            .build()
    }
    single {
        Repository()
    }
}

val dbModule = module {
    single { AppDatabase.getDatabase(get()) }
    single { Executors.newSingleThreadExecutor() }
}

val viewModelsModule = module {
    viewModel { AuthViewModel() }
    viewModel { LoginFormViewModel() }
    viewModel { RegisterViewModel() }
    viewModel { OnboardingViewModel() }
    viewModel { MainViewModel() }
    viewModel { SplashScreenViewModel(get()) }
    viewModel { BackpacksViewModel(get()) }
    viewModel { BackpackDetailsViewModel() }
    viewModel { BackpackItemsViewModel(get()) }
    viewModel { EditBackpackItemViewModel(get()) }
    viewModel { ConfigureAddressViewModel(get())}
    viewModel { SaveAddressViewModel(get())}
    viewModel { HomeViewModel() }

}

val analyticsModule = module {
    single { FirebaseAnalytics.getInstance(get()) }
}