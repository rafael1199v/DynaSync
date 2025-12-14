import org.gradle.kotlin.dsl.implementation
import java.util.Properties as javaProperties
import java.io.FileInputStream as FileIO

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}


android {
    namespace = "com.example.dynasync"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.dynasync"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = javaProperties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(FileIO(localPropertiesFile))
        }

        buildConfigField("String", "SUPABASE_URL", "\"${properties.getProperty("supabaseUrl")}\"")
        buildConfigField("String", "SUPABASE_KEY", "\"${properties.getProperty("supabaseApiKey")}\"")
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
        freeCompilerArgs += listOf(
            "-opt-in=kotlinx.datetime.ExperimentalKotlinxDatetimeApi",
            "-opt-in=kotlin.time.ExperimentalTime"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    val supabaseBOMVersion = "3.2.6"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
    //implementation("com.squareup.retrofit2:retrofit:3.0.0")
    //implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation(platform("io.github.jan-tennert.supabase:bom:${supabaseBOMVersion}"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")
    implementation("io.github.jan-tennert.supabase:storage-kt")

    implementation("io.ktor:ktor-client-okhttp:3.2.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.2.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.3")
    // implementation("androidx.compose.material:material-icons-extended:1.7.8")
    // implementation("com.google.accompanist:accompanist-permissions:0.37.3")
}