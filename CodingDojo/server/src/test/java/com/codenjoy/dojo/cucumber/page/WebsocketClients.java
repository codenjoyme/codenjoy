package com.codenjoy.dojo.cucumber.page;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FakeGameType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.fest.reflect.core.Reflection;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@RequiredArgsConstructor
public class WebsocketClients {

    private final Server server;
    private final GameService gameService;

    private Map<String, WebSocketRunner> runners = new HashMap<>();

    public void cleanUp() {
        runners.values()
                .forEach(WebSocketRunner::close);
    }

    public void assertConnected(String game, String name, String url) {
        Solver solver = mock(Solver.class);
        answer(solver, "");
        WebSocketRunner runner = WebSocketRunner.runClient(null,
                server.endpoint() + url,
                solver,
                board(game));
        runners.put(name, runner);
    }

    private ClientBoard board(String game) {
        GameType gameType = gameService.getGameType(game);
        Class boardClass = gameType.getBoard();

        ClientBoard board = (ClientBoard) Reflection.constructor()
                .withParameterTypes(FakeGameType.class)
                .in(boardClass)
                .newInstance(gameType);

        return board;
    }

    @SneakyThrows
    public void assertRequestReceived(String name, String command, String board) {
        assertRequestReceived(name, command,
                values -> {
                    assertEquals(1, values.size());
                    assertEquals(board, values.getLast());
                });
    }

    @SneakyThrows
    public void assertRequestReceived(String name, String command,
                                      Consumer<Deque<String>> onReceived)
    {
        WebSocketRunner runner = runners.get(name);
        Solver solver = runner.solver();

        answer(solver, command);

        List<ClientBoard> clientBoards = getRequests(solver, 3);

        LinkedList<String> values = clientBoards.stream()
                .map(board -> ((AbstractBoard) board).boardAsString())
                .collect(Collectors.toCollection(LinkedList::new));

        onReceived.accept(values);

        reset(solver);
        answer(solver, "");
    }

    private List<ClientBoard> getRequests(Solver solver, int ticks) throws InterruptedException {
        ArgumentCaptor<ClientBoard> argument = ArgumentCaptor.forClass(ClientBoard.class);
        List<ClientBoard> values = new LinkedList<>();
        for (int tick = 0; tick < ticks; tick++) {
            try {
                verify(solver, atLeastOnce()).get(argument.capture());
                values.addAll(argument.getAllValues());
                values.remove(null); // иногда на старте пробивается null почему-то
            } catch (MockitoAssertionError e) {
                // do nothing
            }
            if (!values.isEmpty()) {
                break;
            }
            Thread.sleep(1000);
        }
        return values;
    }

    private void answer(Solver solver, String command) {
        when(solver.get(any(ClientBoard.class))).thenReturn(command);
    }

    @SneakyThrows
    public void assertRequestReceivedNothing(String name, String command) {
        assertRequestReceived(name, command,
                values -> assertEquals(0, values.size()));
    }
}
