package net.tetris.online.web.controller;

import net.tetris.services.Player;
import net.tetris.services.PlayerScreenUpdateRequest;
import net.tetris.services.UpdateRequest;

import javax.servlet.AsyncContext;

/**
 * User: serhiy.zelenin
 * Date: 11/12/12
 * Time: 5:56 PM
 */
public class ReplayUpdateRequest extends UpdateRequest {
    private String replayId;

    public ReplayUpdateRequest(AsyncContext asyncContext, String replayId) {
        super(asyncContext);
        this.replayId = replayId;
    }

    @Override
    public boolean isApplicableFor(Player player) {
        //Need to perform refactoring and use ReplayRequest instead of setting replay Id into callback url
        return player.getCallbackUrl().equals(replayId);
    }

    @Override
    public String toString() {
        return "ReplayUpdateRequest{" +
                "replayId='" + replayId + '\'' +
                '}';
    }
}
