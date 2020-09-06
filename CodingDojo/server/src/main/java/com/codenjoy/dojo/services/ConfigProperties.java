package com.codenjoy.dojo.services;

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

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Тут собраны только те проперти, которые важны в контроллерах.
 * Все дело в том, что я не хочу делать второго конфига который будет уметь
 * находить properties файлы вокруг приложения еще и в spring-context.xml
 * а это потому, что он не обрабатывается фильтрами maven при сборке в war.
 * Единственное место, где конфигурится *.properties - applicationContext.xml
 */
@Component
@Getter
@Setter
public class ConfigProperties {

    @Value("${email.verification}")
    private boolean isEmailVerificationNeeded;

    @Value("${page.main.url}")
    private String mainPage;

    @Value("${page.admin.url}")
    private String adminPage;

    @Value("${page.help.language}")
    private String helpLanguage;

    @Value("${donate.code}")
    private String donateCode;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.login}")
    private String adminLogin;

    @Value("${server.ip}")
    private String serverIp;

    @Value("${registration.password.autogen-length}")
    private int autoGenPasswordLen;

    @Value("${registration.opened}")
    private boolean isRegistrationOpened;
}
