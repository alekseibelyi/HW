import org.example.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("main")
    public class MainTest {

        private static final Random random = new Random();

    public static Stream<Integer> randomScores() {
        return random.ints(10, 0, 101).boxed();
    }

    @BeforeEach
        public void setUp() {
            System.out.println("===============Test method start");
        }

        @AfterEach
        public void tearDown() {
            System.out.println("Test method end");
            System.out.println("=================");
        }

        //  1. ChetNeChet
        @RepeatedTest(10)
        public void testIsEven() {
            int n = random.nextInt(100) + 1;
            boolean expected = (n % 2 == 0);
            boolean actual = ChetNeChet.isEven(n);
            System.out.println("isEven(" + n + ") = " + actual + ", expected = " + expected);
            assertEquals(expected, actual,
                    "isEven(" + n + "): ожидалось " + expected + ", получено " + actual);
        }

        // 2. Summa
        @RepeatedTest(10)
        public void testSumToN() {
            int n = random.nextInt(50) + 1;
            int expected = 0;
            for (int i = 1; i <= n; i++) expected += i;
            int actual = Summa.sumToN(n);
            System.out.println("sumToN(" + n + ") = " + actual + ", expected = " + expected);
            assertEquals(expected, actual,
                    "sumToN(" + n + "): ожидалось " + expected + ", получено " + actual);
        }


        // 3. RANGE
        // 3.1 start <= end
        @RepeatedTest(10)
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
            assertEquals(expected, actual,
                    "getEvenInRange(" + start + ", " + end + "): ожидалось \""
                            + expected + "\", получено \"" + actual + "\"");
        }

    // 3.2 start == end
    @RepeatedTest(10)
    public void testGetEvenInRangeSameStartEnd() {
        int n = random.nextInt(20) + 1;
        String expected = (n % 2 == 0) ? String.valueOf(n) : "";
        String actual = Range.getEvenInRange(n, n);
        System.out.println("getEvenInRange(" + n + ", " + n + ") = \"" + actual
                + "\", expected = \"" + expected + "\"");
        assertEquals(expected, actual,
                "getEvenInRange(" + n + ", " + n + "): ожидалось \""
                        + expected + "\", получено \"" + actual + "\"");
    }

    // 3.3 start > end
    @ParameterizedTest
    @MethodSource("reversedRanges")
    public void testGetEvenInRangeReversed(int start, int end) {
        String expected = "";
        String actual = Range.getEvenInRange(start, end);
        System.out.println("getEvenInRange(" + start + ", " + end + ") = \"" + actual
                + "\", expected = \"" + expected + "\"");
        assertEquals(expected, actual,
                "getEvenInRange(" + start + ", " + end + ") при start > end: ожидалось \""
                        + expected + "\", получено \"" + actual + "\"");
    }

    static Stream<Arguments> reversedRanges() {
        return Stream.generate(() -> {
            int start = random.nextInt(20) + 10;
            int end = random.nextInt(start - 5) + 1;
            return Arguments.of(start, end);
        }).limit(10);
    }


        // 4. BolsheMensheNull
        @RepeatedTest(10)
        public void testIsPositive() {
            int m = random.nextInt(200) - 100;
            boolean expected = m >= 0;
            boolean actual = BolsheMensheNull.isPositive(m);
            System.out.println("isPositive(" + m + ") = " + actual + ", expected = " + expected);
            assertEquals(expected, actual,
                    "isPositive(" + m + "): ожидалось " + expected + ", получено " + actual);}

        // 5. Age
        @RepeatedTest(10)
        public void testCheckAccess() {
            int age = random.nextInt(100);
            String expected = age > 18 ? "Allowed" : "Denied";
            String actual = Age.checkAccess(age);
            System.out.println("checkAccess(" + age + ") = " + actual + ", expected = " + expected);
            assertEquals(expected, actual,
                    "checkAccess(" + age + "): ожидалось '" + expected
                            + "', получено '" + actual + "'");
        }


        // 6. FindMax
    @RepeatedTest(10)
    public void testFindMax() {
        int size = random.nextInt(10) + 1;
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = random.nextInt(200) - 100;
        int expected = Arrays.stream(arr).max().getAsInt();
        int actual = FindMax.findMax(arr);
        System.out.println("findMax(" + Arrays.toString(arr) + ") = " + actual
                + ", expected = " + expected);
        assertEquals(expected, actual,
                "findMax(" + Arrays.toString(arr) + "): ожидалось " + expected
                        + ", получено " + actual);
    }


        // 7. Bug
        @RepeatedTest(10)
        public void testHasBug() {
            String[] pool = {"Warning", "Error", "org.example.Bug", "Info", "bug", "ERROR", "Debug"};
            int size = random.nextInt(5) + 1;
            String[] messages = new String[size];
            for (int i = 0; i < size; i++) messages[i] = pool[random.nextInt(pool.length)];

            boolean expected = false;
            for (String msg : messages) {
                if ("org.example.Bug".equalsIgnoreCase(msg)) { expected = true; break; }
            }
            boolean actual = Bug.hasBug(messages);
            System.out.println("hasBug(" + Arrays.toString(messages) + ") = " + actual
                    + ", expected = " + expected);
            assertEquals(expected, actual,
                    "hasBug(" + Arrays.toString(messages) + "): ожидалось " + expected
                            + ", получено " + actual);
        }


        // 8. Stroka
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
            assertEquals(expected, actual,
                    "getGrade(" + score + "): ожидалось '" + expected
                            + "', получено '" + actual + "'");
        }

        // 9. Poexali
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
            assertEquals(expected, actual,
                    "blassOff(" + start + "): ожидалось \"" + expected
                            + "\", получено \"" + actual + "\"");
        }

    static Stream<Integer> randomStarts() {
        return Stream.generate(() -> random.nextInt(10) + 1).limit(10);
    }
    // 10. Average

    @ParameterizedTest
    @MethodSource("randomIntLists")
    public void testCalcAverage(List<Integer> list) {
        int sum = 0;
        for (int x : list) sum += x;
        double expected = (double) sum / list.size();
        double actual = Average.calcAverage(list);
        System.out.println("calcAverage(" + list + ") = " + actual
                + ", expected = " + expected);
        assertEquals(expected, actual, 1e-9,
                "calcAverage(" + list + "): ожидалось " + expected
                        + ", получено " + actual);
    }
    static Stream<List<Integer>> randomIntLists() {
        return Stream.generate(() -> {
            int size = random.nextInt(8) + 1;
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) list.add(random.nextInt(100));
            return list;
        }).limit(10);
    }

        // 11. Names
        @ParameterizedTest
        @MethodSource("randomNames")
        public void testRemoveSpecificName(List<String> input, String nameToRemove) {
            List<String> expected = new ArrayList<>();
            for (String name : input) {
                if (!name.equals(nameToRemove)) expected.add(name);
            }
            List<String> actual = Names.removeSpecificName(input, nameToRemove);
            System.out.println("removeSpecificName(" + input + ", \"" + nameToRemove
                    + "\") = " + actual);
            assertEquals(expected, actual,
                    "removeSpecificName: ожидалось " + expected + ", получено " + actual);
        }

    static Stream<Arguments> randomNames() {
        String[] pool = {"Aleksei", "Ivan", "Anastasiya", "Petr", "Anna", "Oleg"};
        return Stream.generate(() -> {
            int size = random.nextInt(5) + 2;
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) list.add(pool[random.nextInt(pool.length)]);
            String toRemove = list.get(random.nextInt(list.size()));
            return Arguments.of(list, toRemove);
        }).limit(10);
    }

        // 12. Reverse
        @ParameterizedTest
        @MethodSource("randomStringArrays")
        public void testReverse(String[] input) {
            String[] expected = new String[input.length];
            for (int i = 0; i < input.length; i++) {
                expected[i] = input[input.length - 1 - i];
            }
            String[] actual = Main.reverse(input);
            System.out.println("reverse(" + Arrays.toString(input) + ") = "
                    + Arrays.toString(actual) + ", expected = " + Arrays.toString(expected));
            assertArrayEquals(expected, actual,
                    "reverse(" + Arrays.toString(input) + "): ожидалось "
                            + Arrays.toString(expected) + ", получено "
                            + Arrays.toString(actual));
        }
    static Stream<Arguments> randomStringArrays() {
        String[] pool = {"a", "b", "c", "d", "e", "f"};
        return Stream.generate(() -> {
            int size = random.nextInt(6) + 1;
            String[] arr = new String[size];
            for (int i = 0; i < size; i++) arr[i] = pool[random.nextInt(pool.length)];
            return Arguments.of((Object) arr);
        }).limit(10);
    }
}