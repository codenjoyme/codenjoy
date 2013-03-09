package com.codenjoy.bomberman.model;

import com.apofig.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import static org.fest.reflect.core.Reflection.method;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 7:24 PM
 */
public class ListUtils {

    public interface ListFactory {
        Object create();
    }

    public static List<Bomb> getUnmodifiableList(final ListFactory factory) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result = factory.create();
                Object invoke = method(method.getName()).withParameterTypes(method.getParameterTypes()).in(result).invoke(args);
                return ProxyFactory.resultBuilder().dontCallRealMethod().returns(invoke).get();
            }
        };
        return ProxyFactory.object(factory.create()).spy(handler).getAs(List.class);
    }
}
