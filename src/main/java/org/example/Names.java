package org.example;

import java.util.ArrayList;
import java.util.List;

public class Names {
    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>();

        for (String name : list) {
            if (!name.equals(nameToRemove)) {
                result.add(name);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> names = List.of("Aleksei", "Ivan", "Anastasiya", "Petr");
        List<String> result = removeSpecificName(names, "Ivan");

        System.out.println(result);
    }
}
