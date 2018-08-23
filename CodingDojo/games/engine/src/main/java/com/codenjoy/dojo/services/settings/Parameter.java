package com.codenjoy.dojo.services.settings;

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
import java.util.function.Function;

/**
 * Изменяемый параметр. Фишка его в том, чтобы на админке можно было в рантайме менять параметры игры,
 * которые иначе тебе пришлось бы тебе захардкодить в твоей игре.
 *
 * @see Settings
 */
public interface Parameter<T> {

    /**
     * @return Значение параметра
     */
    T getValue();

    /**
     * @return Тип параметра
     */
    String getType();

    String getName();

    void update(T value);

    /**
     * Так ты указываешь значение по умолчанию. Обычно этого достаточно для ввода значения.
     *
     * @param value значение
     * @return возвращается this
     */
    Parameter<T> def(T value);

    boolean itsMe(String name);

    <V> Parameter<V> type(Class<V> integerClass);

    Parameter<T> parser(Function<String, T> parser);

    void select(int index);

    boolean changed();

    void changesReacted();

    List<T> getOptions();
}
