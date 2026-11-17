plugins {
    id("java")
}

dependencies {
    implementation(project(":common"))
    testImplementation("io.rest-assured:rest-assured:5.4.0")
    testImplementation("io.qameta.allure:allure-rest-assured:2.24.0")
    testImplementation("org.assertj:assertj-core:3.25.3")

    // Явно добавляем JUnit 5 (если его нет в корневом build.gradle.kts)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.qameta.allure:allure-junit5:2.24.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.aspectj:aspectjweaver:1.9.20")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("allure.results.directory", "$buildDir/allure-results")
}

// Задача для запуска ТОЛЬКО Smoke-тестов
tasks.register<Test>("apiSmoke") {
    description = "Запускает только API Smoke-тесты"
    group = "verification"
    useJUnitPlatform {
        includeTags("smoke")
    }
    systemProperty("allure.results.directory", "$buildDir/allure-results")
}