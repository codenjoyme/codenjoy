package com.codenjoy.dojo.services.helper;

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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.utils.TestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class ChatDealsUtils {

    public static void setupChat(Controller chatController) {
        doAnswer(inv -> {
            Deal deal = (Deal)inv.getArguments()[0];
            TestUtils.setupChat(deal);
            return null;
        }).when(chatController).register(any(Deal.class));
    }

    public static void setupReadableName(Registration registration) {
        // for the field chat activity
        doAnswer(inv -> {
            String id = (String) inv.getArguments()[0];
            return id + "_name";
        }).when(registration).getNameById(anyString());

    }
}
