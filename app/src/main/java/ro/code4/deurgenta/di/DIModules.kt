package ro.code4.deurgenta.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import ro.code4.deurgenta.BuildConfig.API_URL
import ro.code4.deurgenta.BuildConfig.DEBUG
import ro.code4.deurgenta.analytics.AnalyticsService
import ro.code4.deurgenta.analytics.FirebaseAnalyticsService
import ro.code4.deurgenta.data.AppDatabase
import ro.code4.deurgenta.helper.SchedulersProvider
import ro.code4.deurgenta.helper.SchedulersProviderImpl
import ro.code4.deurgenta.helper.getToken
import ro.code4.deurgenta.repositories.AccountRepository
import ro.code4.deurgenta.repositories.AccountRepositoryImpl
import ro.code4.deurgenta.repositories.BackpacksRepository
import ro.code4.deurgenta.repositories.GroupRepository
import ro.code4.deurgenta.repositories.Repository
import ro.code4.deurgenta.repositories.UserRepository
import ro.code4.deurgenta.services.AccountService
import ro.code4.deurgenta.services.BackpackService
import ro.code4.deurgenta.services.GroupService
import ro.code4.deurgenta.services.UserService
import ro.code4.deurgenta.ui.address.AddressTypeViewModel
import ro.code4.deurgenta.ui.address.ConfigureAddressViewModel
import ro.code4.deurgenta.ui.address.SaveAddressViewModel
import ro.code4.deurgenta.ui.auth.AuthViewModel
import ro.code4.deurgenta.ui.auth.login.LoginFormViewModel
import ro.code4.deurgenta.ui.auth.register.RegisterViewModel
import ro.code4.deurgenta.ui.auth.reset.ResetPasswordViewModel
import ro.code4.deurgenta.ui.backpack.edit.EditBackpackItemViewModel
import ro.code4.deurgenta.ui.backpack.items.BackpackItemsViewModel
import ro.code4.deurgenta.ui.backpack.main.BackpacksViewModel
import ro.code4.deurgenta.ui.courses.CoursesFilterViewModel
import ro.code4.deurgenta.ui.courses.CoursesViewModel
import ro.code4.deurgenta.ui.group.CreateGroupViewModel
import ro.code4.deurgenta.ui.group.edit.EditGroupViewModel
import ro.code4.deurgenta.ui.group.listing.GroupsListingViewModel
import ro.code4.deurgenta.ui.home.HomeViewModel
import ro.code4.deurgenta.ui.main.MainViewModel
import ro.code4.deurgenta.ui.onboarding.OnboardingViewModel
import ro.code4.deurgenta.ui.splashscreen.SplashScreenViewModel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

val gson: Gson by lazy {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.excludeFieldsWithoutExposeAnnotation().create()
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
        HttpLoggingInterceptor().apply {
            level = if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    single {
        with(OkHttpClient.Builder()) {
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            connectTimeout(10, TimeUnit.SECONDS)
            addInterceptor(get<Interceptor>())
            addInterceptor(get<HttpLoggingInterceptor>())
            build()
        }
    }

    single {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(get())
            .build()
    }
    single<AccountService> { get<Retrofit>().create() }
    single<GroupService> { get<Retrofit>().create() }
    single<UserService> { get<Retrofit>().create() }
    single<BackpackService> { get<Retrofit>().create() }
    single { Repository() }
    single { BackpacksRepository(get<AppDatabase>().backpackDao(), get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single { UserRepository(get<AppDatabase>().userDao(), get()) }
    single { GroupRepository(get(), get<AppDatabase>().groupDao()) }
    single<SchedulersProvider> { SchedulersProviderImpl() }
}

val dbModule = module {
    single { AppDatabase.getDatabase(get()) }
    single { Executors.newSingleThreadExecutor() }
}

val viewModelsModule = module {
    viewModel { AuthViewModel() }
    viewModel { LoginFormViewModel() }
    viewModel { ResetPasswordViewModel(get(), get()) }
    viewModel { RegisterViewModel() }
    viewModel { OnboardingViewModel() }
    viewModel { MainViewModel() }
    viewModel { SplashScreenViewModel(get()) }
    viewModel { BackpacksViewModel(get()) }
    viewModel { BackpackItemsViewModel(get()) }
    viewModel { EditBackpackItemViewModel(get()) }
    viewModel { ConfigureAddressViewModel(get()) }
    viewModel { SaveAddressViewModel(get()) }
    viewModel { AddressTypeViewModel(get(), get()) }
    viewModel { HomeViewModel() }
    viewModel { CoursesFilterViewModel(get()) }
    viewModel { CoursesViewModel(get()) }
    viewModel { CreateGroupViewModel(get()) }
    viewModel { GroupsListingViewModel(get(), get()) }
    viewModel { EditGroupViewModel(get(), get()) }
}

val analyticsModule = module {
    single<AnalyticsService> { FirebaseAnalyticsService(get()) }
}
