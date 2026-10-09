import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private static final Random random = new Random();

    @BeforeEach
    public void setUp() {
        System.out.println("===============Test method start");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Test method end");
        System.out.println("=================");
    }

    private static void check(boolean ok) {
        if (ok) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //  1. @Test ChetNeChet
    @Test
    public void testIsEven() {
        int n = random.nextInt(100) + 1; // 1..100
        boolean expected = (n % 2 == 0);
        boolean actual = ChetNeChet.isEven(n);
        System.out.println("isEven(" + n + ") = " + actual + ", expected = " + expected);
        check(expected == actual);
        assertEquals(expected, actual);
    }

    // 2. @Test — Summa
    @Test
    public void testSumToN() {
        int n = random.nextInt(50) + 1;
        int expected = 0;
        for (int i = 1; i <= n; i++) expected += i;
        int actual = Summa.sumToN(n);
        System.out.println("sumToN(" + n + ") = " + actual + ", expected = " + expected);
        check(expected == actual);
        assertEquals(expected, actual);
    }


    // 3. @Test — Range
    @Test
    public void testGetEvenInRange() {
        int start = random.nextInt(20) + 1;
        int end = start + random.nextInt(20) + 1;
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(i);
            }
        }
        String expected = sb.toString();
        String actual = Range.getEvenInRange(start, end);
        System.out.println("getEvenInRange(" + start + ", " + end + ") = \"" + actual
                + "\", expected = \"" + expected + "\"");
        check(expected.equals(actual));
        assertEquals(expected, actual);
    }

    // 4. @Test — BolsheMensheNull
    @Test
    public void testIsPositive() {
        int m = random.nextInt(200) - 100; // -100..99
        boolean expected = m >= 0;
        boolean actual = BolsheMensheNull.isPositive(m);
        System.out.println("isPositive(" + m + ") = " + actual + ", expected = " + expected);
        check(expected == actual);
        assertEquals(expected, actual);
    }

    // 5. @RepeatedTest(20) — Age
    @RepeatedTest(20)
    public void testCheckAccess() {
        int age = random.nextInt(100); // 0..99
        String expected = age > 18 ? "Allowed" : "Denied";
        String actual = Age.checkAccess(age);
        System.out.println("checkAccess(" + age + ") = " + actual + ", expected = " + expected);
        check(expected.equals(actual));
        assertEquals(expected, actual);
    }


    // 6. @RepeatedTest(15) — FindMax

    @RepeatedTest(15)
    public void testFindMax() {
        int size = random.nextInt(10) + 1;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = random.nextInt(200) - 100;
        int expected = Arrays.stream(arr).max().getAsInt();
        int actual = FindMax.findMax(arr);
        System.out.println("findMax(" + Arrays.toString(arr) + ") = " + actual + ", expected = " + expected);
        check(expected == actual);
        assertEquals(expected, actual);
    }


    // 7. @RepeatedTest(10) — Bug
    @RepeatedTest(10)
    public void testHasBug() {
        String[] pool = {"Warning", "Error", "Bug", "Info", "bug", "ERROR", "Debug"};
        int size = random.nextInt(5) + 1;
        String[] messages = new String[size];
        for (int i = 0; i < size; i++) messages[i] = pool[random.nextInt(pool.length)];

        boolean expected = false;
        for (String msg : messages)
            if ("Bug".equalsIgnoreCase(msg)) {
                expected = true;
                break;
            }

        boolean actual = Bug.hasBug(messages);
        System.out.println("hasBug(" + Arrays.toString(messages) + ") = " + actual + ", expected = " + expected);
        check(expected == actual);
        assertEquals(expected, actual);
    }


    // 8. @ParameterizedTest — Stroka
    @ParameterizedTest
    @MethodSource("randomScores")
    public void testGetGrade(int score) {
        String expected;
        if (score >= 0 && score <= 20) expected = "E";
        else if (score <= 40) expected = "D";
        else if (score <= 60) expected = "C";
        else if (score <= 80) expected = "B";
        else if (score <= 100) expected = "A";
        else expected = "Error";

        String actual = Stroka.getGrade(score);
        System.out.println("getGrade(" + score + ") = " + actual + ", expected = " + expected);
        check(expected.equals(actual));
        assertEquals(expected, actual);
    }

    static Stream<Integer> randomScores() {
        return random.ints(10, 0, 101).boxed();
    }

    // 9. @ParameterizedTest — Poexali
    @ParameterizedTest
    @MethodSource("randomStarts")
    public void testBlassOff(int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            sb.append(i);
            sb.append(i > 1 ? ", " : " ");
        }
        sb.append("Поехали!");
        String expected = sb.toString();
        String actual = Poexali.blassOff(start);
        System.out.println("blassOff(" + start + ") = \"" + actual + "\"");
        check(expected.equals(actual));
        assertEquals(expected, actual);
    }

    static Stream<Integer> randomStarts() {
        return Stream.generate(() -> random.nextInt(10) + 1).limit(5);
    }

    // 10. @ParameterizedTest — Average

    @ParameterizedTest
    @MethodSource("randomIntLists")
    public void testCalcAverage(List<Integer> list) {
        int sum = 0;
        for (int x : list) sum += x;
        double expected = (double) sum / list.size();
        double actual = Average.calcAverage(list);
        System.out.println("calcAverage(" + list + ") = " + actual + ", expected = " + expected);
        check(Math.abs(expected - actual) < 1e-9);
        assertEquals(expected, actual, 1e-9);
    }

    static Stream<List<Integer>> randomIntLists() {
        return Stream.generate(() -> {
            int size = random.nextInt(8) + 1;
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) list.add(random.nextInt(100));
            return list;
        }).limit(5);
    }

    // 11. @ParameterizedTest — Names
    @ParameterizedTest                          // ← было @Test
    @MethodSource("randomNames")
    public void testRemoveSpecificName(List<String> input, String nameToRemove) {
        List<String> expected = new ArrayList<>();
        for (String name : input) {
            if (!name.equals(nameToRemove)) expected.add(name);
        }

        List<String> actual = Names.removeSpecificName(input, nameToRemove);
        System.out.println("removeSpecificName(" + input + ", \"" + nameToRemove + "\") = " + actual);
        check(expected.equals(actual));
        assertEquals(expected, actual);
    }

    static Stream<Arguments> randomNames() {    // ← Stream<Arguments>, не Stream<String[]>
        String[] pool = {"Aleksei", "Ivan", "Anastasiya", "Petr", "Anna", "Oleg"};
        return Stream.generate(() -> {
            int size = random.nextInt(5) + 2;   // минимум 2 имени
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(pool[random.nextInt(pool.length)]);
            }
            String toRemove = list.get(random.nextInt(list.size()));
            return Arguments.of(list, toRemove);  // ← передаём ДВА аргумента
        }).limit(5);

    }
}