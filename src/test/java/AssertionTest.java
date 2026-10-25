import org.example.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Tag("assertions")
public class AssertionTest {

    // Тест 1. Булев метод

    @RepeatedTest(10)
    @DisplayName("isEven: чётное число даёт true, нечётное — false")
    public void testIsEvenWithAsserts() {
        Boolean even = ChetNeChet.isEven(10);
        Boolean odd  = ChetNeChet.isEven(7);

        assertTrue(even, "Ожидалось true для чётного числа 10, но получено " + even);
        assertFalse(odd, "Ожидалось false для нечётного числа 7, но получено " + odd);
    }

    // Тест 2. Метод, возвращающий список

    @RepeatedTest(10)
    @DisplayName("removeSpecificName: удаляет имя из списка")
    public void testRemoveSpecificNameWithAsserts() {
        List<String> input = new ArrayList<>(Arrays.asList("Aleksei", "Ivan", "Anastasiya", "Petr"));
        List<String> expected = Arrays.asList("Aleksei", "Anastasiya", "Petr");

        List<String> actual = Names.removeSpecificName(input, "Ivan");

        assertEquals(expected, actual,
                "Ожидался список без 'Ivan', но получено: " + actual);

        assertNotNull(actual, "Список не должен быть null");
        assertEquals(3, actual.size(), "Размер списка должен быть 3, а не " + actual.size());
    }

    // Тест 3. Смешанные ассерты + сообщения

    @RepeatedTest(10)
    @DisplayName("Составной тест: checkAccess, getGrade, sumToN")
    public void testMixedAssertions() {
        // checkAccess
        assertEquals("Allowed", Age.checkAccess(21),
                "Для возраста 21 ожидалось 'Allowed', получено: " + Age.checkAccess(21));
        assertEquals("Denied", Age.checkAccess(18),
                "Для возраста 18 ожидалось 'Denied', получено: " + Age.checkAccess(18));

        // getGrade
        assertEquals("A", Stroka.getGrade(95),
                "Для 95 баллов ожидалось 'A', получено: " + Stroka.getGrade(95));
        assertEquals("E", Stroka.getGrade(10),
                "Для 10 баллов ожидалось 'E', получено: " + Stroka.getGrade(10));

        // sumToN
        assertEquals(15, Summa.sumToN(5),
                "Сумма чисел от 1 до 5 должна быть 15, получено: " + Summa.sumToN(5));
    }

    // Тест 4. FAIL
    @Test
    @DisplayName("FAIL TEST: заведомо неверное ожидание")
    public void testFailingAssertion() {
        int actual = Summa.sumToN(15);

        assertEquals(999, actual,
                "Ожидалось 999, но получено " + actual + " — тест специально падает");
    }
}