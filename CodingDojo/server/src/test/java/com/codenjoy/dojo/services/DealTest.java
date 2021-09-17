package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.lock.LockedGame;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.nullobj.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class DealTest {

    private Player player;
    private Game game;
    private Deal deal;
    private GameType gameType;

    @Before
    public void setup() {
        gameType = PlayerTest.mockGameType("game");
        player = new Player("player", "url", gameType,
                NullPlayerScores.INSTANCE, NullInformationCollector.INSTANCE);

        setupGamePlayer();
        deal = new Deal(player, game, "room");
    }

    private void setupGamePlayer() {
        game = mock(Game.class);
        when(game.getPlayer()).thenReturn(mock(GamePlayer.class));
    }

    @Test
    public void testWorkWithJoystick() {
        // given
        Joystick joystick = deal.getJoystick();
        Joystick real = mock(Joystick.class);
        when(game.getJoystick()).thenReturn(real);

        // when
        joystick.act(1, 2, 3);
        joystick.down();
        deal.tick();

        // then
        InOrder inOrder = inOrder(real);
        inOrder.verify(real).act(1, 2, 3);
        inOrder.verify(real).down();
    }

    @Test
    public void testGetField() {
        // given
        GameField field = mock(GameField.class);
        when(game.getField()).thenReturn(field);

        // when then
        assertSame(field, deal.getField());
    }

    @Test
    public void getGetGameType() {
        // when then
        assertSame(gameType, deal.getGameType());
    }

    @Test
    public void testEquals_null() {
        // when then
        assertEquals(false, deal.equals(null));
    }

    @Test
    public void testEquals_withOtherObject() {
        // when then
        assertEquals(false, deal.equals(new Object()));
    }

    @Test
    public void testEquals_nullInstance() {
        // when then
        assertEquals(false, deal.equals(NullDeal.INSTANCE));
        assertEquals(true, NullDeal.INSTANCE.equals(NullDeal.INSTANCE));
        assertEquals(true, NullDeal.INSTANCE.equals(NullPlayer.INSTANCE));
    }
    @Test
    public void testEquals_withPlayer() {
        // given
        Player otherPlayer = new Player("other player", "other url", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformationCollector.INSTANCE);

        // when then
        assertEquals(false, deal.equals(otherPlayer));
        assertEquals(true, deal.equals(player));
    }

    @Test
    public void testEquals_dealWithPlayer() {
        // given
        GameType gameType = PlayerTest.mockGameType("game");
        Player otherPlayer = new Player("other player", "other url", gameType,
                NullPlayerScores.INSTANCE, NullInformationCollector.INSTANCE);
        Deal player = new Deal(otherPlayer, NullGame.INSTANCE, gameType.name());

        // when then
        assertEquals(false, deal.equals(player));
        assertEquals(true, deal.equals(deal));
    }

    @Test
    public void testEquals_room() {
        // when then
        assertEquals(true, deal.equals("room"));
        assertEquals(false, deal.equals("otherRoom"));
    }

    @Test
    public void testEquals_dealWithGame() {
        // given
        Game game = new LockedGame(new ReentrantReadWriteLock()).wrap(mock(Game.class));
        Deal player = new Deal(null, game, null);

        // when then
        assertEquals(false, deal.equals(player));
        assertEquals(false, player.equals(deal));
    }

    @Test
    public void testEquals_player() {
        // given
        Deal player = new Deal(null, game, null);
        Deal player2 = new Deal(null, mock(Game.class), null);

        // when then
        assertEquals(true, deal.equals(player));
        assertEquals(true, player.equals(deal));
        assertEquals(false, deal.equals(player2));
        assertEquals(false, player2.equals(deal));
    }

    @Test
    public void testEquals_field() {
        // given
        Game realGame = mock(Game.class);
        GameField field = mock(GameField.class);
        when(realGame.getField()).thenReturn(field);
        Game game = new LockedGame(new ReentrantReadWriteLock()).wrap(realGame);
        Deal player = new Deal(null, game, null);

        // when then
        assertEquals(false, deal.equals(field));
        assertEquals(true, player.equals(field));
    }

    @Test
    public void testHashCode() {
        // when then
        assertEquals(2096629736, deal.hashCode());
    }

    @Test
    public void testRemove() {
        // given
        boolean[] removed = {false};
        Consumer<Deal> onRemove = deal -> removed[0] = true;

        // when
        deal.remove(onRemove);

        // then
        verify(LockedGame.unwrap(game)).close();
        assertEquals(true, removed[0]);
    }

    @Test
    public void testGetPlayer() {
        // when then
        assertSame(player, deal.getPlayer());
    }

    @Test
    public void testGetGame() {
        // when then
        assertSame(game, deal.getGame());
    }

    @Test
    public void testToString() {
        // when then
        assertEquals(String.format("Deal[player=player, room=room, game=%s]",
                game.getClass().getSimpleName()),
                deal.toString());
    }
    
    @Test 
    public void testSetRoomName_alsoUpdatePlayer() {
        // given
        assertEquals("room", deal.getRoom());
        assertEquals("room", deal.getPlayer().getRoom());
        
        // when 
        deal.setRoom("otherRoom");
        
        // then
        assertEquals("otherRoom", deal.getRoom());
        assertEquals("otherRoom", deal.getPlayer().getRoom());
    }

    @Test
    public void testSetPlayerId_alsoUpdatePlayer() {
        // given
        assertEquals(DEFAULT_TEAM_ID, deal.getTeamId());
        assertEquals(DEFAULT_TEAM_ID, deal.getPlayer().getTeamId());

        // when
        deal.setTeamId(12);

        // then
        assertEquals(12, deal.getTeamId());
        assertEquals(12, deal.getPlayer().getTeamId());
    }

    @Test
    public void testClearScores() {
        // given
        gameType = PlayerTest.mockGameType("game");
        player = spy(new Player("player", "url", gameType,
                NullPlayerScores.INSTANCE, NullInformationCollector.INSTANCE));

        setupGamePlayer();

        LevelProgress progress = mock(LevelProgress.class);
        when(game.getProgress()).thenReturn(progress);

        deal = new Deal(player, game, "room");

        // when
        deal.clearScore();

        // then
        verify(game.getProgress()).reset();
        verify(player).clearScore();
        verify(LockedGame.unwrap(game)).clearScore();
    }

    @Test
    public void testGetPlayerTeamId() {
        assertEquals(DEFAULT_TEAM_ID, deal.getTeamId());

        player.setTeamId(12);

        assertEquals(12, deal.getTeamId());
    }

    @Test
    public void testGetPlayerTeamId_defaultTeamId() {
        assertEquals(DEFAULT_TEAM_ID, deal.getTeamId());
    }
}
