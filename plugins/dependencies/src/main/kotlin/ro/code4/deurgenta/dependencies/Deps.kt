package ro.code4.deurgenta.dependencies

object Libs {
    // AndroidX dependencies
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val annotations = "androidx.annotation:annotation:${Versions.annotations}"
    const val prefsKtx = "androidx.preference:preference-ktx:${Versions.prefsKtx}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleReactiveStreamsKtx = "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.lifecycle}"
    const val rxJava = "io.reactivex.rxjava3:rxjava:${Versions.rxJava}"
    const val rxAndroid = "io.reactivex.rxjava3:rxandroid:${Versions.rxAndroid}"
    const val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.viewpager2}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    // other dependencies
    val googlePlayServicesLocation = "com.google.android.gms:play-services-location:${Versions.googlePlayServices}"
    val desugar = "com.android.tools:desugar_jdk_libs:${Versions.desugar}"
    val viewBindingDelegate = "com.github.Zhuinden:fragmentviewbindingdelegate-kt:${Versions.viewBindingDelegate}"
    val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    val okhttpLogging = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    val retrofitRxjava3 = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
    val retrofitScalars = "com.squareup.retrofit2:converter-scalars:${Versions.retrofit}"
    val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    val roomRxjava3 = "androidx.room:room-rxjava3:${Versions.room}"

    // testing dependencies
    val junit = "junit:junit:${Versions.junit}"
    val testCore = "androidx.arch.core:core-testing:${Versions.testCore}"
    val testExtJunit = "androidx.test.ext:junit-ktx:${Versions.testExtJunit}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    val roomTest = "androidx.room:room-testing:${Versions.room}"
    val koinTest = "io.insert-koin:koin-test:${Versions.koin}"
    val mockk = "io.mockk:mockk:${Versions.mockk}"
    val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"
    val mockkAgentJvm = "io.mockk:mockk-agent-jvm:${Versions.mockk}"
}

object Plugins {
    const val androidTools = "com.android.tools.build:gradle:${Versions.androidTools}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val versions = "com.github.ben-manes:gradle-versions-plugin:${Versions.versionsPlugin}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
    const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
    const val sonarqube = "org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:${Versions.sonarqube}"
    const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val firebaseAppDistribution =
        "com.google.firebase:firebase-appdistribution-gradle:${Versions.firebaseAppDistribution}"
    const val detekt = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}"
}
