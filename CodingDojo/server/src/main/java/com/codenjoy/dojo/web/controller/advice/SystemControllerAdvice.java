package com.codenjoy.dojo.web.controller.advice;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@ControllerAdvice
public class SystemControllerAdvice {

    public static final String DEBUG_ATTRIBUTE = "debug";
    public static final String ACTIVE_PROFILES_ATTRIBUTE = "activeProfiles";

    @Autowired
    private Environment environment;

    @ModelAttribute(DEBUG_ATTRIBUTE)
    public Boolean debugAdvice() {
        return Stream.of(environment.getActiveProfiles())
                .anyMatch(p -> DEBUG_ATTRIBUTE.equalsIgnoreCase(p.toString()));
    }

    @ModelAttribute(ACTIVE_PROFILES_ATTRIBUTE)
    public List<String> setProfilesVar() {
        return Arrays.asList(environment.getActiveProfiles());
    }
}
