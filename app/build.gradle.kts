plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.financetrackerapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.financetrackerapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // segmented button
    implementation("com.github.addisonelliott:SegmentedButton:3.1.9")

    // room
    implementation("androidx.room:room-runtime:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    implementation("androidx.room:room-paging:2.7.2")

    // splash
    implementation("androidx.core:core-splashscreen:1.0.0")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")

    // chart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // expanable recycler view
    implementation("com.github.kamrul3288:Expandable-Recyclerview-List-Android:1.0.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0")

    // camera x
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:1.5.1")
    implementation("androidx.camera:camera-camera2:1.5.1")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:1.5.1")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:1.5.1")

    testImplementation("androidx.room:room-testing:2.7.2")
    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

}