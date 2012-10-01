package com.globallogic.snake.web.controller;

import javax.servlet.AsyncContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 6:51 AM
 */
public class UpdateRequest {
    private AsyncContext asyncContext;
    private Set<String> playersToUpdate;
    private boolean forAllPlayers;

    public UpdateRequest(AsyncContext asyncContext, boolean forAllPlayers, Set<String> playersToUpdate) {
        this.asyncContext = asyncContext;
        this.forAllPlayers = forAllPlayers;
        this.playersToUpdate = playersToUpdate;
    }

    public UpdateRequest(AsyncContext asyncContext, String... playersToUpdate) {
        this(asyncContext, false, new HashSet<>(Arrays.asList(playersToUpdate)));
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public Set<String> getPlayersToUpdate() {
        return playersToUpdate;
    }

    public boolean isForAllPlayers() {
        return forAllPlayers;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "asyncContext=" + asyncContext +
                ", playersToUpdate=" + playersToUpdate +
                ", forAllPlayers=" + forAllPlayers +
                '}';
    }
}
