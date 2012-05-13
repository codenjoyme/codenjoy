package net.tetris.web.controller;

import javax.servlet.AsyncContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:29 PM
 */
public class UpdateRequest {
    private AsyncContext asyncContext;
    private Set<String> playersToUpdate;

    public UpdateRequest(AsyncContext asyncContext, Set<String> playersToUpdate) {
        this.asyncContext = asyncContext;
        this.playersToUpdate = playersToUpdate;
    }

    public UpdateRequest(AsyncContext asyncContext, String... playersToUpdate) {
        this(asyncContext, new HashSet<>(Arrays.asList(playersToUpdate)));
    }

    public AsyncContext getAsyncContext() {
        return asyncContext;
    }

    public Set<String> getPlayersToUpdate() {
        return playersToUpdate;
    }
}
