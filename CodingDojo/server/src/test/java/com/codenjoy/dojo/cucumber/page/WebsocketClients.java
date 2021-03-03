package com.codenjoy.dojo.cucumber.page;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.mocks.FakeGameType;
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
public class WebsocketClients {

    @Autowired
    private Server server;

    @Autowired
    private GameService gameService;

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