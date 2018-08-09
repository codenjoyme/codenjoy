package com.codenjoy.dojo.services;

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


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.fest.reflect.core.Reflection.method;

public class ProxyFactory {
    private Object object;
    private InvocationHandler handler;

    public static ProxyFactory object(Object object) {
        return new ProxyFactory(object);
    }

    private ProxyFactory(Object object) {
        this.object = object;
    }

    public <T> T getAs(Class<T> asClass) {
        checkCamparability(object, asClass);

        if (!asClass.isInterface()) {
            asClass = (Class<T>) asClass.getInterfaces()[0];
        }

        return (T) Proxy.newProxyInstance(asClass.getClassLoader(),
                new Class[]{asClass},
                new ObjectProxy(object, handler));
    }

    private <T> void checkCamparability(Object object, Class<T> asClass) {
        for (Method method : asClass.getDeclaredMethods()) {
            Method found = method(method.getName())
                    .withParameterTypes(method.getParameterTypes())
                    .in(object).info();

            if (!found.getReturnType().equals(method.getReturnType())) {
                throw new RuntimeException(String.format("Unable to find method '%s' in %s " +
                        "with parameter type(s) %s with return type %s",
                        method.getName(),
                        object.getClass().getName(),
                        Arrays.toString(method.getParameterTypes()),
                        method.getReturnType().getName()));
            }
        }
    }

    public ProxyFactory spy(InvocationHandler handler) {
        this.handler = handler;
        return this;
    }

    public interface After {
        Object doit(Object result);
    }

    public interface ResultSet {
        boolean isCallAtRealObject();
        String methodName();
        Object[] withArgs();

        Object myResult();
        boolean returnMe();
        After after();
    }

    public static class ResultSetBoulder {
        private boolean isCallAtRealObject = true;
        private String methodName = null;
        private Object[] withArgs = null;
        private boolean returnMe = false;
        private Object myResult = null;
        private After after = null;

        public ResultSet get() {
            return getResultSet(isCallAtRealObject, methodName, withArgs, returnMe, myResult, after);
        }

        public ResultSetBoulder dontCallRealMethod() {
            isCallAtRealObject = false;
            return this;
        }

        public ResultSetBoulder call(String name) {
            if (!isCallAtRealObject) {
                throw new IllegalArgumentException("You can not use method 'dontCallRealMethod()' with method 'call(String name)'");
            }
            methodName = name;
            return this;
        }

        public ResultSetBoulder with(Object... args) {
            if (!isCallAtRealObject) {
                throw new IllegalArgumentException("You can not use method 'dontCallRealMethod()' with method 'with(Object... args)'");
            }
            withArgs = args;
            return this;
        }

        public ResultSetBoulder returns(Object result) {
            returnMe = true;
            this.myResult = result;
            return this;
        }

        public ResultSetBoulder doAfter(After after) {
            if (!isCallAtRealObject) {
                throw new IllegalArgumentException("You can not use method 'dontCallRealMethod()' with method 'doAfter(After after)'");
            }
            this.after = after;
            return this;
        }
    }

    public static ResultSetBoulder resultBuilder() {
        return new ResultSetBoulder();
    }

    static ResultSet getResultSet(final boolean isCallAtRealObject,
                                               final String methodName,
                                               final Object[] withArgs,
                                               final boolean returnMe,
                                               final Object myResult,
                                               final After after)
    {
        return new ResultSet() {
            @Override
            public boolean isCallAtRealObject() {
                return isCallAtRealObject;
            }

            @Override
            public boolean returnMe() {
                return returnMe;
            }

            @Override
            public After after() {
                return after;
            }

            @Override
            public Object myResult() {
                return myResult;
            }

            @Override
            public Object[] withArgs() {
                return withArgs;
            }

            @Override
            public String methodName() {
                return methodName;
            }
        };
    }

    class ObjectProxy implements InvocationHandler {
        private Object object;
        private InvocationHandler handler;

        public ObjectProxy(Object object) {
            this.object = object;
        }

        public ObjectProxy(Object object, InvocationHandler handler) {
            this(object);
            this.handler = handler;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (handler != null) {
                Object result = handler.invoke(object, method, args);

                if (result instanceof ResultSet) {
                    ResultSet settings = (ResultSet)result;

                    if (!settings.isCallAtRealObject()) {
                        return settings.myResult();
                    }

                    if (settings.withArgs() != null) {
                        args = settings.withArgs();
                    }
                    if (settings.methodName() != null) {

                        method = findMethod(settings.methodName(), args);
                    }

                    Object realResult = callRealMethod(method, args);

                    if (settings.after() != null) {
                        realResult = settings.after().doit(realResult);
                    }

                    if (settings.returnMe()) {
                        return settings.myResult();
                    } else {
                        return realResult;
                    }
                }
            }
            return callRealMethod(method, args);
        }

        private Method findMethod(String methodName, Object[] args) {
            for (Method method : object.getClass().getMethods()) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }

                Class<?>[] types = method.getParameterTypes();
                if (types.length != args.length) {
                    continue;
                }

                for (int index = 0; index < types.length; index ++) {
                    if (!args[index].getClass().isAssignableFrom(types[index])) {
                        continue;
                    }
                }
                return method;
            }
            throw new NoSuchMethodError(String.format("Method %s for %s not found.", methodName, Arrays.toString(args)));
        }

        private Object callRealMethod(Method method, Object[] args) {
            return method(method.getName())
                    .withParameterTypes(method.getParameterTypes())
                    .in(object).invoke(args);
        }
    }
}
