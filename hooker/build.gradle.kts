plugins {
    `kotlin-dsl`
    alias(libs.plugins.plugin.publish)
    signing
}

gradlePlugin {
    plugins {
        create("hooker") {
            id = "com.plugin.hooker"
            implementationClass = "com.plugin.hooker.HookerPlugin"
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            groupId = "com.plugin.hooker"
            artifactId = "com.plugin.hooker.gradle.plugin"
            version = "1.0.0"

            afterEvaluate {
                from(components["java"])
            }
            pom {
                name.set("HookerPlugin")
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
    sign(publishing.publications["maven"])
}

dependencies {

    implementation(libs.android.tools.build.gradle)
    implementation(libs.asm.commons)
}