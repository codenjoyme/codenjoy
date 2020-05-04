package com.codenjoy.dojo.stuff;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;


import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.fail;

public class SmartAssert extends Runner {

    public static int STACK_TRACE_COUNT = 10;
    
    private Class test;
    
    private static Map<String, List<Failure>> failures = new ConcurrentHashMap<>();

    public SmartAssert(Class test) {
        super();
        this.test = test;
    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(test, 
                "Do not panic! You have performed wonders in this... colourful Universe.");
    }

    @Override
    @SneakyThrows
    public void run(RunNotifier notifier) {
        System.out.println("running the tests from SmartAssert: " + test);
        Object testObject = test.newInstance();
        for (Method method : test.getMethods()) {
            if (!method.isAnnotationPresent(Test.class)) continue;

            notifier.fireTestStarted(description(method));

            method.invoke(testObject);
            checkResult(failures(test.getName()));

            notifier.fireTestFinished(description(method));
        }
    }

    private Description description(Method method) {
        return Description.createTestDescription(test, method.getName());
    }

    private static class Failure {

        private String actual;
        private String expected;
        private String message;
        private List<StackTraceElement> where;

        private Failure(String expected, String actual) {
            this.expected = expected;
            this.actual = actual;
            this.message = new ComparisonFailure("", expected, actual).getMessage();
            where = getLines(STACK_TRACE_COUNT);
        }

        private List<StackTraceElement> getLines(int count) {
            StackTraceElement[] stackTrace = stackTrace();
            String caller = getCaller().getClassName();
            
            return new LinkedList<StackTraceElement>(){{
                for (int i = 0; i < stackTrace.length; i++) {
                    if (size() >= count) break;
                    
                    StackTraceElement element = stackTrace[i];
                    if (element.getClassName().equals(caller)) {
                        add(element);
                    }
                }
            }};
        }
        
        @Override
        public String toString() {
            // TODO почему-то тут idea не полхватывает expected: but was:
            return  "org.junit.ComparisonFailure: " + message + "\n" +
                    "\t\texpected: " + expected + "\n" +
                    "\t\tactual:   " + actual + "\n" +
                        where.stream()
                            .map(s -> "\t" + s + "\n")
                            .reduce("", (left, right) -> left + right);
        }
    }

    private static StackTraceElement[] stackTrace() {
        Exception exception = new Exception();
        return exception.getStackTrace();
    }

    private static StackTraceElement getCaller() {
        StackTraceElement[] elements = stackTrace();
        for (int i = 0; i < elements.length; i++) {
            StackTraceElement element = elements[i];
            if (!element.getClassName().equals(SmartAssert.class.getName()) &&
                    !element.getClassName().contains(SmartAssert.class.getSimpleName() + "$")) {
                return element;
            }             
        }
        throw new RuntimeException();
    }

    public static void assertEquals(Object expected, Object actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (AssertionError e) {
            failures().add(new Failure(toString(expected), toString(actual)));
        }
    }

    private static String toString(Object value) {
        return (value == null) ? null : value.toString();
    }

    private static void checkResult(List<Failure> list) {
        if (list.isEmpty()) return;
        
        list.forEach(System.err::println);
        list.clear();
        fail("There are errors");
    }
    
    public static void checkResult() {
        checkResult(failures());
    }

    private static List<Failure> failures() {
        return failures(getCaller().getClassName());    
    }
    
    private static List<Failure> failures(String caller) {
        if (!failures.containsKey(caller)) {
            failures.put(caller, new LinkedList<>());
        }
        return failures.get(caller);
    }
}
