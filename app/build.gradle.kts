import ro.code4.deurgenta.dependencies.Libs
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("configuration")
    id("org.sonarqube")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

variantConfigFields {
    addField("apiUrl", "API_URL", "String")
    addField("termsAndConditions", "TERMS_AND_CONDITIONS", "String")
    addField("addressSearchRadius", "SEARCH_RADIUS", "double")
    addField("code4roUrl", "CODE4RO_URL", "String")
    addField("code4roFacebookUrl", "CODE4RO_FACEBOOK_URL", "String")
    addField("code4roInstagramUrl", "CODE4RO_INSTAGRAM_URL", "String")
    addField("code4roGithubUrl", "CODE4RO_GITHUB_URL", "String")
    addField("code4roDonateUrl", "CODE4RO_DONATE_URL", "String")
    addField("supportEmail", "SUPPORT_EMAIL", "String")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "ro.code4.deurgenta"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    sourceSets {
        val sharedTestsFolder = "src/sharedTest/java"
        named("test") {
            java.srcDir(sharedTestsFolder)
        }
        named("androidTest") {
            java.srcDir(sharedTestsFolder)
        }
    }

    signingConfigs {
        register("fastlane") {
            val keystoreProperties = Properties().apply {
                load(file("$rootDir/keystore.properties").inputStream())
            }
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders += mapOf("enableCrashReporting" to false)
        }
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs["fastlane"]
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders += mapOf("enableCrashReporting" to true)
        }
    }

    lint {
        baselineFile = rootProject.file("app/lint-baseline.xml")
        lintConfig = rootProject.file(".lint/config.xml")
        isWarningsAsErrors = true
        isAbortOnError = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {
    implementation(fileTree("libs").include(listOf("*.aar", "*.jar")).exclude("*mock*.jar"))

    // Needed for locating the user -  todo - do it with here positioning
    implementation(Libs.googlePlayServicesLocation)

    coreLibraryDesugaring(Libs.desugar)
    implementation(Libs.appcompat)
    implementation(Libs.annotations)
    implementation(Libs.prefsKtx)
    implementation(Libs.coreKtx)
    implementation(Libs.material)
    implementation(Libs.constraintLayout)
    implementation(Libs.lifecycleViewModelKtx)
    implementation(Libs.lifecycleLivedataKtx)
    implementation(Libs.lifecycleRuntimeKtx)
    implementation(Libs.lifecycleCommonJava8)
    implementation(Libs.lifecycleReactiveStreamsKtx)
    implementation(Libs.rxJava)
    implementation(Libs.rxAndroid)
    implementation(Libs.viewpager2)
    implementation(Libs.fragment)
    debugImplementation(Libs.fragmentTesting)
    implementation(Libs.navigationFragmentKtx)
    implementation(Libs.navigationUiKtx)
    implementation(Libs.viewBindingDelegate)
    implementation(Libs.okhttp)
    implementation(Libs.okhttpLogging)
    implementation(Libs.retrofit)
    implementation(Libs.retrofitGson)
    implementation(Libs.retrofitRxjava3)
    implementation(Libs.retrofitScalars)
    implementation(platform(Libs.firebaseBom))
    implementation(Libs.firebaseAnalytics)
    implementation(Libs.firebaseCrashlytics)
    implementation(Libs.koinCore)
    implementation(Libs.koinAndroid)
    implementation(Libs.roomRuntime)
    kapt(Libs.roomCompiler)
    implementation(Libs.roomKtx)
    implementation(Libs.roomRxjava3)

    // Unit tests
    testImplementation(Libs.junit)
    testImplementation(Libs.mockk)
    testImplementation(Libs.mockkAgentJvm)
    testImplementation(Libs.testCore)

    // Instrumented tests
    androidTestImplementation(Libs.testExtJunit)
    androidTestImplementation(Libs.espressoCore)
    androidTestImplementation(Libs.roomTest)
    // needs the dex opener for version lower than P
    androidTestImplementation(Libs.koinTest)
    androidTestImplementation(Libs.mockkAndroid)
    androidTestImplementation(Libs.mockkAgentJvm)
    androidTestImplementation(Libs.testCore)
}

apply(plugin = "com.google.gms.google-services")
