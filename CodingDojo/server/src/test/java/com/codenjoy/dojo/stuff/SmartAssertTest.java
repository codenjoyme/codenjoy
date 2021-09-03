package com.codenjoy.dojo.stuff;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
