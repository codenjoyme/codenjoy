package com.codenjoy.dojo.config;

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

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.util.Random;

@Slf4j
public class TestSqliteDBLocations
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        setup(context, "messages", "messages.db");
        setup(context, "log", "logs.db");
        setup(context, "payment", "payment.db");
        setup(context, "saves", "saves.db");
        setup(context, "users", "users.db");
        setup(context, "settings", "settings.db");
    }

    public void setup(ConfigurableApplicationContext context, String db, String file) {
        String dbFile = "target/" + file + new Random().nextInt();
        String property = "database.files." + db + "=" + dbFile;
        log.info("Property overridden:" + property);
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, property);
    }
}
