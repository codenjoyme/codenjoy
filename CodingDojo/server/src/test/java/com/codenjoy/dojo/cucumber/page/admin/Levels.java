package com.codenjoy.dojo.cucumber.page.admin;

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

import com.codenjoy.dojo.cucumber.page.PageObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.openqa.selenium.By;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.cucumber.page.WebDriverWrapper.getText;
import static com.codenjoy.dojo.cucumber.utils.Assert.assertEquals;
import static com.codenjoy.dojo.cucumber.utils.PageUtils.xpath;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class Levels extends PageObject {

    // selectors
    public static final String LEVELS = "//table[@id='levels']";
    public static final BiFunction<String, String, By> MAP = (index, with) -> xpath(LEVELS + "//tr[@index%s]//*[@with='%s']", index, with);
    public static final Function<Integer, By> MAP_KEY = index -> MAP.apply(format("='%s'", index), "key");
    public static final Function<Integer, By> MAP_VALUE = index -> MAP.apply(format("='%s'", index), "value");
    public static final By SAVE_BUTTON = xpath(LEVELS + "//input[@value='Save']");
    public static final By ADD_BUTTON = xpath(LEVELS + "//input[@value='Add']");

    public String mapKey(int index) {
        return web.text(MAP_KEY.apply(index));
    }

    public void mapKey(int index, String value) {
        web.text(MAP_KEY.apply(index), value);
    }

    public String mapValue(int index) {
        return web.text(MAP_VALUE.apply(index));
    }

    public void mapValue(int index, String value) {
        web.text(MAP_VALUE.apply(index), value);
    }

    public List<String> keys() {
        return web.elementsBy(MAP.apply("", "key")).stream()
                .map(element -> getText(element))
                .collect(toList());
    }

    public Map<String, String> map() {
        final int[] id = {0};
        return new LinkedHashMap<>(){{
            List<String> keys = keys();
            for (int index = 0; index < keys.size(); index++) {
                String key = mapKey(index);
                if (StringUtils.isEmpty(key)) {
                    key = "<EMPTY" + (++id[0]) + ">";
                }
                this.put(key, mapValue(index));
            }
        }};
    }

    public void assertMaps(String expected) {
        assertEquals(expected, toString());
    }

    @Override
    public String toString() {
        List<Map.Entry<String, String>> entries = Lists.newArrayList(map().entrySet());
        return IntStream.range(0, entries.size())
                .mapToObj(index -> String.format("(%s)%s=%s",
                        index,
                        entries.get(index).getKey().replace("[Level] Map", ""),
                        entries.get(index).getValue()))
                .collect(joining(", "));
    }

    public void save() {
        web.elementBy(SAVE_BUTTON).click();
    }

    public void add() {
        web.elementBy(ADD_BUTTON).click();
    }
}
