package com.codenjoy.dojo.stuff;

import org.junit.After;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.joining;

/**
 * Это не автоматический юнит тест, а демонстрационный стенд.
 * Поменяй enable = true и увидишь как отрабатывают assertEquals
 * в консоли IDE.
 */
public class SmartAssertTest {

    /**
     * Если поставить тут true, можно проверить как каждый ассерт
     * каждого теста слетает.
     */
    private boolean enable = false;

    @After
    public void after() throws Exception {
        SmartAssert.checkResult();
    }

    /**
     * Просто какой-то тест с какими-то ассертами
     */
    @Test
    public void test1() {
        assert1();

        assert2();

        assert3("z\n3");
    }

    /**
     * Просто какой-то другой тест с какими-то ассертами
     */
    @Test
    public void test2() {
        assert3("z\n3");

        assert2();

        assert1();
    }

    private void assert3(String expected) {
        String actual = change(expected);

        // обрати внимание, тут SmartAssert.assertEquals
        // а не классический Assert.assertEquals
        // метод импортирован статически, а потому тебе
        // надо поменять только импорт в твоих тестах
        assertEquals(expected, actual);
    }

    /**
     * Благодаря этому методу можно изменить поведение всех ассертов
     */
    private String change(String expected) {
        if (enable) {
            return Arrays.stream(expected.split("\n"))
                    .sorted()
                    .collect(joining("\n"));
        } else {
            return expected;
        }
    }

    // дальше идет симуляция тестовых методов для создания
    // массовки в stack trace во время вывода в консоль
    // слетевшего теста. Это сделано для отладки SmartAssert,
    // в котором возможны косяки связанные с разной дистанцией
    // от слетевшего assert до теста, и их можно увидеть только
    // если структура теста не примитивная.

    private void assert2() {
        assert3("z\n2");
    }

    private void assert1() {
        method();
    }

    private void method() {
        assert3("z\n1");
    }
}
