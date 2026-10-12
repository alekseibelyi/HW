package org.example;

public class Poexali {
    public static String blassOff(int start) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            result.append(i);

            if (i > 1) {
                result.append(", ");
            } else {
                result.append(" ");
            }
        }
        result.append("Поехали!");
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(blassOff(10));
    }
}
