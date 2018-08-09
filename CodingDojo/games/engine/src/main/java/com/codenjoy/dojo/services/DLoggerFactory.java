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



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Oleksandr_Baglai on 2017-09-06.
 */
public class DLoggerFactory {

    public static final String DEBUG_KEY = "debug";
    static Map<String, Boolean> settings = new ConcurrentHashMap<>();

    public static Logger getLogger(Class<?> clazz, boolean... sout) {
        boolean printToConsole = sout != null && sout.length == 1 && sout[0];
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new Logger() { // TODO to use DuckTyping
            @Override
            public String getName() {
                return logger.getName();
            }

            @Override
            public boolean isTraceEnabled() {
                return logger.isTraceEnabled();
            }

            @Override
            public void trace(String s) {
                logger.trace(s);
            }

            @Override
            public void trace(String s, Object o) {
                logger.trace(s, o);
            }

            @Override
            public void trace(String s, Object o, Object o1) {
                logger.trace(s, o, o1);
            }

            @Override
            public void trace(String s, Object... objects) {
                logger.trace(s, objects);
            }

            @Override
            public void trace(String s, Throwable throwable) {
                logger.trace(s, throwable);
            }

            @Override
            public boolean isTraceEnabled(Marker marker) {
                return logger.isTraceEnabled(marker);
            }

            @Override
            public void trace(Marker marker, String s) {
                logger.trace(marker, s);
            }

            @Override
            public void trace(Marker marker, String s, Object o) {
                logger.trace(marker, s, o);
            }

            @Override
            public void trace(Marker marker, String s, Object o, Object o1) {
                logger.trace(marker, s, o, o1);
            }

            @Override
            public void trace(Marker marker, String s, Object... objects) {
                logger.trace(marker, s, objects);
            }

            @Override
            public void trace(Marker marker, String s, Throwable throwable) {
                logger.trace(marker, s, throwable);
            }

            @Override
            public boolean isDebugEnabled() {
                return printToConsole || settings.containsKey(DEBUG_KEY);
            }

            @Override
            public void debug(String s) {
                if (printToConsole) {
                    System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n");
                }
                logger.debug(s);
            }

            @Override
            public void debug(String s, Object o) {
                if (printToConsole) {
                    System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", o);
                }
                logger.debug(s, o);
            }

            @Override
            public void debug(String s, Object o, Object o1) {
                if (printToConsole) {
                    System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", o, o1);
                }
                logger.debug(s, o, o1);
            }

            @Override
            public void debug(String s, Object... objects) {
                if (printToConsole) {
                    System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", objects);
                }
                logger.debug(s, objects);
            }

            @Override
            public void debug(String s, Throwable throwable) {
                if (printToConsole) {
                    System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", throwable);
                }
                logger.debug(s, throwable);
            }

            @Override
            public boolean isDebugEnabled(Marker marker) {
                return (isDebugEnabled() && logger.isDebugEnabled(marker));
            }

            @Override
            public void debug(Marker marker, String s) {
                logger.debug(marker, s);
            }

            @Override
            public void debug(Marker marker, String s, Object o) {
                logger.debug(marker, s, o);
            }

            @Override
            public void debug(Marker marker, String s, Object o, Object o1) {
                logger.debug(marker, s, o, o1);
            }

            @Override
            public void debug(Marker marker, String s, Object... objects) {
                logger.debug(marker, s, objects);
            }

            @Override
            public void debug(Marker marker, String s, Throwable throwable) {
                logger.debug(marker, s, throwable);
            }

            @Override
            public boolean isInfoEnabled() {
                return logger.isInfoEnabled();
            }

            @Override
            public void info(String s) {
                logger.info(s);
            }

            @Override
            public void info(String s, Object o) {
                logger.info(s, o);
            }

            @Override
            public void info(String s, Object o, Object o1) {
                logger.info(s, o, o1);
            }

            @Override
            public void info(String s, Object... objects) {
                logger.info(s, objects);
            }

            @Override
            public void info(String s, Throwable throwable) {
                logger.info(s, throwable);
            }

            @Override
            public boolean isInfoEnabled(Marker marker) {
                return logger.isInfoEnabled(marker);
            }

            @Override
            public void info(Marker marker, String s) {
                logger.info(marker, s);
            }

            @Override
            public void info(Marker marker, String s, Object o) {
                logger.info(marker, s, o);
            }

            @Override
            public void info(Marker marker, String s, Object o, Object o1) {
                logger.info(marker, s, o, o1);
            }

            @Override
            public void info(Marker marker, String s, Object... objects) {
                logger.info(marker, s, objects);
            }

            @Override
            public void info(Marker marker, String s, Throwable throwable) {
                logger.info(marker, s, throwable);
            }

            @Override
            public boolean isWarnEnabled() {
                return logger.isWarnEnabled();
            }

            @Override
            public void warn(String s) {
                logger.warn(s);
            }

            @Override
            public void warn(String s, Object o) {
                logger.warn(s, o);
            }

            @Override
            public void warn(String s, Object o, Object o1) {
                logger.warn(s, o, o1);
            }

            @Override
            public void warn(String s, Object... objects) {
                logger.warn(s, objects);
            }

            @Override
            public void warn(String s, Throwable throwable) {
                logger.warn(s, throwable);
            }

            @Override
            public boolean isWarnEnabled(Marker marker) {
                return logger.isWarnEnabled(marker);
            }

            @Override
            public void warn(Marker marker, String s) {
                logger.warn(marker, s);
            }

            @Override
            public void warn(Marker marker, String s, Object o) {
                logger.warn(marker, s, o);
            }

            @Override
            public void warn(Marker marker, String s, Object o, Object o1) {
                logger.warn(marker, s, o, o1);
            }

            @Override
            public void warn(Marker marker, String s, Object... objects) {
                logger.warn(marker, s, objects);
            }

            @Override
            public void warn(Marker marker, String s, Throwable throwable) {
                logger.warn(marker, s, throwable);
            }

            @Override
            public boolean isErrorEnabled() {
                return logger.isErrorEnabled();
            }

            @Override
            public void error(String s) {
                logger.error(s);
            }

            @Override
            public void error(String s, Object o) {
                logger.error(s, o);
            }

            @Override
            public void error(String s, Object o, Object o1) {
                logger.error(s, o, o1);
            }

            @Override
            public void error(String s, Object... objects) {
                logger.error(s, objects);
            }

            @Override
            public void error(String s, Throwable throwable) {
                logger.error(s, throwable);
            }

            @Override
            public boolean isErrorEnabled(Marker marker) {
                return logger.isErrorEnabled(marker);
            }

            @Override
            public void error(Marker marker, String s) {
                logger.error(marker, s);
            }

            @Override
            public void error(Marker marker, String s, Object o) {
                logger.error(marker, s, o);
            }

            @Override
            public void error(Marker marker, String s, Object o, Object o1) {
                logger.error(marker, s, o, o1);
            }

            @Override
            public void error(Marker marker, String s, Object... objects) {
                logger.error(marker, s, objects);
            }

            @Override
            public void error(Marker marker, String s, Throwable throwable) {
                logger.error(marker, s, throwable);
            }
        };
    }
}
