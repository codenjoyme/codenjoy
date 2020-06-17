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

    private boolean working;
    private WebSocket socket;
    private Solver solver;
    private String action;
    private String board;

    public ConnectionStatus(WebSocket socket) {
        working = true;
        this.socket = socket;
    }

    public void waitNotify(String name) {
        if (!working) {
            System.out.println("Skip, not working: " + name + socket.hashCode());
            return;
        }
        System.out.println("Entering into sync: " + name + socket.hashCode());
        synchronized (wait) {
            System.out.println("Socket notify:   " + name + socket.hashCode());
            wait.notify();
            System.out.println("Socket notified: " + name + socket.hashCode());

            try {
                System.out.println("Socket waiting:  " + name + socket.hashCode());
                wait.wait();
                System.out.println("Socket released: " + name + socket.hashCode());
            } catch (InterruptedException e) {
                // случится, если что-то прервет Thread
            }
        }
        System.out.println("Exited from sync: " + name + socket.hashCode());
    }

    public String getAction() {
        waitNotify("getAction");

        return action;
    }

    public String getBoard() {
        waitNotify("getBoard");

        return board;
    }

    public void release() {
        working = false;
        synchronized (wait) {
            wait.notify();
        }
    }
}
