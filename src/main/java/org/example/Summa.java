package org.example;

public class Summa {
    public static int sumToN(int n) {
        int sum = 0;

        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(sumToN(5));
        System.out.println(sumToN(15));
    }
}
