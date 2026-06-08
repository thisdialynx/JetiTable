import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    kotlin("plugin.serialization")
}

android {
    namespace = "lnx.jetitable"
    compileSdk = 37

    defaultConfig {
        applicationId = "lnx.jetitable"
        minSdk = 24
        targetSdk = 37
        versionCode = 33
        versionName = "0.3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties()

        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        val apiBaseUrl = localProperties.getProperty("API_BASE_URL") ?: ""
        val apiAuthorisationEndpoint =
            localProperties.getProperty("API_AUTHORISATION_ENDPOINT") ?: ""
        val apiQueryEndpoint = localProperties.getProperty("API_QUERY_ENDPOINT") ?: ""
        val apiCheckPassword = localProperties.getProperty("API_CHECK_PASSWORD") ?: ""
        val apiPasswordRecovery = localProperties.getProperty("API_PASSWORD_RECOVERY") ?: ""
        val apiCheckAccess = localProperties.getProperty("API_CHECK_ACCESS") ?: ""
        val apiDailyClassList = localProperties.getProperty("API_DAILY_CLASS_LIST") ?: ""
        val apiPresenceVerification = localProperties.getProperty("API_PRESENCE_VERIFICATION") ?: ""
        val apiExamList = localProperties.getProperty("API_EXAM_LIST") ?: ""
        val apiAttendanceList = localProperties.getProperty("API_ATTENDANCE_LIST") ?: ""
        val apiState = localProperties.getProperty("API_STATE") ?: ""

        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "API_AUTHORISATION_ENDPOINT", "\"$apiAuthorisationEndpoint\"")
        buildConfigField("String", "API_QUERY_ENDPOINT", "\"$apiQueryEndpoint\"")
        buildConfigField("String", "API_CHECK_PASSWORD", "\"$apiCheckPassword\"")
        buildConfigField("String", "API_PASSWORD_RECOVERY", "\"$apiPasswordRecovery\"")
        buildConfigField("String", "API_CHECK_ACCESS", "\"$apiCheckAccess\"")
        buildConfigField("String", "API_DAILY_CLASS_LIST", "\"$apiDailyClassList\"")
        buildConfigField("String", "API_PRESENCE_VERIFICATION", "\"$apiPresenceVerification\"")
        buildConfigField("String", "API_EXAM_LIST", "\"$apiExamList\"")
        buildConfigField("String", "API_ATTENDANCE_LIST", "\"$apiAttendanceList\"")
        buildConfigField("String", "API_STATE", "\"$apiState\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    androidResources {
        generateLocaleConfig = true
    }
}

dependencies {
    implementation(libs.timber)
    implementation(libs.androidx.startup)
    implementation(libs.hilt.library)
    implementation(libs.accompanist.permissions)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil3.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.jsoup.jsoup)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.hilt.lifecycle.viewmodel)
    implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    ksp(libs.hilt.compiler)
    ksp(libs.kotlin.metadata.jvm)
    ksp(libs.androidx.hilt.compiler)
}