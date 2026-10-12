package org.example;

public class Age {
    public static String checkAccess(int age) {
        if (age > 18) {
            return "Allowed";
        } else {
            return "Denied";
        }
    }

    public static void main(String[] args) {
        printAccessResult(10);
        printAccessResult(17);
        printAccessResult(18);
        printAccessResult(21);
        printAccessResult(99);
    }

    public static void printAccessResult(int age) {
        System.out.println("Возраст: " + age + ", доступ: " + checkAccess(age));
    }
}
