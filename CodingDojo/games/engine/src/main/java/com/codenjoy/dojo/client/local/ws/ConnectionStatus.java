package com.codenjoy.dojo.client.local.ws;

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
