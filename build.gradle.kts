plugins {
    id("java")
    id("io.qameta.allure") version "2.11.2" apply false // <-- Добавить эту строку
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.qameta.allure")

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("io.qameta.allure:allure-junit5:2.24.0")

        // ВАЖНО: Обновите AspectJ
        testImplementation("org.aspectj:aspectjweaver:1.9.20")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("allure.results.directory", "$buildDir/allure-results")
    }

    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        testImplementation("io.qameta.allure:allure-junit5:2.24.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        systemProperty("allure.results.directory", "$buildDir/allure-results")
    }
}

// Задача для генерации отчета во ВСЕХ модулях
tasks.register("allureReportAll") {
    dependsOn(":api-tests:allureReport", ":ui-tests:allureReport")
}