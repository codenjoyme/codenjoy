package com.codenjoy.dojo.integration.mocker;

import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MockerConfiguration implements Configuration {


    private static JettyRunner runner;

    // TODO подумать как можно сделать без статики
    public static void add(JettyRunner runner) {
        MockerConfiguration.runner = runner;
    }

    @Override
    public void preConfigure(WebAppContext context) {
        // do nothing
    }

    @Override
    public void configure(WebAppContext context) {
        // do nothing
    }

    @Override
    public void postConfigure(WebAppContext context) {
        if (runner != null) {
            runner.setupApplicationContext(context.getServletContext());
        }

    }

    @Override
    public void deconfigure(WebAppContext context) {
        // do nothing
    }

    @Override
    public void destroy(WebAppContext context) {
        // do nothing
    }

    @Override
    public void cloneConfigure(WebAppContext template, WebAppContext context) {
        // do nothing
    }
}
