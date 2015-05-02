package com.codenjoy.dojo.integration.mocker;

import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.List;

public class SpyXmlWebApplicationContext extends XmlWebApplicationContext {

    private static List<String> spies;
    private static List<String> mocks;
    private static List<String> logs;

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory factory = super.createBeanFactory();
        factory.addBeanPostProcessor(new SpyPostProcessor());
        factory.setAllowRawInjectionDespiteWrapping(true);
        return factory;
    }

    public static void init(List<String> spies, List<String> mocks, List<String> logs) {
        SpyXmlWebApplicationContext.spies = spies;
        SpyXmlWebApplicationContext.mocks = mocks;
        SpyXmlWebApplicationContext.logs = logs;
    }

    class SpyPostProcessor implements BeanPostProcessor, Ordered {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (spies.contains(beanName)) {
                return Mockito.spy(bean);
            } else if (mocks.contains(beanName)) {
                return Mockito.mock(bean.getClass());
            } else if (logs.contains(beanName)) {
                return LoggerFactory.get(bean);
            } else {
                return bean;
            }
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }


}

