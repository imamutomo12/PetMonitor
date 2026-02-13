plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)    // Add this back
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp") // Add KSP instead
}

android {
    namespace = "edu.imamutomo.petmonitor"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "edu.imamutomo.petmonitor"
        minSdk = 26
        targetSdk = 36
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


    kotlin {
        jvmToolchain(17)
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.compose.material:material-icons-extended")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.10.0")

    implementation("androidx.activity:activity-ktx:1.12.3")
    implementation("androidx.fragment:fragment-ktx:1.8.9")

    implementation("androidx.navigation:navigation-compose:2.9.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.3.0")

    // Debug Compose
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    val roomVersion = "2.8.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    // Optional: Room Paging integration
    implementation("androidx.room:room-paging:$roomVersion")

    implementation("com.google.code.gson:gson:2.10.1")


    implementation("com.google.dagger:hilt-android:2.59.1")
    ksp("com.google.dagger:hilt-compiler:2.59.1")
    // Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.59.1")
    kspAndroidTest("com.google.dagger:hilt-compiler:2.59.1")
    testImplementation("com.google.dagger:hilt-android-testing:2.59.1")
    kspTest("com.google.dagger:hilt-compiler:2.59.1")

    implementation("androidx.datastore:datastore-preferences:1.2.0")
    implementation("androidx.datastore:datastore-preferences-core:1.2.0")

    implementation("androidx.work:work-runtime-ktx:2.11.1")
    implementation("androidx.hilt:hilt-work:1.3.0")
    ksp("androidx.hilt:hilt-compiler:1.3.0")

    implementation("androidx.core:core-ktx:1.17.0") // NotificationCompat included

    implementation("org.json:json:20251224")
    // Alternative: Moshi or Gson if preferred
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-svg:2.7.0")

    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // =========================================================================
    // DATE/TIME HANDLING
    // =========================================================================
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

    // =========================================================================
    // LOGGING
    // =========================================================================
    implementation("com.jakewharton.timber:timber:5.0.1")

    // =========================================================================
    // TESTING
    // =========================================================================

    testImplementation("org.mockito:mockito-core:5.8.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.2.3")
    testImplementation("io.mockk:mockk:1.14.9")
    testImplementation("com.google.truth:truth:1.4.5")
    testImplementation("app.cash.turbine:turbine:1.2.1") // Flow testing



    // Room testing
    testImplementation("androidx.room:room-testing:$roomVersion")

    // Coroutines testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}