package org.example;

public class BolsheMensheNull {
    public static boolean isPositive(int m) {
        return m >= 0 ? true : false;
    }

    public static void main(String[] args) {
        int number = -4;

        System.out.println("Число: " + number + " " + isPositive(number));
    }
}
