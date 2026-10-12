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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}

// Задача 1: запуск всех тестов
tasks.register<DefaultTask>("runAllTests") {
    dependsOn(tasks.test)
    group = "verification"
    description = "Запускает все тесты в проекте"
}

// Задача 2: сообщение после прогона тестов
tasks.register<DefaultTask>("printTestEnd") {
    dependsOn(tasks.named("runAllTests"))
    group = "verification"
    description = "Выводит сообщение после прогона тестов"
    doLast {
        println("Test run is over")
    }
}

// Фильтрация по @Tag — только тесты с @Tag("main")
tasks.register<Test>("runMainTests") {
    description = "Запускает только тесты с тегом 'main'"
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform {
        includeTags("main")
    }
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}

// Фильтрация по @Tag — только тесты с @Tag("assertions")
tasks.register<Test>("runAssertionsTests") {
    description = "Запускает только тесты с тегом 'assertions'"
    group = "verification"
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    useJUnitPlatform {
        includeTags("assertions")
    }
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}