package com.codenjoy.integration.mocker;

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


import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class LoggerFactory {
    private static List<Invocation> invocations = new LinkedList<Invocation>();

    public static <T> T get(T object) {
        return (T) Enhancer.create(object.getClass(), new MyInvocationHandler(object));
    }

    public static List<Invocation> getAll() {
        return invocations;
    }

    public interface Invocation {
        Method method();
        String toString();
        Object returns();
        Object[] parameters();
    }

    static class MyInvocationHandler implements MethodInterceptor {
        private Object object;

        public MyInvocationHandler(Object object) {
            this.object = object;
        }

        @Override
        public Object intercept(final Object proxy, final Method method, final Object[] args, MethodProxy methodProxy) throws Throwable {
            synchronized (invocations) {
                final Object result = method.invoke(object, args);
                invocations.add(new Invocation() {
                    @Override
                    public Method method() {
                        return method;
                    }

                    @Override
                    public String toString() {
                        String s = "";
                        for (Object arg : args) {
                            if (!s.equals("")) s += ", ";
                            s += getValue(arg);
                        }

                        return object.getClass().getSimpleName() + "." + method.getName() + "(" + s + ") => " + getValue(result);
                    }

                    private String getValue(Object arg) {
                        if (arg instanceof Objects) {
                            return arg.getClass().getSimpleName() + "[" +  arg + "]";
                        } else if (arg instanceof String) {
                            return "'" + arg + "'";
                        } else {
                            return "'" + arg + "'";
                        }
                    }

                    @Override
                    public Object returns() {
                        return result;
                    }

                    @Override
                    public Object[] parameters() {
                        return args;
                    }
                });
                return result;
            }
        }
    }
}
