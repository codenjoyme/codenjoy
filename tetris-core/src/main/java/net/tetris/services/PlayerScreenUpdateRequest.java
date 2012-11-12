package net.tetris.services;

import org.apache.commons.lang.StringUtils;

import javax.servlet.AsyncContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:29 PM
 */
public class PlayerScreenUpdateRequest extends UpdateRequest{
    private Set<String> playersToUpdate;
    private boolean forAllPlayers;

    public PlayerScreenUpdateRequest(AsyncContext asyncContext, boolean forAllPlayers, Set<String> playersToUpdate) {
        super(asyncContext);
        this.forAllPlayers = forAllPlayers;
        this.playersToUpdate = playersToUpdate;
    }

    public PlayerScreenUpdateRequest(AsyncContext asyncContext, String... playersToUpdate) {
        this(asyncContext, false, new HashSet<>(Arrays.asList(playersToUpdate)));
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
    public boolean isApplicableFor(Player player) {
        return isForAllPlayers() || getPlayersToUpdate().contains(player.getName());
    }
}
