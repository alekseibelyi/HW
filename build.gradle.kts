plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Только кодировка — блок закрывается сразу
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// Настройки тестов — на верхнем уровне
tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}

// Задание 1: запуск всех тестов
tasks.register<DefaultTask>("runAllTests") {
    dependsOn(tasks.test)
    group = "verification"
    description = "Запускает все тесты в проекте"
}

// Задание 2: сообщение после прогона тестов
tasks.register<DefaultTask>("printTestEnd") {
    dependsOn(tasks.named("runAllTests"))
    group = "verification"
    description = "Выводит сообщение после прогона тестов"
    doLast {
        println("Test run is over")
    }
}