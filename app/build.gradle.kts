import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val ecoPlantLocalSettings = Properties().apply {
    val settingsFile = rootProject.file("local.properties")
    if (settingsFile.exists()) settingsFile.inputStream().use { stream -> load(stream) }
}
fun quoteBuildConfig(value: String) = "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""

android {
    namespace = "com.khaulahqurrota.ecoplant"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.khaulahqurrota.ecoplant"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "API_BASE_URL", quoteBuildConfig(ecoPlantLocalSettings.getProperty("ecoplant.api.baseUrl", "http://10.210.34.155:3000/api/")))
        buildConfigField("String", "API_TOKEN", quoteBuildConfig(ecoPlantLocalSettings.getProperty("ecoplant.api.token", "")))
        buildConfigField("String", "ADMIN_USERNAME", quoteBuildConfig(ecoPlantLocalSettings.getProperty("ecoplant.admin.username", "")))
        buildConfigField("String", "ADMIN_PASSWORD", quoteBuildConfig(ecoPlantLocalSettings.getProperty("ecoplant.admin.password", "")))
        buildConfigField("String", "OFFICER_USERNAME", quoteBuildConfig(ecoPlantLocalSettings.getProperty("ecoplant.officer.username", "")))
        buildConfigField("String", "OFFICER_PASSWORD", quoteBuildConfig(ecoPlantLocalSettings.getProperty("ecoplant.officer.password", "")))

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.play.services.code.scanner)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

