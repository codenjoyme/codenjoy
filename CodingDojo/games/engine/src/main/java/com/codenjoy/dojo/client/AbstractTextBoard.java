package com.codenjoy.dojo.client;

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


import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.UnicodeUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractTextBoard implements ClientBoard {

    protected String data;

    @Override
    public ClientBoard forString(String data) {
        this.data = UnicodeUtils.unescapeJava(data);
        return this;
    }

    public boolean isGameOver() {
        return StringUtils.isEmpty(data);
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BoardData " + JsonUtils.prettyPrint(data);
    }
}
