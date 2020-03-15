package com.codenjoy.dojo.web.rest;

import org.junit.After;
import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;


import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.fail;

public class SmartAssert {
    
    @Test
    public void test() {
        q1();
        q2();
        assertEquals("z\n3", "3\nz");
    }

    private void q2() {
        assertEquals("z\n2", "2\nz");
    }

    private void q1() {
        q3();
    }

    private void q3() {
        assertEquals("z", "1");
    }

    @After
    public void checkAsserts() {
        checkResult();
    }
    
    private static class Failure {
        private String message;
        private List<StackTraceElement> where;

        private Failure(String expected, String actual) {
            this.message = new ComparisonFailure("", expected, actual).getMessage();
            where = getLines(4, 10);
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
            return "org.junit.ComparisonFailure: " + message + "\n"
                    + where.stream()
                        .map(s -> "\t" + s + "\n")
                        .reduce("", (left, right) -> left + right);
        }
    }

    private List<Failure> failures = new LinkedList<>();

    private void assertEquals(Object expected, Object actual) {
        try {
            Assert.assertEquals(expected, actual);
        } catch (AssertionError e) {
            failures.add(new Failure(expected.toString(), actual.toString()));
        }
    }

    private void checkResult() {
        if (!failures.isEmpty()) {
            failures.forEach(failure ->
                    System.err.println(failure));
            failures.clear();
            fail("There are errors");
        }
    }
}
