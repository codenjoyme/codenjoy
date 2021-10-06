package com.codenjoy.dojo.sample.model.check;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

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

public class WrapperManager {

    private int deep;
    private Pending pending;
    private BidiMap<Object, Object> wrappers;
    private Caller caller;
    private List<String> messages;

    public WrapperManager() {
        messages = new LinkedList<>();
        wrappers = new DualHashBidiMap<>();
        deep = 0;
        pending = new Pending();
    }

    public String messages() {
        return messages.stream()
                .collect(joining("\n"));
    }

    public void addCall(String method, Object... parameters) {
        call(method, false, parameters);
    }

    public void appendCall(String method, Object... parameters) {
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

    public void end() {
        if (!pending.enabled()) {
            deep--;
        }
    }

    public <T> T objectSpy(T delegate, boolean include, String... methods) {
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

        // methods handler
        MethodHandler handler = (self, method, proceed, args) -> {
            boolean process = isProcess(excluded, included, method);

            if (process) {
                Pending last = pending.disable();
                prolongLastCall(delegate);
                appendCall("." + method.getName(), getArgs(args, last));
            }

            unwrapAll(args);
            Object result = method.invoke(delegate, args);

            if (process) {
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
            }

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

    private boolean isProcess(List<String> excluded, List<String> included, Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }

        if (included.isEmpty()) {
            return findFirst(method, excluded).isEmpty();
        } else {
            return findFirst(method, included).isPresent();
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
                && wrappers.getKey(delegate) != caller.wrapper())
        {
            caller = null;
            return;
        }

        addCall(caller.name(), caller.args());
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
            if (caller != null && caller.wrapper() != result) {
                caller(caller.name(), result);
            }
            return result;
        }
        return object;
    }

    public void caller(String name, Object wrapper, Object... args) {
        if (!pending.enabled()) {
            caller = new Caller(name, wrapper, args);
        } else {
            addCall(name, args);
        }
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

    public void pending(boolean enabled) {
        pending.enabled(enabled);
    }

    public List<String> messagesList() {
        return messages;
    }
}
