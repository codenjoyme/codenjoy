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

/**
 * Все то, что ты захардкодил бы в константы в своей игре ты можешь заврапить в параметр с помощью этого чуда.
 * Для каждой константы (отличаются они по имени) будет создан враппер, который ты сможешь
 * проинитить значением по-умолчанию и в дальнейшем пользоваться в коде игры, заместь
 * непосредственного захардкодженного значения. Магия в том, что они отображатся на адмике после выбора твоей игры.
 *
 * @see Parameter
 */
public interface Settings {

    List<Parameter> getParameters();

    // TODO а тут точно надо <?> ?
    EditBox<?> addEditBox(String name);

    SelectBox<?> addSelect(String name, List<Object> strings);

    CheckBox<Boolean> addCheckBox(String name);

    // TODO а тут точно надо <T> ?
    <T> Parameter<T> getParameter(String name);

    boolean hasParameter(String name);

    void removeParameter(String name);

    /**
     * @return true - если были изменения настроек
     */
    boolean changed();

    /**
     * @return Список имен параметров, которые поменялись
     */
    List<String> whatChanged();

    /**
     * Так ты сообщаешь что отреагировал на все изменения.
     */
    void changesReacted();

    void clear();

    void updateAll(List<Parameter> parameters);
}
