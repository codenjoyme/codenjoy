package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.ProxyFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class SpyTest {

    private SomeImpl impl;
    private InvocationHandler handler;

    interface Some {
        String method(String input);
    }

    class SomeImpl implements Some {
        public String method(String input) {
            return "SomeImpl say: " + input;
        }

        public String method2(String input) {
            return "SomeImpl say from another method: " + input;
        }
    }

    @Before
    public void setup() {
        impl = spy(new SomeImpl());
        handler = mock(InvocationHandler.class);
    }

    @Test
    public void shouldCallSpyWithoutIntrusion() throws Throwable {
        // given
        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        // when
        String result = iface.method("args");

        // then
        assertEquals("SomeImpl say: args", result);
        assertSpyCalledWith("method", "[args]");

        verify(impl).method("args");
        verifyNoMoreInteractions(impl);
    }

    private void assertSpyCalledWith(String expectedMethod, String expectedArgs) throws Throwable {
        ArgumentCaptor<Object[]> captureArgs = ArgumentCaptor.forClass(Object[].class);
        ArgumentCaptor<Method> captureMethod = ArgumentCaptor.forClass(Method.class);

        verify(handler, times(1)).invoke(eq(impl), captureMethod.capture(), captureArgs.capture());

        assertEquals(expectedArgs, Arrays.toString(captureArgs.getValue()));
        assertEquals(expectedMethod, captureMethod.getValue().getName());
    }

    @Test
    public void shouldCallSpyWithIntrusion_useSpyAnswerChangeArgs() throws Throwable {
        // given
        setupSpy(ProxyFactory.resultBuilder().with("new args").returns("new answer").get());

        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        // when
        String result = iface.method("args");

        // then
        assertEquals("new answer", result);
        assertSpyCalledWith("method", "[args]");

        verify(impl).method("new args");
        verifyNoMoreInteractions(impl);
    }
    private void setupSpy(ProxyFactory.ResultSet setup) throws Throwable {
        when(handler.invoke(eq(impl), any(Method.class), any(Object[].class))).thenReturn(setup);
    }

    @Test
    public void shouldCallSpyWithIntrusion_calRealMethodWithSameArgs() throws Throwable {
        // given
        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        setupSpy(ProxyFactory.resultBuilder().get());

        // when
        String result = iface.method("args");

        // then
        assertEquals("SomeImpl say: args", result);
        assertSpyCalledWith("method", "[args]");

        verify(impl).method("args");
        verifyNoMoreInteractions(impl);
    }

    @Test
    public void shouldCallSpyWithIntrusion_callAnotherMethod() throws Throwable {
        // given
        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        setupSpy(ProxyFactory.resultBuilder().call("method2").with("new args").get());

        // when
        String result = iface.method("args");

        // then
        assertEquals("SomeImpl say from another method: new args", result);
        assertSpyCalledWith("method", "[args]");

        verify(impl).method2("new args");
        verifyNoMoreInteractions(impl);
    }

    @Test
    public void shouldCallSpyWithIntrusion_callAnotherMethodWithRealArgs() throws Throwable {
        // given
        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        setupSpy(ProxyFactory.resultBuilder().call("method2").get());

        // when
        String result = iface.method("args");

        // then
        assertEquals("SomeImpl say from another method: args", result);
        assertSpyCalledWith("method", "[args]");

        verify(impl).method2("args");
        verifyNoMoreInteractions(impl);
    }

    @Test
    public void shouldCallSpyWithIntrusion_callRealMethodWithChangeResult() throws Throwable {
        // given
        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        setupSpy(ProxyFactory.resultBuilder().returns("new answer").get());

        // when
        String result = iface.method("args");

        // then
        assertEquals("new answer", result);
        assertSpyCalledWith("method", "[args]");

        verify(impl).method("args");
        verifyNoMoreInteractions(impl);
    }

    @Test
    public void shouldCallSpyWithIntrusion_dontCallRealMethodWithChangeResult() throws Throwable {
        // given
        Some iface = ProxyFactory.object(impl).spy(handler).getAs(Some.class);

        setupSpy(ProxyFactory.resultBuilder().dontCallRealMethod().returns("new answer").get());

        // when
        String result = iface.method("args");

        // then
        assertEquals("new answer", result);
        assertSpyCalledWith("method", "[args]");

        verifyNoMoreInteractions(impl);
    }

    @Test
    public void shouldExceptionWhenBadUse() throws Throwable {
        try {
            ProxyFactory.resultBuilder().dontCallRealMethod().call("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("You can not use method 'dontCallRealMethod()' with method 'call(String name)'", e.getMessage());
        }

        try {
            ProxyFactory.resultBuilder().dontCallRealMethod().with("");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("You can not use method 'dontCallRealMethod()' with method 'with(Object... args)'", e.getMessage());
        }

        try {
            ProxyFactory.resultBuilder().dontCallRealMethod().doAfter(null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("You can not use method 'dontCallRealMethod()' with method 'doAfter(After after)'", e.getMessage());
        }
    }

    @Test
    public void shouldCanUseAsSameClass() throws Throwable {
        final ProxyFactory.After after = new ProxyFactory.After() {
            public Object doit(Object result) {
                return result + "_zxc";
            }
        };

        InvocationHandler before = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return ProxyFactory.resultBuilder().with("asd_" + args[0]).doAfter(after).get();
            }
        };

        Some impl2 = ProxyFactory.object(impl).spy(before).getAs(Some.class);

        assertEquals("SomeImpl say: asd_qwe_zxc", impl2.method("qwe"));
    }
}
