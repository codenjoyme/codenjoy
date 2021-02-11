package com.codenjoy.dojo.bomberman.client.simple;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Pattern {
    
    private String pattern;
    private Synonyms synonyms;

    public Pattern(String pattern, Synonyms synonyms) {
        this.pattern = pattern;
        this.synonyms = synonyms;
    }

    public Pattern() {
        pattern = StringUtils.EMPTY;
        synonyms = new Synonyms(); 
    }

    public String pattern() {
        return pattern;
    }

    public Synonyms synonyms() {
        return synonyms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pattern pattern1 = (Pattern) o;
        return pattern.equals(pattern1.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern);
    }

    @Override
    public String toString() {
        return String.format("\n" +
                TestUtils.injectN(pattern) +
                "synonyms: " +
                synonyms);
    }
}
