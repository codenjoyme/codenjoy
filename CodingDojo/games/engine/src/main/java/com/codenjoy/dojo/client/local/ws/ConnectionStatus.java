package com.codenjoy.dojo.client.local.ws;

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

import com.codenjoy.dojo.client.Solver;
import org.java_websocket.WebSocket;

public class ConnectionStatus {

    private Object wait = new Object();

    private boolean working;
    private WebSocket socket;
    private Solver solver;
    private String action;

    public ConnectionStatus(WebSocket socket) {
        working = true;
        this.socket = socket;
    }

    public void waitNotify() {
        if (!working) {
            return;
        }
        synchronized (wait) {
            if (action == null) {
                try {
                    wait.wait();
                } catch (InterruptedException e) {
                    // случится, если что-то прервет Thread
                }
            }
        }
    }

    public String pullAction() {
        waitNotify();

        String result = action;
        action = null;
        return result;
    }

    public void setAction(String action) {
        synchronized (wait) {
            this.action = action;
            wait.notify();
        }
    }

    public WebSocket getSocket() {
        return socket;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

    public Solver getSolver() {
        return solver;
    }

    public void release() {
        working = false;
        synchronized (wait) {
            wait.notify();
        }
    }
}
