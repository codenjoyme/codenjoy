package com.codenjoy.dojo.kata.model.levels.algorithms;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.kata.model.levels.AlgorithmLevelImpl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HelloWorldAlgorithm extends AlgorithmLevelImpl {

    public static final String HELLO = "hello";
    public static final String WORLD = "world";

    @Override
    public String get(String question) {
        if (question.equals(HELLO)) {
            return WORLD;
        } 
        
        if (question.equals(WORLD)) {
            return HELLO;
        } 
        
        return question;
    }

    @Override
    public String description() {
        return "Напиши метод, принимающий один String аргумент " +
                "и возвращающий строку 'world' если на вход пришло 'hello'," +
                "и 'hello' - если пришло 'world', в противном случае алгоритм " +
                "должен вернуть ту же строчку, что пришла на вход. \n" +
                "Алгоритм реализован для проверки конневшена клиента к серверу.";
    }

    @Override
    public List<String> getQuestions() {
        return Arrays.asList(
                HELLO,
                WORLD,
                "qwe",
                "asd",
                "zxc");
    }

    @Override
    public int complexity() {
        return 0;
    }

    @Override
    public String author() {
        return "Александр Баглай (apofig@gmail.com)";
    }
}
