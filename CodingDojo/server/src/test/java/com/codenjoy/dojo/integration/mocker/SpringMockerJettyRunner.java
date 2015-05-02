package com.codenjoy.dojo.integration.mocker;

import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SpringMockerJettyRunner extends JettyRunner {

    private List<String> mocks = new LinkedList<String>();
    private List<String> spies = new LinkedList<String>();
    private List<String> logs = new LinkedList<String>();
    public WebApplicationContext context;

    private boolean started;

    public SpringMockerJettyRunner(String webApp, String contextPath) {
        super(webApp, contextPath);

        addListener(new ServletContextInitEvent() {
            @Override
            public void contextInit(WebAppContext context, ServletContext servletContext) {
                String contextClass = context.getInitParameter(ContextLoader.CONTEXT_CLASS_PARAM);
                if (!contextClass.equals(XmlWebApplicationContext.class.getName())) {
                    throw new RuntimeException("Тип " + contextClass + " не поддерживается!");
                }
                context.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM, SpyXmlWebApplicationContext.class.getName());
                SpyXmlWebApplicationContext.init(spies, mocks, logs);
            }
        });

        addListener(new SpringContextInitEvent() {

            @Override
            public void contextInit(WebApplicationContext context) {
                SpringMockerJettyRunner.this.context = context;
            }
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
