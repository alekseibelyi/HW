package org.example;

public class Range {
    public static String getEvenInRange(int start, int end) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(i);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(getEvenInRange(2, 5));
        System.out.println(getEvenInRange(1, 10));

    }
}
