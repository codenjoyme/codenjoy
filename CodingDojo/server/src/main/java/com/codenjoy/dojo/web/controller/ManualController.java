package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@AllArgsConstructor
@Slf4j
public class ManualController {
    public static final String MANUAL_URI = "/manual";
    private static final String CODENJOY_DEFAULT_MANUAL_TYPE = "codenjoy";
    private static final String EN_DEFAULT_LANGUAGE_FOR_MANUAL = "en";

    private Validator validator;

    @GetMapping(value = MANUAL_URI + "/{game}")
    public String manualForGameWithDefaultLanguage(
            @PathVariable String game,
            @RequestParam(required = false) String type
    ) {
        return manualForGame(game, type, EN_DEFAULT_LANGUAGE_FOR_MANUAL);
    }

    @GetMapping(value = MANUAL_URI + "/{game}/{language}")
    public String manualForGame(
            @PathVariable String game,
            @RequestParam(required = false) String type,
            @PathVariable String language) {

        validator.checkGame(game, CANT_BE_NULL);
        validator.checkLanguageCode(language, CAN_BE_NULL);
        validator.checkManualType(type, CAN_BE_NULL);

        if (Strings.isNullOrEmpty(type)) {
            type = CODENJOY_DEFAULT_MANUAL_TYPE;
        }
        if (Strings.isNullOrEmpty(language)) {
            language = EN_DEFAULT_LANGUAGE_FOR_MANUAL;
        }

        return getRedirectURI(game, type, language);
    }

    private String getRedirectURI(String game, String type, String language) {
        return String.format("redirect:/resources/%s/help/%s-manual-%s.md", game, type, language);
    }
}