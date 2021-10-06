package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.mock;

public abstract class AbstractGameCheckTest extends AbstractGameTest {

    @Rule
    public TestName test = new TestName();
    private List<String> messages;
    private int deep;
    private Pending pending;
    private boolean callRealAssert;
    private BidiMap<Object, Object> wrappers;
    private Caller caller;

    @Before
    @Override
    public void setup() {
        messages = new LinkedList<>();
        wrappers = new DualHashBidiMap<>();
        deep = 0;
        pending = new Pending();
        callRealAssert = false;

        addCall("before");
        super.setup();
        end();

        addCall(test.getMethodName());
    }

    @After
    @Override
    public void after() {
        end(); // end test

        addCall("after");
        super.after();
        end();

        checkFile();
    }

    static class Pending {
        private boolean enabled = false;
        private String value = null;

        public void value(String value) {
            this.value = value;
        }

        public boolean enabled() {
            return enabled;
        }

        public boolean hasValue() {
            return value != null;
        }

        public String value() {
            return value;
        }

        private Pending copy() {
            Pending result = new Pending();
            result.value = value;
            result.enabled = enabled;
            return result;
        }

        public Pending disable() {
            Pending result = copy();
            enabled = false;
            value = null;
            return result;
        }

        public void enable() {
            enabled = true;
        }
    }

    protected String messages() {
        return messages.stream()
                .collect(joining("\n"));
    }

    protected void checkFile() {
        TestUtils.assertSmokeFile(this.getClass().getSimpleName()
                + "/" + test.getMethodName() +  ".data", messages);
    }

    public void assertEquals(Object expected, Object actual) {
        if (callRealAssert) {
            super.assertEquals(expected, actual);
        }
    }

    private void addCall(String method, Object... parameters) {
        call(method, false, parameters);
    }

    private void appendCall(String method, Object... parameters) {
        call(method, true, parameters);
    }

    private void call(String method, boolean append, Object... parameters) {
        if (messages.isEmpty()) {
            append = false;
        }
        if (pending.enabled()) {
            append = true;
        }
        if (!append) {
            deep++;
        }
        List<String> params = Arrays.stream(parameters)
                .map(param -> asString(param))
                .map(string -> string.replaceAll("\n$", ""))
                .collect(toList());
        boolean multiline = params.stream()
                .anyMatch(param -> param.contains("\n"));
        String data = params.stream()
                .collect(joining(multiline ? ",\n" : ", "));
        data = (data.contains("\n") ? "\n" : "") + data;
        data = data.replace("\n", "\n" + leftPad() + leftPad());
        String message = String.format("%s%s%s(%s)",
                (!append && deep == 1) ? "\n" : "",
                (!append) ? leftPad() : "",
                method,
                data);

        if (pending.enabled()) {
            pending.value(message);
            return;
        }

        if (!append) {
            messages.add(message);
        } else {
            append(message);
        }
    }

    private void append(String message) {
        int index = messages.size() - 1;
        messages.set(index, messages.get(index) + message);
    }

    private void appendResult(Object data) {
        append(String.format(" = %s", data.toString()));
    }

    private String asString(Object object) {
        if (object == null) {
            return "null";
        } else if (object.getClass().isArray()) {
            return arrayToString(object)
                    .replaceAll("^\\[", "")
                    .replaceAll("\\]$", "");
        } else {
            return object.toString();
        }
    }

    private String arrayToString(Object object) {
        Class<?> type = object.getClass().getComponentType();
        if (type.isPrimitive()) {
            if (boolean.class.isAssignableFrom(type)) {
                return Arrays.toString((boolean[]) object);
            }

            if (byte.class.isAssignableFrom(type)) {
                return Arrays.toString((byte[]) object);
            }

            if (char.class.isAssignableFrom(type)) {
                return Arrays.toString((char[]) object);
            }

            if (double.class.isAssignableFrom(type)) {
                return Arrays.toString((double[]) object);
            }

            if (float.class.isAssignableFrom(type)) {
                return Arrays.toString((float[]) object);
            }

            if (int.class.isAssignableFrom(type)) {
                return Arrays.toString((int[]) object);
            }

            if (long.class.isAssignableFrom(type)) {
                return Arrays.toString((long[]) object);
            }

            if (short.class.isAssignableFrom(type)) {
                return Arrays.toString((short[]) object);
            }
        }
        return Arrays.deepToString((Object[]) object);
    }

    private String leftPad() {
        return StringUtils.leftPad("", 4*(deep - 1), ' ');
    }

    private void end() {
        if (!pending.enabled()) {
            deep--;
        }
    }

    @Override
    public void dice(int... ints) {
        addCall("dice", ints);

        super.dice(ints);

        end();
    }

    @Override
    public void givenFl(String... maps) {
        addCall("givenFl", maps);

        super.givenFl(maps);

        end();
    }

    @Override
    protected void givenPlayer(Hero hero) {
        addCall("givenPlayer", hero);

        super.givenPlayer(hero);

        end();
    }

    @Override
    public void tick() {
        addCall("tick");

        super.tick();

        end();
    }

    @Override
    public void assertF(String expected, int index) {
        Object actual = super.game(index).getBoardAsString();
        addCall("assertF", actual, index);

        if (callRealAssert) {
            super.assertF(expected, index);
        }

        end();
    }

    @Override
    public Game game(int index) {
//        log("game", index);

        // TODO add wrapper
        Game result = super.game(index);

//        logEnd();

        return result;
    }

    @Override
    public Player player(int index) {
        Player result = objectSpy(super.player(index), false);
        addCall("player", index);
        return result;
    }

    @Override
    public Sample field() {
        Sample result = objectSpy(super.field(), true,
                "newGame",
                "clearScore");
        caller = new Caller("field", result);
        pending.enable();
        return result;
    }

    static class Caller {
        private String name;
        private Object wrapper;
        private Object[] args;

        public Caller(String name, Object wrapper, Object... args) {
            this.name = name;
            this.wrapper = wrapper;
            this.args = args;
        }
    }

    @Override
    public GameSettings settings() {
        GameSettings result = objectSpy(super.settings(), true,
                "[-R]SettingsReader:integer",
                "[-R]SettingsReader:string",
                "[-R]SettingsReader:bool");
        caller = new Caller("settings", result);
        return result;
    }

    @Override
    public Hero hero(int index) {
        Hero result = objectSpy(super.hero(index), false, "itsMe");
        caller = new Caller("hero", result, index);
        return result;
    }

    private <T> T objectSpy(T delegate, boolean include, String... methods) {
        if (wrappers.containsValue(delegate)) {
            return (T) wrappers.getKey(delegate);
        }

        // methods that we dont override
        List<String> excluded = new LinkedList<>();
        List<String> included = new LinkedList<>();
        excluded.addAll(Arrays.asList("equals", "hashCode", "toString"));
        (include ? included : excluded)
                .addAll(Arrays.asList(methods));

        // default constructor parameters fake
        Constructor<?> constructor = delegate.getClass().getDeclaredConstructors()[0];
        Class<?>[] types = constructor.getParameterTypes();
        Object[] typesValues = new Object[types.length];
        for (int index = 0; index < types.length; index++) {
            typesValues[index] = mock(types[index]);
        }

        // setup proxy
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(delegate.getClass());
        factory.setFilter(method -> {
            if (!Modifier.isPublic(method.getModifiers())) {
                return false;
            }

            if (included.isEmpty()) {
                return findFirst(method, excluded).isEmpty();
            } else {
                return findFirst(method, included).isPresent();
            }
        });

        // methods handler
        MethodHandler handler = (self, method, proceed, args) -> {
            Pending last = pending.disable();
            prolongLastCall(delegate);
            appendCall("." + method.getName(), getArgs(args, last));
            unwrapAll(args);
            Object result = method.invoke(delegate, args);
            if (!method.getReturnType().equals(void.class)) {
                boolean showResult = true;
                Optional<String> pattern = findFirst(method, included);
                if (pattern.isPresent()) {
                    showResult &= !pattern.get().contains("[-R]");
                }
                if (showResult) {
                    appendResult(result);
                }
            }
            end();

            return findWrapper(result);
        };

        // create proxy
        try {
            T wrapper = (T) factory.create(types, typesValues, handler);
            wrappers.put(wrapper, delegate);
            return wrapper;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getArgs(Object[] args, Pending pending) {
        if (pending.enabled()) {
            if (!pending.hasValue()) {
                return new Object[0];
            }

            return new Object[]{pending.value()};
        }

        return args;
    }

    private void unwrapAll(Object[] args) {
        for (int index = 0; index < args.length; index++) {
            Object arg = args[index];
            if (wrappers.containsKey(arg)) {
                args[index] = wrappers.get(arg);
            }
        }
    }

    private <T> void prolongLastCall(T delegate) {
        if (caller == null) {
            return;
        }

        if (wrappers.containsValue(delegate)
                && wrappers.getKey(delegate) != caller.wrapper)
        {
            caller = null;
            return;
        }

        addCall(caller.name, caller.args);
    }

    private Object findWrapper(Object object) {
        if (object == null) {
            return null;
        }
        if (wrappers.containsKey(object)) {
            return object;
        }
        if (wrappers.containsValue(object)) {
            Object result = wrappers.getKey(object);
            if (caller != null && caller.wrapper != result) {
                caller = new Caller(caller.name, result);
            }
            return result;
        }
        return object;
    }

    private Optional<String> findFirst(Method method, List<String> list) {
        return list.stream()
                .filter(it -> {
                    if (it.startsWith("[")) {
                        it = it.substring(it.lastIndexOf("]") + 1);
                    }

                    if (it.contains(":")) {
                        return it.equals(methodName(method));
                    }

                    return it.equals(method.getName());
                })
                .findFirst();
    }

    private String methodName(Method method) {
        return String.format("%s:%s",
                method.getReturnType().getSimpleName(),
                method.getName());
    }

    public class EventsWrapper extends EventsListenersAssert {

        private final EventsListenersAssert events;

        public EventsWrapper(EventsListenersAssert events) {
            super(null, null);    // fake
            this.events = events;
        }

        @Override
        public void verifyNoEvents(Integer... indexes) {
            appendCall(".verifyNoEvents", Arrays.asList(indexes));
            end();

            try {
                events.verifyNoEvents(indexes);
            } catch (ComparisonFailure failure) {
                verifyAllEvents(indexes);

                if (callRealAssert) {
                    throw failure;
                }
           }
        }

        private String verifyAllEvents(Integer[] indexes) {
            String actual = events.getEvents(indexes);
            appendCall(".verifyAllEvents", actual, Arrays.asList(indexes));
            end();

            return actual;
        }

        @Override
        public void verifyAllEvents(String expected, Integer... indexes) {
            String actual = verifyAllEvents(indexes);

            if (callRealAssert) {
                assertEquals(expected, actual);
            }
        }
    }

    @Override
    public EventsListenersAssert events() {
        addCall("events");

        return new EventsWrapper(super.events());
    }

}