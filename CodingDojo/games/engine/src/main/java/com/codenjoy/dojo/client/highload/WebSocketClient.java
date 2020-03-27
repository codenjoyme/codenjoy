package com.codenjoy.dojo.client.highload;

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


import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.client.UrlParser;
import lombok.SneakyThrows;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class WebSocketClient implements Closeable {

    public static final String WS_URI_PATTERN = "%s://%s/%s/ws?user=%s&code=%s";

    private Session session;
    private org.eclipse.jetty.websocket.client.WebSocketClient client;
    private URI uri;
    private Runner.MySocket socket;

    public WebSocketClient(String url, Runner.MySocket socket) {
        this.socket = socket;
        UrlParser parser = new UrlParser(url);
        try {
            uri = getUri(parser.protocol, parser.server,
                    parser.context, parser.userName, parser.code);

            client = createClient();
            client.start();

            tryToConnect();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> this.close()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private static URI getUri(String protocol, String server, String context, String userName, String code) {
       return new URI(String.format(WS_URI_PATTERN, protocol, server, context, userName, code));
    }

    private org.eclipse.jetty.websocket.client.WebSocketClient createClient() {
        if (UrlParser.WSS_PROTOCOL.equals(uri.getScheme())) {
            SslContextFactory ssl = new SslContextFactory(true);
            ssl.setValidateCerts(false);
            return new org.eclipse.jetty.websocket.client.WebSocketClient(ssl);
        }

        if (UrlParser.WS_PROTOCOL.equals(uri.getScheme())) {
            return new org.eclipse.jetty.websocket.client.WebSocketClient();
        }

        throw new UnsupportedOperationException("Unsupported WebSocket protocol: " + uri.getScheme());
    }

    @Override
    public void close() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
            client = null;
        } catch (Exception e) {
            print(e);
        }
    }

    private void tryToConnect() throws Exception {
        if (session != null) {
            session.close();
        }

        session = client.connect(socket, uri)
                .get(5000, TimeUnit.MILLISECONDS);
    }

    public static void print(String message) {
        System.out.println(message);
    }

    private void print(Exception e) {
        e.printStackTrace(System.out);
    }

}
