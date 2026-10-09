plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.testng:testng:7.1.0")
    implementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}
// Задание 1: Задача запускает все тесты в проекте
tasks.register("runAllTests") {
    dependsOn(tasks.test)
    group = "verification"
    description = "Запускает все тесты в проекте"
}

// Задание 2: После прогона тестов пишет в консоль "Test runbis over"
// и запускается после завершения первой задачи
tasks.register("printTestEnd") {
    dependsOn(tasks.named("runAllTests"))
    group = "verification"
    description = "Выводит сообщение после прогона тестов"
    doLast {
        println("Test runbis over")
    }
}
