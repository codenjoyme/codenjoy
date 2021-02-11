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



import lombok.experimental.Delegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Oleksandr_Baglai on 2017-09-06.
 */
// TODO удалить, поскольку можно теперь по другому включать DEBUG сообщения
public class DLoggerFactory {

    public static final String DEBUG_KEY = "debug";
    static Map<String, Boolean> settings = new ConcurrentHashMap<>();

    private static class DebugLogger implements Logger {

        @Delegate(excludes=DebugLog.class)
        private final Logger logger;

        private final boolean printToConsole;

        public DebugLogger(Class<?> clazz, boolean... sout) {
            printToConsole = sout != null && sout.length == 1 && sout[0];
            logger = LoggerFactory.getLogger(clazz);
        }

        private interface DebugLog {
            
            boolean isDebugEnabled();
            void debug(String s);
            void debug(String s, Object o);
            void debug(String s, Object o, Object o1);
            void debug(String s, Object... objects);
            void debug(String s, Throwable throwable);
        }

        public boolean isDebugEnabled() {
            return printToConsole || settings.containsKey(DEBUG_KEY);
        }

        public void debug(String s) {
            if (printToConsole) {
                System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n");
            }
            logger.debug(s);
        }

        public void debug(String s, Object o) {
            if (printToConsole) {
                System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", o);
            }
            logger.debug(s, o);
        }

        public void debug(String s, Object o, Object o1) {
            if (printToConsole) {
                System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", o, o1);
            }
            logger.debug(s, o, o1);
        }

        public void debug(String s, Object... objects) {
            if (printToConsole) {
                System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", objects);
            }
            logger.debug(s, objects);
        }

        public void debug(String s, Throwable throwable) {
            if (printToConsole) {
                System.out.printf(s.replaceAll("\\{\\}", "%s") + "\n", throwable);
            }
            logger.debug(s, throwable);
        }
    }

    public static Logger getLogger(Class<?> clazz, boolean... sout) {
        return new DebugLogger(clazz, sout);
    }
}
