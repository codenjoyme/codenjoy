package com.codenjoy.dojo.transport.screen;

import com.codenjoy.dojo.services.PlayerServiceImpl;
import org.apache.commons.lang.StringUtils;

import javax.servlet.AsyncContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlayerScreenUpdateRequest extends UpdateRequest {
    private Set<String> playersToUpdate;
    private boolean forAllPlayers;

    public PlayerScreenUpdateRequest(AsyncContext asyncContext, boolean forAllPlayers, Set<String> playersToUpdate) {
        super(asyncContext);
        this.forAllPlayers = forAllPlayers;
        this.playersToUpdate = playersToUpdate;
    }

    public PlayerScreenUpdateRequest(AsyncContext asyncContext, String... playersToUpdate) {
        this(asyncContext, false, new HashSet<String>(Arrays.asList(playersToUpdate)));
    }

    public Set<String> getPlayersToUpdate() {
        return playersToUpdate;
    }

    public boolean isForAllPlayers() {
        return forAllPlayers;
    }

    @Override
    public String toString() {
        return "PlayerScreenUpdateRequest{" +
                "asyncContext=" + getAsyncContext() +
                ", playersToUpdate=" + StringUtils.join(playersToUpdate, ",") +
                ", forAllPlayers=" + forAllPlayers +
                '}';
    }

    @Override
    public boolean isApplicableFor(ScreenRecipient recipient) {
        return isForAllPlayers() || getPlayersToUpdate().contains(recipient.getName())
                // TODO:1 вот это очень большой хак, надо придумать другой способ отправки
                || recipient.getName().equals(PlayerServiceImpl.CHAT);
    }
}
