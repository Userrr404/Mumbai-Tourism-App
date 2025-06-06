plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.tourismapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tourismapp"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    implementation(libs.tools.core)
    implementation(libs.games.activity)
    implementation(libs.play.services.maps)
    implementation (libs.play.services.maps.v1820)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // define a BOM and its version
    implementation(platform(libs.okhttp.bom))

    // define any required OkHttp artifacts without version
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)

    implementation(libs.volley)


    implementation (libs.glide)
//    Failed to find GeneratedAppGlideModule. You should include an annotationProcessor compile dependency... for this next line add

    annotationProcessor(libs.compiler) // Add this

    implementation (libs.material.v1110)

    implementation (libs.volley)
    implementation (libs.circleimageview)
}