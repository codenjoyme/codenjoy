package com.codenjoy.dojo.utils.events;

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

import java.util.List;

public interface Testing {

    void assertEquals(Object o1, Object o2);

    <T> T verify(T mock, Object mode);

    <T> void reset(T... mocks);

    void verifyNoMoreInteractions(Object... mocks);

    <T> T any(Class<T> type);

    Object never();

    Object atLeast(int minNumberOfInvocations);

    <T, S extends T> Captor<T> captorForClass(Class<S> clazz);

    interface Captor<T> {
        T capture();

        List<T> getAllValues();
    }
}
