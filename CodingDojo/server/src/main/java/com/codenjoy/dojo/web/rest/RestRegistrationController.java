package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.GuiPlotColorDecoder;
import com.codenjoy.dojo.services.NullGameType;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/rest/user")
public class RestRegistrationController {

    @Autowired private Registration registration;

    @RequestMapping(value = "/{playerName}/check/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUserLogin(@PathVariable("playerName") String playerName, @PathVariable("code") String code) {
        String actualName = registration.getEmail(code);
        return actualName != null && actualName.equals(playerName);
    }
}
