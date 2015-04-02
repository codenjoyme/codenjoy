package com.codenjoy.dojo.integration.mocker;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
* Created by Sanja on 19.06.14.
*/
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
