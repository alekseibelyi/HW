package org.example;
import java.util.Arrays;


public class Main {
    // 1. Метод проверки четности
    public static Boolean isEven(int n) {
        return n % 2 == 0;
    }

    // 2. Метод проверки доступа по возрасту
    public static String checkAccess(int age) {
        if (age >= 18) {
            return "Access granted";
        } else {
            return "Access denied";
        }
    }

    // 3. Метод получения оценки Gradle
    public static String getGradle(int score) {
        if (score >= 90) {
            return "A";
        } else if (score >= 80) {
            return "B";
        } else if (score >= 70) {
            return "C";
        } else if (score >= 60) {
            return "D";
        } else {
            return "F";
        }
    }

}