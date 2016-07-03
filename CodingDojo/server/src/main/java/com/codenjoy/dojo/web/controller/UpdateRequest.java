package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import javax.servlet.AsyncContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
        this(asyncContext, false, new HashSet<String>(Arrays.asList(playersToUpdate)));
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
