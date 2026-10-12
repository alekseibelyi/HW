package org.example;

public class Bug {
    public static Boolean hasBug(String[] messages) {
        for (String message : messages) {
            if ("org.example.Bug".equalsIgnoreCase(message)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] messages = {"Warning", "Error", "org.example.Bug"};
        System.out.println((hasBug(messages)));
    }
}
