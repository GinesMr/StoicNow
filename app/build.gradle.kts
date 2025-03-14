plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.stoic"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.stoic"
        minSdk = 30
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    dependencies {
        // Tus dependencias existentes
        implementation("com.google.android.material:material:1.4.0")
        implementation("androidx.core:core-ktx:1.6.0")
        implementation("androidx.appcompat:appcompat:1.3.1")
        implementation("androidx.constraintlayout:constraintlayout:2.1.0")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
        implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
        implementation("androidx.navigation:navigation-ui-ktx:2.3.5")


        // Dependencias para pruebas
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.3")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    }
}