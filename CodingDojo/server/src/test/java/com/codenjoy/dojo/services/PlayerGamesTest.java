package com.codenjoy.dojo.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;

public class PlayerGamesTest {

    private PlayerGames playerGames;
    private Player player;
    private Game game;
    private PlayerController controller;
    private LazyJoystick lazyJoystick;
    private Joystick joystick;
    private PlayerSpy playerSpy;
    private Statistics statistics;

    @Before
    public void setUp() throws Exception {
        player = createPlayer("game", "player");

        game = mock(Game.class);
        joystick = mock(Joystick.class);
        when(game.getJoystick()).thenReturn(joystick);

        controller = mock(PlayerController.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                lazyJoystick = (LazyJoystick)invocation.getArguments()[1];
                return null;
            }
        }).when(controller).registerPlayerTransport(eq(player), any(LazyJoystick.class));


        statistics = mock(Statistics.class);
        playerSpy = mock(PlayerSpy.class);
        when(statistics.newPlayer(any(Player.class))).thenReturn(playerSpy);
        playerGames = new PlayerGames(statistics);

        playerGames.add(player, game, controller);
    }

    @Test
    public void testRemove() throws Exception {
        assertFalse(playerGames.isEmpty());
        assertEquals(1, playerGames.size());
        PlayerGame playerGame = playerGames.get(player.getName());

        playerGames.remove(player);

        assertTrue(playerGames.isEmpty());
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame);
    }

    @Test
    public void testGet() throws Exception {
        assertSame(NullPlayerGame.INSTANCE, playerGames.get("bla"));

        PlayerGame playerGame = playerGames.get(player.getName());

        assertSame(player, playerGame.getPlayer());
        assertSame(game, playerGame.getGame());
        assertSame(controller, playerGame.getController());

    }

    @Test
    public void testAdd() throws Exception {
        Player otherPlayer = addOtherPlayer();

        assertFalse(playerGames.isEmpty());
        assertEquals(2, playerGames.size());

        PlayerGame playerGame = playerGames.get(otherPlayer.getName());

        assertSame(otherPlayer, playerGame.getPlayer());
    }

    @Test
    public void testIterator() throws Exception {
        Player otherPlayer = addOtherPlayer();

        Iterator<PlayerGame> iterator = playerGames.iterator();
        assertTrue(iterator.hasNext());
        assertSame(player, iterator.next().getPlayer());

        assertTrue(iterator.hasNext());
        assertSame(otherPlayer, iterator.next().getPlayer());

        assertFalse(iterator.hasNext());
    }

    private Player addOtherPlayer(String game) {
        Player otherPlayer = createPlayer(game, "player" + Calendar.getInstance().getTimeInMillis());
        playerGames.add(otherPlayer, mock(Game.class), mock(PlayerController.class));
        return otherPlayer;
    }

    private Player createPlayer(String game, String name) {
        GameService gameService = mock(GameService.class);
        GameType gameType = mock(GameType.class);
        PlayerScores scores = mock(PlayerScores.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);
        when(gameType.name()).thenReturn(game);
        when(gameService.getGame(anyString())).thenReturn(gameType);

        return new Player(name, "url", gameType, scores, mock(Information.class), Protocol.WS);
    }

    @Test
    public void testPlayers() throws Exception {
        Player otherPlayer = addOtherPlayer();

        List<Player> players = playerGames.players();

        assertSame(player, players.get(0));
        assertSame(otherPlayer, players.get(1));
        assertEquals(2, players.size());
    }

    private Player addOtherPlayer() {
        return addOtherPlayer("game");
    }

    @Test
    public void testClear() throws Exception {
        Player player2 = addOtherPlayer();
        Player player3 = addOtherPlayer();

        PlayerGame playerGame1 = playerGames.get(player.getName());
        PlayerGame playerGame2 = playerGames.get(player2.getName());
        PlayerGame playerGame3 = playerGames.get(player3.getName());

        assertEquals(3, playerGames.size());

        playerGames.clear();

        assertEquals(0, playerGames.size());

        verifyRemove(playerGame1);
        verifyRemove(playerGame2);
        verifyRemove(playerGame3);
    }

    private void verifyRemove(PlayerGame playerGame2) {
        verify(playerGame2.getGame()).destroy();
        verify(playerGame2.getController()).unregisterPlayerTransport(playerGame2.getPlayer());
    }

    @Test
    public void testGetGameTypes() {
        Player player2 = addOtherPlayer("game2");
        playerGames.add(player2, mock(Game.class), mock(PlayerController.class));

        List<GameType> gameTypes = playerGames.getGameTypes();

        assertEquals(2, gameTypes.size());
        assertEquals("game", gameTypes.get(0).name());
        assertEquals("game2", gameTypes.get(1).name());
    }

    @Test
    public void shouldTickLazyJoystickWhenTick() {
        // given
        lazyJoystick.right();

        // when
        playerGames.tick();

        // then
        verify(joystick).right();
    }

    @Test
    public void shouldQuietTickPlayerSpyWhenTick() {
        // given
        lazyJoystick.right();
        doThrow(new RuntimeException()).when(playerSpy).act();

        // when
        playerGames.tick();

        // then
        verify(playerSpy).act();
    }

    @Test
    public void shouldTickPlayerSpyWhenTick() {
        // given
        lazyJoystick.right();

        // when
        playerGames.tick();

        // then
        verify(playerSpy).act();
    }

    @Test
    public void shouldTickStatisticsWhenTick() {
        // when
        playerGames.tick();

        // then
        verify(statistics).tick();
    }

    @Test
    public void shouldQuietTickStatisticsWhenTick() {
        // given
        doThrow(new RuntimeException()).when(statistics).tick();

        // when
        playerGames.tick();

        // then
        verify(statistics).tick();
    }

    @Test
    public void shouldNewGameWhenGameOverWhenTick() {
        // given
        when(game.isGameOver()).thenReturn(true);

        // when
        playerGames.tick();

        // then
        verify(game).newGame();
    }

    @Test
    public void shouldQuietNewGameWhenGameOverWhenTick() {
        // given
        when(game.isGameOver()).thenReturn(true);
        doThrow(new RuntimeException()).when(game).newGame();

        // when
        playerGames.tick();

        // then
        verify(game).newGame();
    }

    @Test
      public void shouldTickGameWhenTickIfSingleGameType() {
        // given
        GameType gameType = playerGames.getGameTypes().get(0);
        when(gameType.isSingleBoard()).thenReturn(true);

        // when
        playerGames.tick();

        // then
        verify(game).tick();
    }

    @Test
    public void shouldTickGameWhenTickIfNotSingleGameType() {
        // given
        GameType gameType = playerGames.getGameTypes().get(0);
        when(gameType.isSingleBoard()).thenReturn(false);

        // when
        playerGames.tick();

        // then
        verify(game).tick();
    }

    @Test
    public void shouldNotRemovePlayerIfNoActive() {
        // given
        when(statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, PlayerGames.TICKS_FOR_REMOVE)).thenReturn(Arrays.asList(player));
        assertEquals(1, playerGames.size());

        // when
        playerGames.tick();

        // then
        assertEquals(1, playerGames.size());
    }


}
