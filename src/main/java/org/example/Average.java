package org.example;

import java.util.Arrays;
import java.util.List;

public class Average {
    public static double calcAverage(List<Integer> list) {
        int sum = 0;

        for (int number : list) {
            sum += number;
        }
        return (double) sum / list.size();
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(10, 20, 30);
        System.out.println(calcAverage(numbers));

    }
}
