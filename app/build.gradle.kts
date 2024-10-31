plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.hotel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hotel"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation (libs.glide.v4120)
    implementation(platform(libs.firebase.bom))
        implementation(platform(libs.firebase.bom.v3223))
        implementation(libs.google.firebase.auth.ktx) // Firebase Auth with KTX
        implementation(libs.firebase.database.ktx) // Firebase Realtime Database with KTX
        implementation(libs.play.services.auth)
        implementation(libs.glide)
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.appcompat)
        implementation(libs.material)
        implementation(libs.androidx.activity)
        implementation(libs.androidx.constraintlayout)
        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)
        implementation(libs.androidx.room.ktx)
        implementation(libs.firebase.storage)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.crashlytics.buildtools) // Firebase Storage
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation(libs.imageslideshow)

}
