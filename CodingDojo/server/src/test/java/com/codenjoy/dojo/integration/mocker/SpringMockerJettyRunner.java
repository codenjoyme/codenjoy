package com.codenjoy.dojo.integration.mocker;

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


import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SpringMockerJettyRunner extends JettyRunner {

    private List<String> mocks = new LinkedList<>();
    private List<String> spies = new LinkedList<>();
    private List<String> logs = new LinkedList<>();
    public WebApplicationContext context;

    private boolean started;

    public SpringMockerJettyRunner(String webApp, String contextPath) {
        super(webApp, contextPath);

        addListener((context, servletContext) -> {
            String contextClass = context.getInitParameter(ContextLoader.CONTEXT_CLASS_PARAM);
            if (!contextClass.equals(XmlWebApplicationContext.class.getName())) {
                throw new RuntimeException("Тип " + contextClass + " не поддерживается!");
            }
            context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, SpyXmlWebApplicationContext.class.getName());
            SpyXmlWebApplicationContext.init(spies, mocks, logs);
        });

        addListener(context -> {
            SpringMockerJettyRunner.this.context = context;
        });
    }

    public int start(int port) throws Exception {
        port = super.start(port);
        started = true;
        return port;
    }

    public void stop() throws Exception {
        started = false;
        super.stop();
    }

    public SpringMockerJettyRunner mockBean(String... names) {
        mocks.addAll(Arrays.asList(names));
        return this;
    }

    public SpringMockerJettyRunner spyBean(String... names) {
        spies.addAll(Arrays.asList(names));
        return this;
    }

    public SpringMockerJettyRunner logBean(String... names) {
        logs.addAll(Arrays.asList(names));
        return this;
    }

    public <T> T getBean(Class<T> clazz, String name) {
        if (!started) {
            throw new RuntimeException("First start the server.");
        }
        return (T) context.getBean(name);
    }

}
