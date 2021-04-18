package com.codenjoy.dojo.utils.events;

import com.codenjoy.dojo.utils.events.EventsListenersAssert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Некоторые тестовые утилитные классы находятся в engine и используются в играх как библиотеки.
 * Но вот проблема, в них надо использовать и Assert и Mockito, а эти библиотеки импортируются Maven
 * только для тестов. Потому через этого малого мы инкапсулируем доступ к Maven и JUnit.Assert через reflections.
 * Вот так вот! И такой код тоже бывает. Но не забываем, что любой подход - это упражнение по программированию.
 * Если найдешь лучшее решение, кроме как импортировать Maven и JUnit не для тестов - ждем PullRequest.
 */
public class MockitoMocker implements EventsListenersAssert.Mocker {

    private Class<?> Assert;
    private Class<?> Mockito;
    private Class<?> VerificationMode;
    private Class<?> ArgumentCaptor;

    public MockitoMocker() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Assert = classLoader.loadClass("org.junit.Assert");
            Mockito = classLoader.loadClass("org.mockito.Mockito");
            VerificationMode = classLoader.loadClass("org.mockito.verification.VerificationMode");
            ArgumentCaptor = classLoader.loadClass("org.mockito.ArgumentCaptor");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // так вызываются статические методы класса
    public Object callStatic(Class<?> clazz, String name, Class[] argsTypes, Object[] args) {
        return call(clazz, name, argsTypes, clazz, args);
    }

    // а так вызываются методы на объекте
    public Object call(Class<?> clazz, String name, Class[] argsTypes, Object thizz, Object[] args) {
        try {
            Method method = clazz.getDeclaredMethod(name, argsTypes);
            return method.invoke(thizz, args);
        } catch (Exception e) {
            process(e);
        }
        return null;
    }

    // тут мы разворачиваем исключения так, чтобы тесты валились в IDE
    // привычно с подсветкой Diff actual vs expected
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
    public void assertEquals(Object o1, Object o2) {
        callStatic(Assert, "assertEquals",
                new Class[]{Object.class, Object.class},
                new Object[]{o1, o2});
    }

    @Override
    public <T> T verify(T mock, Object mode) {
        return (T) callStatic(Mockito, "verify",
                new Class[]{Object.class, VerificationMode},
                new Object[]{mock, mode});
    }

    @Override
    public <T> void reset(T... mocks) {
        callStatic(Mockito, "reset",
                new Class[]{Object[].class},
                new Object[]{(Object) mocks});
    }

    @Override
    public void verifyNoMoreInteractions(Object... mocks) {
        callStatic(Mockito, "verifyNoMoreInteractions",
                new Class[]{Object[].class},
                new Object[]{(Object) mocks});
    }

    @Override
    public <T> T any(Class<T> type) {
        return (T) callStatic(Mockito, "any",
                new Class[]{Class.class},
                new Object[]{type});
    }

    @Override
    public Object never() {
        return callStatic(Mockito, "never",
                new Class[]{},
                new Object[]{});
    }

    @Override
    public Object atLeast(int minNumberOfInvocations) {
        return callStatic(Mockito, "atLeast",
                new Class[]{int.class},
                new Object[]{minNumberOfInvocations});
    }

    public Object getArgumentCaptor(Class clazz) {
        return callStatic(ArgumentCaptor, "forClass",
                new Class[]{Class.class},
                new Object[]{clazz});
    }

    @Override
    public <T, S extends T> EventsListenersAssert.Captor<T> captorForClass(Class<S> clazz) {
        Object captor = getArgumentCaptor(clazz);
        return new EventsListenersAssert.Captor<T>() {
            @Override
            public T capture() {
                return (T) call(ArgumentCaptor, "capture",
                        new Class[]{},
                        captor, new Object[]{});
            }

            @Override
            public List<T> getAllValues() {
                return (List<T>) call(ArgumentCaptor, "getAllValues",
                        new Class[]{},
                        captor, new Object[]{});
            }
        };
    }
}
