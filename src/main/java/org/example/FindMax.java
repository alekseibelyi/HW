package org.example;

public class FindMax {
    public static int findMax(int[] arr) {
        int max = arr[0];

        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] numbers = {0, 5, 23, -24, 4, 99};
        System.out.println(findMax(numbers));
    }
}
