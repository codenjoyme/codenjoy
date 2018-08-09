package com.codenjoy.dojo.kata.model.levels;

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


import java.util.List;

public interface Level {

    List<String> getQuestions();

    List<String> getAnswers();

    int size();

    /**
     * @return Время идеального (самого быстрого, но правильного)
     * решения задачи в минутах. На основе него и времени затрачиваемого
     * участником на решение алгоритма будет подсчитываться очки.
     * Формула Score = A*(((C/100-1)*Time + Complexity*(B-C/100))/(B-1))
     * где:
     * A - константа, для придания округлости очкам -
     *     чтобы очки были без дробных частей, по умолчанию 100
     * C - количество процентов ниже которых опускаться не будем
     *     обычно 30% за самое худшее по времени решение
     * B - во сколько раз по времени самое худшее решение отличается
     *     от идеального (Complexity), по умолчанию 3x
     * Кроме того, за каждый пройденный тест, дается "подбодряшка" в размере
     * Score2 = A * (D/(100*TestCount))
     * где:
     * A - все то же, что и в прошлый раз
     * D - бюджет на ве тесты, порядка 10%
     */
    int complexity();

    String description();
}
