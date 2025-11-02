plugins {
    id("java")
    id("io.qameta.allure") version "3.0.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.rest-assured:rest-assured:5.5.6")
    
    // Allure dependencies
    testImplementation("io.qameta.allure:allure-junit5:2.25.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.25.0")
}

tasks.test {
    useJUnitPlatform()
}

allure {
    version.set("2.25.0")
    adapter {
        frameworks {
            junit5 {
                adapterVersion.set("2.25.0")
            }
        }
    }
}