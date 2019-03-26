package com.codenjoy.dojo.lunolet.client.swing;

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


import com.codenjoy.dojo.lunolet.client.Board;
import com.codenjoy.dojo.lunolet.client.YourSolver;
import com.codenjoy.dojo.services.RandomDice;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwingClient extends JFrame implements ActionListener {

    private static final String SERVER_AND_PORT = "127.0.0.1:8080";
    // this is your player id (you can get it from board page url after registration)
    private static final String USER_ID = "3edq63tw0bq4w4iem7nb";
    // you can get this code after registration on the server with your email
    // http://server-ip:8080/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890
    private static final String CODE = "1889919902398150091";

    private final JButton buttonConnect;
    private final JButton buttonClose;
    private final JTextArea textArea;
    private ImagePanel canvas;
    private Session session;
    private WebSocketClient cc;

    private Board board = new Board();
    private YourSolver solver = new YourSolver(new RandomDice());

    private String uri = "ws://127.0.0.1:8080/codenjoy-contest/ws";

    public SwingClient() {
        super("Lunolet Swing Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();

        BorderLayout layout = new BorderLayout();
        layout.setVgap(4);
        contentPane.setLayout(layout);

        canvas = new ImagePanel();
        java.awt.Dimension dimCanvas = new java.awt.Dimension(600, 400);
        canvas.setSize(dimCanvas);
        canvas.setMaximumSize(dimCanvas);
        canvas.setMinimumSize(dimCanvas);
        contentPane.add(canvas, BorderLayout.CENTER);

        textArea = new JTextArea(10, 50);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        contentPane.add(scrollPane, BorderLayout.PAGE_END);

        java.awt.Dimension d = new java.awt.Dimension(620, 640);
        setPreferredSize(d);
        setSize(d);

        JPanel panelCommands = new JPanel();
        contentPane.add(panelCommands, BorderLayout.PAGE_START);
        panelCommands.setLayout(new FlowLayout());
        //panelCommands.setPreferredSize(new Dimension(100, 100));

        buttonConnect = new JButton("Connect");
        buttonConnect.addActionListener(this);
        panelCommands.add(buttonConnect);

        buttonClose = new JButton("Close");
        buttonClose.addActionListener(this);
        buttonClose.setEnabled(false);
        panelCommands.add(buttonClose);

        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonConnect) {
            connect();
        } else if (e.getSource() == buttonClose) {
            disconnect();
        }
    }

    private void connect() {
        try {
            // TODO to use board url parsing like WebSocketRunner.runClient
            String server = String.format("ws://%s/codenjoy-contest/ws", SERVER_AND_PORT);
            URI uri = new URI(String.format("%s?user=%s&code=%s", server, USER_ID, CODE));

            if (session != null) {
                session.close();
            }

            cc = new WebSocketClient();
            cc.start();
            session = cc.connect(new ClientSocket(), uri).get(5000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            textArea.append(ex.toString());
        }
    }

    private void disconnect() {
        if (cc == null)
            return;
        try {
            cc.stop();
        } catch (Exception ex) {
            textArea.append(ex.toString());
        }
    }

    @WebSocket
    public class ClientSocket {
        final Pattern pattern = Pattern.compile("^board=(.*)$");

        @OnWebSocketConnect
        public void onConnect(Session session) {
            print("Opened connection " + session.toString());
            buttonConnect.setEnabled(false);
            buttonClose.setEnabled(true);
        }

        @OnWebSocketClose
        public void onClose(int closeCode, String message) {
            print("Closed with message: '" + message + "' and code: " + closeCode);
            buttonConnect.setEnabled(true);
            buttonClose.setEnabled(false);
        }

        @OnWebSocketError
        public void onError(Session session, Throwable reason) {
            print("Error with message: '" + reason.toString());
        }

        @OnWebSocketMessage
        public void onMessage(String data) {
            print("Received: " + data);
            try {
                Matcher matcher = pattern.matcher(data);
                if (!matcher.matches()) {
                    throw new RuntimeException("Error parsing data: " + data);
                }

                board.forString(matcher.group(1));
                //print("Board: " + board);
                canvas.drawBoard(board);

                String answer = solver.get(board);
                print("Answer: " + answer);

                session.getRemote().sendString(answer);
            } catch (Exception ex) {
                print(ex.toString());
            }
        }
    }

    private void print(String text) {
        textArea.append(text);
        textArea.append("\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        new SwingClient();
    }
}
