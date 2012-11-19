package integraion;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * User: oleksandr.baglai
 * Date: 11/18/12
 * Time: 5:07 PM
 */
public class SpringMockerJettyRunner extends JettyRunner {

    private Map<String, Object> mocks = new HashMap<String, Object>();
    private Map<String, Object> spies = new HashMap<String, Object>();
    private static final boolean CREATE_MOCK = true;
    private static final boolean CREATE_SPY = true;
    private boolean started;

    public SpringMockerJettyRunner(String... webContextPlaces) {
        super(webContextPlaces);

        addSpringContextInitListener(new SpringContextInitEvent() {
            @Override
            public void contextInit(WebApplicationContext context) {
                for (Map.Entry<String, Object> entry : mocks.entrySet()) {
                    entry.setValue(mocking(context, entry.getKey(), CREATE_MOCK));
                }

                for (Map.Entry<String, Object> entry : spies.entrySet()) {
                    entry.setValue(mocking(context, entry.getKey(), CREATE_SPY));
                }
            }
        });
    }

    public int start() throws Exception {
        int port = super.start();
        started = true;
        return port;
    }

    public void stop() throws Exception {
        started = false;
        super.stop();
    }

    public SpringMockerJettyRunner mockBean(String name) {
        mocks.put(name, null);
        return this;
    }

    public SpringMockerJettyRunner spyBean(String name) {
        spies.put(name, null);
        return this;
    }

    public <T> T getBean(String name) {
        if (!started) {
            throw new RuntimeException("First start the server.");
        }
        if (mocks.containsKey(name)) {
            return (T) mocks.get(name);
        } else if (spies.containsKey(name)) {
            return (T) spies.get(name);
        }
        throw new RuntimeException("Bean '" + name + "' not found.");
    }


    private static Object mocking(WebApplicationContext webAppContext, String beanName, boolean isMockOfSpy) {
        Object realBean = webAppContext.getBean(beanName);

        XmlWebApplicationContext context = (XmlWebApplicationContext) webAppContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

        Object newBean = null;
        if (isMockOfSpy) {
            newBean = mock(realBean.getClass());
        } else {
            newBean = spy(realBean);
        }

        beanFactory.removeBeanDefinition(beanName);
        beanFactory.registerSingleton(beanName, newBean);
        return beanFactory.getBean(beanName);
    }

}
