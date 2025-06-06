import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
    signing
}

android {
    namespace = "com.plugin.annotation"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.flowfan"
            artifactId = "hooker-annotation"
            version = "1.0.0"

            afterEvaluate {
                from(components["release"])
            }
            pom {
                name.set("HookerAnnotation")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("fan1138612367")
                        name.set("FlowFan")
                        email.set("fan1138612367@vip.qq.com")
                    }
                }
                scm {
                    connection.set("https://github.com/FlowFan/Hooker.git")
                    developerConnection.set("https://github.com/FlowFan/Hooker.git")
                }
            }
        }
    }
    repositories {
        maven {
            url = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
            credentials {
                username = properties["ossrhUsername"] as String
                password = properties["ossrhPassword"] as String
            }
        }
    }
}

signing {
    sign(publishing.publications["release"])
}

dependencies {

    implementation(libs.asm)
}