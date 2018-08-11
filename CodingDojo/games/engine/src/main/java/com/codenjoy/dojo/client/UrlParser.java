package com.codenjoy.dojo.client;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlParser {
    String server;
    String code;
    String userName;
    String context;

    public UrlParser(String uri) {
        try {
            URL url = new URL(uri);
            String[] queryParts = url.getQuery().split("=");
            String[] urlParts = url.getPath().split("\\/");
            if (urlParts.length != 5
                    || !urlParts[0].equals("")
                    || !urlParts[2].equals("board")
                    || !urlParts[3].equals("player")
                    || queryParts.length != 2
                    || !queryParts[0].equals("code"))
            {
                throw new IllegalArgumentException("Bad URL");
            }

            server = url.getHost() + portPart(url.getPort());
            code = queryParts[1];
            userName = urlParts[4];
            context = urlParts[1];
        } catch (MalformedURLException e) {
            throw new RuntimeException("Please set url in format " +
                    "'http://codenjoyDomainOrIP:8080/codenjoy-contest/" +
                    "board/player/your@email.com?code=12345678901234567890'",
                    e);
        }
    }

    private String portPart(int port) {
        return (port == -1) ? "" : (":" + port);
    }

    @Override
    public String toString() {
        return "UrlParser{" +
                "server='" + server + '\'' +
                ", context='" + context + '\'' +
                ", code='" + code + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
