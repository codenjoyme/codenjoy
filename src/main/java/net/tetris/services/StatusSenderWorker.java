package net.tetris.services;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class StatusSenderWorker implements SenderWorker {

    private String playerName;
    private String callbackUrl;
    private GameWorker gameWorker;
    private PlayerConsole screen;
    private DefaultHttpClient httpClient;

    public StatusSenderWorker(String playerName, String callbackUrl, GameWorker gameWorker, PlayerConsole screen) {
        this.playerName = playerName;
        this.callbackUrl = callbackUrl;
        this.gameWorker = gameWorker;
        this.screen = screen;
        httpClient = new DefaultHttpClient();
        
    }

    @Override
    public void sendStatusToPlayer() {
        HttpGet get = new HttpGet(callbackUrl+"?test=test");
        BasicResponseHandler responseHandler = new BasicResponseHandler();

        try {
            String response = httpClient.execute(get, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();//TODO
        }
    }
}
