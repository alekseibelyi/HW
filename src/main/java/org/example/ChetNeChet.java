package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChetNeChet {
   public static Boolean isEven(int n) {
        return n % 2 ==0;
    }
public  static void printResult(int n) {
    if (isEven(n)){
        System.out.println(n+ " - Чётное число");
    } else {
        System.out.println(n + " - Нечётное число");
    }
}
 public static void main(String[] args) {
        printResult(2);
        printResult(3);
        printResult(5);
        printResult(10);
        printResult(999);
 }
}


