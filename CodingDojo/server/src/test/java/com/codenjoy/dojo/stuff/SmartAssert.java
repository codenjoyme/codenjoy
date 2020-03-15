package com.codenjoy.dojo.stuff;

import org.junit.After;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;


import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

public class SmartAssert extends Runner {

    public static final int STACK_TRACE_COUNT = 10;
    public static final int REAL_ASSERT_DEPTH = 4;
    
    private Class test;
    
    // TODO тут надо решить со статикой
    private static List<Failure> failures = new LinkedList<>();

    public SmartAssert(Class test) {
        super();
        this.test = test;
    }

    @Override
    public Description getDescription() {
        return Description.createTestDescription(test, "This is SmartAssertRunner");
    }

    @Override
    public void run(RunNotifier notifier) {
        System.out.println("running the tests from SmartAssert: " + test);
        try {
            Object testObject = test.newInstance();
            for (Method method : test.getMethods()) {
                if (!method.isAnnotationPresent(Test.class)) continue;
                
                notifier.fireTestStarted(description(method));
                
                method.invoke(testObject);
                checkResult();
                
                notifier.fireTestFinished(description(method));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Description description(Method method) {
        return Description.createTestDescription(test, method.getName());
    }

    private static class Failure {
        private String message;
        private List<StackTraceElement> where;


        private Failure(String expected, String actual) {
            this.message = new ComparisonFailure("", expected, actual).getMessage();
            where = getLines(REAL_ASSERT_DEPTH, STACK_TRACE_COUNT);
        }

        private List<StackTraceElement> getLines(int depth, int count) {
            Exception exception = new Exception();
            StackTraceElement[] stackTrace = exception.getStackTrace();
            return new LinkedList<StackTraceElement>(){{
                add(stackTrace[depth]);
                for (int i = 1; i < count; i++) {
                    StackTraceElement element1 = stackTrace[depth];
                    StackTraceElement element2 = stackTrace[depth + i];
                    if (element1.getClassName().equals(element2.getClassName())) {
                        add(element2);
                    } else {
                        break;
                    }
                }
            }};
        }
        
        @Override
        public String toString() {
            // TODO почему-то тут idea не полхватывает expected: but was:  
            return "org.junit.ComparisonFailure: " + message + "\n"
                    + where.stream()
                        .map(s -> "\t" + s + "\n")
                        .reduce("", (left, right) -> left + right);
        }
    }

    public static void assertEquals(Object expected, Object actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (AssertionError e) {
            failures.add(new Failure(expected.toString(), actual.toString()));
        }
    }

    public static void checkResult() {
        if (!failures.isEmpty()) {
            failures.forEach(failure ->
                    System.err.println(failure));
            failures.clear();
            fail("There are errors");
        }
    }
}
