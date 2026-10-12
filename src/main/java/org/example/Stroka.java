package org.example;

public class Stroka {
    public static String getGrade(int score) {
        if (score >= 0 && score <= 20) {
            return "E";
        } else if (score >= 21 && score <= 40) {
            return "D";
        } else if (score >= 41 && score <= 60) {
            return "C";
        } else if (score >= 61 && score <= 80) {
            return "B";
        } else if (score >= 81 && score <= 100) {
            return "A";
        } else {
            return "Error";
        }
    }

    public static void main(String[] args) {
        System.out.println("0: " + getGrade(0));
        System.out.println("20: " + getGrade(20));
        System.out.println("21: " + getGrade(21));
        System.out.println("60: " + getGrade(60));
        System.out.println("80: " + getGrade(80));
        System.out.println("100: " + getGrade(100));
        System.out.println("101: " + getGrade(101));
        System.out.println("-1: " + getGrade(-1));

    }
}
