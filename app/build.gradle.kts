plugins {


    id("kotlin-kapt")  // ← MUST ADD THIS for Room annotation processing
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "com.noor.noorulhuda_products_manager"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.noor.noorulhuda_products_manager"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    android {
        signingConfigs {
            create("release") {  // Use `create()` instead of direct declaration
                storeFile = file("C:/Users/noorl/Desktop/AndroidProjects/NoorulhudaCOMP304402Lab3/my-release-key.jks")
                storePassword = "123456"  // Use `=` for assignment
                keyAlias = "key0"
                keyPassword = "123456"
            }
        }

        buildTypes {
            getByName("release") {
                signingConfig = signingConfigs.getByName("release")  // Assign using `=`
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.runtime.livedata)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


// Room components
    implementation(libs.androidx.room.runtime)  // New
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)  // ← THIS FIXES YOUR ERROR

    // Your existing dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)


    // Other dependencies
    implementation(libs.androidx.activity.compose)
}