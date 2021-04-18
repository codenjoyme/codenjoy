package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.utils.EventsListenersAssert;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MockitoMocker implements EventsListenersAssert.Mocker {

    private Class<?> assertClass;
    private Class<?> mockitoClass;
    private Class<?> mockitoVerificationModeClass;
    private Class<?> argumentCaptorClass;

    public MockitoMocker() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            assertClass = classLoader.loadClass("org.junit.Assert");
            mockitoClass = classLoader.loadClass("org.mockito.Mockito");
            mockitoVerificationModeClass = classLoader.loadClass("org.mockito.verification.VerificationMode");
            argumentCaptorClass = classLoader.loadClass("org.mockito.ArgumentCaptor");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assertEquals(Object o1, Object o2) {
        call(assertClass, "assertEquals",
                new Class[]{Object.class, Object.class},
                new Object[]{o1, o2});
    }

    public Object call(Class<?> clazz, String name, Class[] argsTypes, Object[] args) {
        try {
            Method method = clazz.getDeclaredMethod(name, argsTypes);
            return method.invoke(clazz, args);
        } catch (Exception e) {
            process(e);
        }
        return null;
    }

    public void process(Exception e) {
        if (e instanceof InvocationTargetException) {
            Throwable target = ((InvocationTargetException) e).getTargetException();
            if (target instanceof Error) {
                throw (Error) target;
            }
            if (target instanceof RuntimeException) {
                throw (RuntimeException) target;
            }
        }
        throw new RuntimeException(e);
    }

    @Override
    public <T> T verify(T mock, Object mode) {
        return (T) call(mockitoClass, "verify",
                new Class[]{Object.class, mockitoVerificationModeClass},
                new Object[]{mock, mode});
    }

    @Override
    public <T> void reset(T... mocks) {
        call(mockitoClass, "reset",
                new Class[]{Object[].class},
                new Object[]{(Object) mocks});
    }

    @Override
    public void verifyNoMoreInteractions(Object... mocks) {
        call(mockitoClass, "verifyNoMoreInteractions",
                new Class[]{Object[].class},
                new Object[]{(Object) mocks});
    }

    @Override
    public <T> T any(Class<T> type) {
        return (T) call(mockitoClass, "any",
                new Class[]{Class.class},
                new Object[]{type});
    }

    @Override
    public Object never() {
        return call(mockitoClass, "never",
                new Class[]{},
                new Object[]{});
    }

    @Override
    public Object atLeast(int minNumberOfInvocations) {
        return call(mockitoClass, "atLeast",
                new Class[]{},
                new Object[]{});
    }

    @Override
    public <T, S extends T> EventsListenersAssert.Captor<T> captorForClass(Class<S> clazz) {
        ArgumentCaptor<T> captor = getArgumentCaptor(clazz);
        return new EventsListenersAssert.Captor<T>() {
            @Override
            public T capture() {
                try {
                    Method method = argumentCaptorClass.getDeclaredMethod("capture");
                    return (T) method.invoke(captor);
                } catch (Exception e) {
                    process(e);
                }
                return null;
            }

            @Override
            public List<T> getAllValues() {
                try {
                    Method method = argumentCaptorClass.getDeclaredMethod("getAllValues");
                    return (List<T>) method.invoke(captor);
                } catch (Exception e) {
                    process(e);
                }
                return null;
            }
        };
    }

    public <T, S extends T> ArgumentCaptor<T> getArgumentCaptor(Class<S> clazz) {
        ArgumentCaptor<T> captor = null;
        try {
            Method method = argumentCaptorClass.getDeclaredMethod("forClass", Class.class);
            captor = (ArgumentCaptor<T>) method.invoke(argumentCaptorClass, clazz);
        } catch (Exception e) {
            process(e);
        }
        return captor;
    }
}
