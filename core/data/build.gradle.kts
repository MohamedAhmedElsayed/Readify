plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.hilt)
  alias(libs.plugins.ksp)
}

android {
  namespace = "com.innovation.core.data"
  compileSdk = 36

  defaultConfig {
    minSdk = 24

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
    buildConfigField(
      "String",
      "API_KEY",
      "\"${project.findProperty("API_KEY") ?: ""}\""
    )
    buildConfigField(
      "String",
      "BASE_URL",
      "\"https://newsapi.org/v2/\""
    )
  }
  buildFeatures {
    buildConfig = true
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)

  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)

// Kotlin Serialization
  api(libs.kotlinx.serialization.json)

// Retrofit
  api(libs.retrofit)
  implementation(libs.retrofit.kotlinx.serialization)

// OkHttp
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}