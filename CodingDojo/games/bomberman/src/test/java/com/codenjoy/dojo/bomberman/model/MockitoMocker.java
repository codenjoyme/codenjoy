package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.utils.EventsListenersAssert;

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
        callStatic(assertClass, "assertEquals",
                new Class[]{Object.class, Object.class},
                new Object[]{o1, o2});
    }

    public Object callStatic(Class<?> clazz, String name, Class[] argsTypes, Object[] args) {
        return call(clazz, name, argsTypes, clazz, args);
    }

    public Object call(Class<?> clazz, String name, Class[] argsTypes, Object thizz, Object[] args) {
        try {
            Method method = clazz.getDeclaredMethod(name, argsTypes);
            return method.invoke(thizz, args);
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
        return (T) callStatic(mockitoClass, "verify",
                new Class[]{Object.class, mockitoVerificationModeClass},
                new Object[]{mock, mode});
    }

    @Override
    public <T> void reset(T... mocks) {
        callStatic(mockitoClass, "reset",
                new Class[]{Object[].class},
                new Object[]{(Object) mocks});
    }

    @Override
    public void verifyNoMoreInteractions(Object... mocks) {
        callStatic(mockitoClass, "verifyNoMoreInteractions",
                new Class[]{Object[].class},
                new Object[]{(Object) mocks});
    }

    @Override
    public <T> T any(Class<T> type) {
        return (T) callStatic(mockitoClass, "any",
                new Class[]{Class.class},
                new Object[]{type});
    }

    @Override
    public Object never() {
        return callStatic(mockitoClass, "never",
                new Class[]{},
                new Object[]{});
    }

    @Override
    public Object atLeast(int minNumberOfInvocations) {
        return callStatic(mockitoClass, "atLeast",
                new Class[]{int.class},
                new Object[]{minNumberOfInvocations});
    }

    @Override
    public <T, S extends T> EventsListenersAssert.Captor<T> captorForClass(Class<S> clazz) {
        Object captor = getArgumentCaptor(clazz);
        return new EventsListenersAssert.Captor<T>() {
            @Override
            public T capture() {
                return (T) call(argumentCaptorClass, "capture",
                        new Class[]{},
                        captor, new Object[]{});
            }

            @Override
            public List<T> getAllValues() {
                return (List<T>) call(argumentCaptorClass, "getAllValues",
                        new Class[]{},
                        captor, new Object[]{});
            }
        };
    }

    public Object getArgumentCaptor(Class clazz) {
        return callStatic(argumentCaptorClass, "forClass",
                new Class[]{Class.class},
                new Object[]{clazz});
    }
}
