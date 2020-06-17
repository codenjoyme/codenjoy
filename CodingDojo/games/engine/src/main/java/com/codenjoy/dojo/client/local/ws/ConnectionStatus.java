package com.codenjoy.dojo.client.local.ws;

import com.codenjoy.dojo.client.Solver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.java_websocket.WebSocket;

@Getter
@Setter
public class ConnectionStatus {

    private Object wait = new Object();

    private WebSocket socket;
    private Solver solver;
    private String action;
    private String board;

    public ConnectionStatus(WebSocket socket) {
        this.socket = socket;
    }

    public void waitNotify() {
        synchronized (wait) {
            wait.notify();

            try {
                wait.wait();
            } catch (InterruptedException e) {
                // случится, если что-то прервет Thread
            }
        }
    }

    public String getAction() {
        waitNotify();

        return action;
    }

    public String getBoard() {
        waitNotify();

        return board;
    }
}
