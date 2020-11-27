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

import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PlayerGamesMultiplayerTest {

    private PlayerGames playerGames;
    private PrinterFactory printerFactory;

    private GameType single;
    private GameType multiple;
    private GameType tournament;
    private GameType triple;
    private GameType quadro;
    private GameType team4NotDisposable;
    private GameType team5Disposable;
    private GameType training4;

    private List<GameField> fields = new LinkedList<>();
    private List<GamePlayer> gamePlayers = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<Supplier<GameField>> getFileds = new LinkedList<>();

    @Before
    public void setup() {
        playerGames = new PlayerGames();

        playerGames.roomService = mock(RoomService.class);
        // по умолчанию все комнаты активны
        when(playerGames.roomService.isActive(anyString())).thenReturn(true);

        printerFactory = mock(PrinterFactory.class);

        single = setupGameType("single", MultiplayerType.SINGLE);
        tournament = setupGameType("tournament", MultiplayerType.TOURNAMENT);
        triple = setupGameType("triple", MultiplayerType.TRIPLE);
        quadro = setupGameType("quadro", MultiplayerType.QUADRO);
        team4NotDisposable = setupGameType("team4", MultiplayerType.TEAM.apply(4, !MultiplayerType.DISPOSABLE));
        team5Disposable = setupGameType("team5", MultiplayerType.TEAM.apply(5, MultiplayerType.DISPOSABLE));
        multiple = setupGameType("multiple", MultiplayerType.MULTIPLE);
        training4 = setupGameType("training4", MultiplayerType.TRAINING.apply(4));
    }

    private GameType setupGameType(String gameName, MultiplayerType type) {
        GameType result = mock(GameType.class);
        when(result.getMultiplayerType()).thenReturn(type);
        when(result.name()).thenReturn(gameName);
        when(result.getPrinterFactory()).thenReturn(printerFactory);
        when(result.createGame(anyInt())).thenAnswer(inv -> createGameField());
        when(result.createPlayer(any(EventListener.class), anyString())).thenAnswer(inv -> createGamePlayer());
        return result;
    }

    private GameField createGameField() {
        GameField field = mock(GameField.class);
        when(field.reader()).thenReturn(mock(BoardReader.class));
        fields.add(field);
        return field;
    }

    private GamePlayer createGamePlayer() {
        GamePlayer player = mock(GamePlayer.class);
        when(player.isAlive()).thenReturn(true);
        gamePlayers.add(player);
        return player;
    }

    private void verifyAllFieldsTicks(VerificationMode count) {
        getFileds.forEach(field -> verifyFieldTicks(field.get(), count));
    }

    private void resetAllFields() {
        getFileds.forEach(field -> reset(field.get()));
    }

    private void verifyFieldTicks(GameField field, VerificationMode count) {
        verify(field, count).quietTick();
    }

    private void playerWantsToPlay(GameType gameType) {
        int index = gamePlayers.size();
        Player player = new Player("player" + index);
        player.setEventListener(mock(InformationCollector.class));
        players.add(player);
        PlayerGame playerGame = playerWantsToPlay(gameType, player, null);
        getFileds.add(() -> playerGame.getGame().getField());
    }

    private PlayerGame playerWantsToPlay(GameType gameType, Player player, Object data) {
        player.setGameType(gameType);
        String roomName = gameType.name();
        return playerGames.add(player, roomName, null);
    }

    private static class GroupsAsserter {
        private Function<Integer, GameField> mapper;
        private List<List<Integer>> groups;
        private List<Integer> isNull;

        public GroupsAsserter(Function<Integer, GameField> mapper) {
            groups = new LinkedList();
            isNull = new LinkedList();
            this.mapper = mapper;
        }

        public GroupsAsserter notIn(Integer... anotherGroup) {
            groups.add(Arrays.asList(anotherGroup));
            return this;
        }

        public void check() {
            List<String> expected = new LinkedList<>();
            List<String> actual = new LinkedList<>();

            for (int i = 0; i < groups.size(); i++) {
                List<Integer> group = groups.get(i);
                for (int ii = 0; ii < group.size(); ii++) {
                    for (int jj = ii + 1; jj < group.size(); jj++) {
                        if (jj == ii) continue;
                        Integer fj = group.get(jj);
                        Integer fi = group.get(ii);
                        String message = String.format("field[%s] == field[%s]", fi, fj);
                        expected.add(message);
                        if (mapper.apply(fi) == mapper.apply(fj)) {
                            actual.add(message);
                        } else {
                            actual.add(message.replace("==", "!="));
                        }
                    }
                }
                for (int j = i + 1; j < groups.size(); j++) {
                    List<Integer> group1 = groups.get(i);
                    List<Integer> group2 = groups.get(j);
                    for (int ii = 0; ii < group1.size(); ii++) {
                        for (int jj = 0; jj < group2.size(); jj++) {
                            Integer fi = group1.get(ii);
                            Integer fj = group2.get(jj);
                            String message = String.format("field[%s] != field[%s]", fi, fj);
                            expected.add(message);
                            if (mapper.apply(fi) != mapper.apply(fj)) {
                                actual.add(message);
                            } else {
                                actual.add(message.replace("!=", "=="));
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < isNull.size(); i++) {
                String message = String.format("field[%s] %s null", i, (mapper.apply(isNull.get(i)) == null)?"==":"!=");
                actual.add(message);
                expected.add(message.replace("!=", "=="));
            }
            assertEquals(expected.toString().replaceAll(",", ",\n"),
                    actual.toString().replaceAll(",", ",\n"));
        }

        public GroupsAsserter isNull(Integer... fields) {
            isNull.addAll(Arrays.asList(fields));
            return this;
        }
    }

    private GroupsAsserter assertGroup(Integer... group) {
        return new GroupsAsserter(index -> getFileds.get(index).get())
                .notIn(group);
    }

    @Test
    public void shouldEveryPlayerGoToTheirOwnField_whenSingle() {
        // given
        playerWantsToPlay(single);
        playerWantsToPlay(single);
        playerWantsToPlay(single);

        // then
        assertGroup(0)     // тикнется
                .notIn(1)  // тикнется
                .notIn(2)  // тикнется
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    } 
    
    @Test
    public void shouldTwoPlayerOnBoard_whenTournament() {
        // given
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        assertGroup(0, 1)    // эта борда тикнется ибо укомплектована
                .notIn(2, 3) // эта борда тикнется ибо укомплектована
                .notIn(4)    // а эта будет ждать комплектации
                .check();

        // when
        playerGames.tick();

        // then
        assertEquals(5, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));

        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));

        verifyFieldTicks(getFileds.get(4).get(), never());
    }

    @Test
    public void shouldThreePlayerOnBoard_whenTriple() {
        // given
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);

        assertGroup(0, 1, 2)   // эта борда тикнется ибо укомплектована
                .notIn(3, 4)   // а эта будет ждать комплектации
                .check();

        // when
        playerGames.tick();

        // then
        assertEquals(5, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));

        verifyFieldTicks(getFileds.get(3).get(), never());
        verifyFieldTicks(getFileds.get(4).get(), never());
    }

    @Test
    public void shouldXPlayerOnBoard_whenTeamNotDisposable() {
        // given
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);

        assertGroup(0, 1, 2, 3)  // все тикнутся, потому что поле NotDisposable
                .notIn(4)        // все тикнутся, потому что поле NotDisposable
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldXPlayerOnBoard_whenTeamDisposable() {
        // given
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);

        assertGroup(0, 1, 2, 3, 4)  // все тикнутся, поле укомплектовано
                .notIn(5, 6)        // не тикнутся, ждем комплектации
                .check();

        // when
        playerGames.tick();

        // then
        assertEquals(7, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));
        verifyFieldTicks(getFileds.get(4).get(), times(1));

        verifyFieldTicks(getFileds.get(5).get(), never());
        verifyFieldTicks(getFileds.get(6).get(), never());
    }

    @Test
    public void shouldFourPlayerOnBoard_whenQuadro() {
        // given
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        assertGroup(0, 1, 2, 3)   // эта борда тикнется ибо укомплектована
                .notIn(4)         // а эта будет ждать комплектации
                .check();

        // when
        playerGames.tick();

        // then
        assertEquals(5, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));

        verifyFieldTicks(getFileds.get(4).get(), never());
    }

    // независимо от того кто где в training все комнаты будут тикаться независимо
    // а multiple одна для всех
    @Test
    public void shouldSeveralTrainings_whenTraining() {
        // given
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);

        assertGroup(0)     // все комнаты на первом уровне одиночные как single
                .notIn(1)  // -- " --
                .notIn(2)  // -- " --
                .notIn(3)  // -- " --
                .check();

        // when
        playerGames.tick();

        // then
        assertEquals(4, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));

        resetAllFields();

        // when
        nextLevel(1);
        nextLevel(2);
        nextLevel(3);
        playerGames.tick();

        // then
        assertGroup(0)     // все там же - первый уровень single
                .notIn(1)  // второй уровень single
                .notIn(2)  // второй уровень single
                .notIn(3)  // второй уровень single
                .check();

        assertEquals(4, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));

        resetAllFields();

        //
        nextLevel(1);
        nextLevel(2);
        nextLevel(3);
        playerGames.tick();

        // then
        // then
        assertGroup(0)     // все там же - первый уровень single
                .notIn(1)  // третий предпоследний уровень single
                .notIn(2)  // третий предпоследний уровень single
                .notIn(3)  // третий предпоследний уровень single
                .check();

        assertEquals(4, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));

        resetAllFields();

        // when
        nextLevel(1);
        nextLevel(2);
//        nextLevel(3); // этот не и дет дальше
        playerGames.tick();

        // then
        assertGroup(0)        // все там же - первый уровень single
                .notIn(3)     // этот остался на третьем предпоследнем уровне single
                .notIn(1, 2)  // а эти двое перешли на multiple
                .check();

        assertEquals(4, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));

        assertSame(getFileds.get(1).get(), getFileds.get(2).get());
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));

        verifyFieldTicks(getFileds.get(3).get(), times(1));

        resetAllFields();

        // when
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);

        // then
        assertGroup(0)        // все там же - первый уровень single
                .notIn(3)     // этот остался на третьем предпоследнем уровне single
                .notIn(1, 2)  // а эти двое перешли на multiple
                .notIn(4)     // первый уровень single
                .notIn(5)     // первый уровень single
                .check();

        // when
        nextLevel(3);
        playerGames.tick();

        // then
        assertGroup(0)        // все там же - первый уровень single
                .notIn(1, 2, 3)  // 3 тоже дошел до multiple
                .notIn(4)     // первый уровень single
                .notIn(5)     // первый уровень single
                .check();

        assertEquals(6, getFileds.size());

        verifyFieldTicks(getFileds.get(0).get(), times(1));

        assertSame(getFileds.get(1).get(), getFileds.get(2).get());
        assertSame(getFileds.get(1).get(), getFileds.get(3).get());
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));

        verifyFieldTicks(getFileds.get(4).get(), times(1));
        verifyFieldTicks(getFileds.get(5).get(), times(1));

        resetAllFields();

        // when
        nextLevel(0);
        nextLevel(0);
        nextLevel(0);

        nextLevel(4);
        nextLevel(4);
        nextLevel(4);

        nextLevel(5);
        nextLevel(5);
//        nextLevel(5); // этот не и дет дальше

        playerGames.tick();

        // then
        assertGroup(5)                       // завис на предпоследнем
                .notIn(0, 1, 2, 3, 4)        // все дошли до конца кроме 5
                .check();

        assertEquals(6, getFileds.size());

        assertSame(getFileds.get(0).get(), getFileds.get(1).get());
        assertSame(getFileds.get(1).get(), getFileds.get(2).get());
        assertSame(getFileds.get(2).get(), getFileds.get(3).get());
        assertSame(getFileds.get(3).get(), getFileds.get(4).get());
        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));
        verifyFieldTicks(getFileds.get(4).get(), times(1));

        verifyFieldTicks(getFileds.get(5).get(), times(1));

        resetAllFields();

        // when
        nextLevel(5);

        playerGames.tick();

        // then
        assertGroup(0, 1, 2, 3, 4, 5)        // все дошли до конца
                .check();

        assertEquals(6, getFileds.size());

        assertSame(getFileds.get(0).get(), getFileds.get(1).get());
        assertSame(getFileds.get(1).get(), getFileds.get(2).get());
        assertSame(getFileds.get(2).get(), getFileds.get(3).get());
        assertSame(getFileds.get(3).get(), getFileds.get(4).get());
        assertSame(getFileds.get(4).get(), getFileds.get(5).get());
        verifyFieldTicks(getFileds.get(0).get(), times(1));
        verifyFieldTicks(getFileds.get(1).get(), times(1));
        verifyFieldTicks(getFileds.get(2).get(), times(1));
        verifyFieldTicks(getFileds.get(3).get(), times(1));
        verifyFieldTicks(getFileds.get(4).get(), times(1));
        verifyFieldTicks(getFileds.get(5).get(), times(1));

        resetAllFields();
    }

    @Test
    public void shouldAllPlayersGoToOneField_whenMultiple() {
        // given
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        assertGroup(0, 1, 2)   // тикнтся так как она not disposable
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldPlayerStartsNewGame_whenSingle_whenRemove() {
        // given
        playerWantsToPlay(single);
        playerWantsToPlay(single);

        assertGroup(0)
                .notIn(1)
                .check();

        // when
        playerWantsToPlay(single);

        assertGroup(0)
                .notIn(1)
                .notIn(2)
                .check();

        // when
        remove(0);

        // then
        assertGroup(0)
                .notIn(1)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTournament_whenRemove() {
        // given
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        assertGroup(0, 1) // двое первых забили комнату сразу
                .notIn(2) // а третий ждет в своей отдельной, у него 1 место свободно
                .check();

        // when
        remove(0);

        // then
        assertGroup(2, 1)  // так как я удалил 0 и 1 остался сам, то он сразу перешел в новую комнату к 2
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        // then
        assertGroup(2, 1)    // старая забитая комната
                .notIn(3, 4) // и двое новых тоже забили комнату
                .isNull(0)
                .check();

        // when
        remove(1);

        // then
        assertGroup(3, 4)  // старая забитая комната
                .notIn(2)  // удаляя 1 я оставляю 2 в комнате самого, и потому для него сразу создается новая комната
                .isNull(0, 1)
                .check();

        // when
        playerWantsToPlay(tournament);

        // then
        assertGroup(3, 4)    // старая забитая комната
                .notIn(2, 5) // новенький подселяется к одиночке
                .isNull(0, 1)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTriple_whenRemove() {
        // given
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);

        assertGroup(0, 1, 2) // комната забита, сюда больше не зайдут
                .notIn(3, 4) // сюда может зайти еще один
                .check();

        // when
        remove(0);

        // then
        assertGroup(1, 2)    // комната вроде бы и свободна, но сюда больше не зайдут потому что она одноразовая
                .notIn(3, 4) // а сюда еще можно одного добавить
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(triple); // добвится к 3, 4
        playerWantsToPlay(triple); // будет один ждать

        // then
        assertGroup(1, 2)       // комната забита, в ней было трое раньше
                .notIn(3, 4, 5) // и эта комната теперь тоже забита
                .notIn(6)       // а это новенький, к нему можно двоих подселить еще
                .isNull(0)
                .check();

        // when
        remove(1);

        // then
        assertGroup(3, 4, 5)  // старая комната, забитая уже
                .notIn(6, 2)  // так как 2 после удаления 1 оставался один в комнате, то для него новая комната создана и она свободна - тут еще один подселиться может
                .isNull(0, 1) // а это удаленные ребята
                .check();

        // when
        playerWantsToPlay(triple);

        // then
        assertGroup(3, 4, 5)    // старая комната, забитая уже
                .notIn(6, 2, 7) // последний прибыл и все мест больше нет
                .isNull(0, 1)
                .check();

        // when
        playerWantsToPlay(triple);

        // then
        assertGroup(3, 4, 5)     // старая комната, забитая уже
                .notIn(6, 7, 2)  // старая комната, забитая уже
                .notIn(8)        // новому и новая комната
                .isNull(0, 1)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenQuadro_whenRemove() {
        // given
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        assertGroup(0, 1, 2, 3) // все 4 сразу же забили комнату под себя
                .notIn(4)       // у последнего прибывшего в комнате есть еще 3 места
                .check();

        // when
        remove(0);

        // then
        assertGroup(1, 2, 3)   // тут хоть и трое, но комната забита
                .notIn(4)      // тут как и раньше 3 места вакантных
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        // then
        assertGroup(1, 2, 3)       // страя команата была забита доверху, потому и не заполняется больше
                .notIn(4, 5, 6, 7) // а вот ствободная - пожалуйста. кстати она уже тоже забита
                .isNull(0)
                .check();

        // when
        remove(1);

        // then
        assertGroup(2, 3)           // старая забитая комната
                .notIn(4, 5, 6, 7)  // старая забитая комната
                .isNull(0, 1)
                .check();

        // when
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        // then
        assertGroup(2, 3)          // старая забитая комната
                .notIn(4, 5, 6, 7) // старая забитая комната
                .notIn(8, 9, 10)   // а тут еще место есть
                .isNull(0, 1)
                .check();

        // when
        remove(10);

        // then
        assertGroup(2, 3)          // старая забитая комната
                .notIn(4, 5, 6, 7) // старая забитая комната
                .notIn(8, 9)       // а тут еще место есть, но только одно потому что тут было трое
                .isNull(0, 1, 10)
                .check();

        // when
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        // then
        assertGroup(2, 3)          // старая забитая комната
                .notIn(4, 5, 6, 7) // старая забитая комната
                .notIn(8, 9, 11)   // последний пришел и место занял, в комнате всего было 4 юзера
                .notIn(12, 13)     // а это новая, тут еще два места есть
                .isNull(0, 1, 10)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTeam4NotDisposable_whenRemove() {
        // given
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);

        assertGroup(0, 1, 2, 3) // комната забита, но она многоразовая
                .notIn(4)       // новенький пока сам по себе
                .check();

        // when
        remove(0);

        // then
        assertGroup(1, 2, 3)   // так как комната многоразовая тут появилась 1 вакансия
                .notIn(4)      // новенький все так же в ожидании
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);

        // then
        assertGroup(1, 2, 3, 5)  // все новоприбывшие равномерно...
                .notIn(4, 6, 7)  // ... заняли все свободные комнаты
                .isNull(0)
                .check();

        // when
        remove(1);

        // then
        assertGroup(2, 3, 5)     // снова в первой комнате вакансия
                .notIn(4, 6, 7)  // и тут тоже
                .isNull(0, 1)
                .check();

        // when
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);
        playerWantsToPlay(team4NotDisposable);

        // then
        assertGroup(2, 3, 5, 8)     // все вакансии ...
                .notIn(4, 6, 7, 9)  // ... Успешно забили
                .notIn(10)          // одному не хватило
                .isNull(0, 1)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTeam5Disposable_whenRemove() {
        // given
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);

        assertGroup(0, 1, 2, 3, 4) // комната забита, она одноразовая
                .notIn(5)          // новенький пока сам по себе
                .check();

        // when
        remove(0);

        // then
        assertGroup(1, 2, 3, 4)   // комната хоть и кажется что свободна для одного, но она одноразовая
                .notIn(5)         // новенький все так же в ожидании
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);

        // then
        assertGroup(1, 2, 3, 4)         // это старя забитая комната
                .notIn(5, 6, 7, 8, 9)   // новые занимают свободные вакансии
                .isNull(0)
                .check();

        // when
        remove(1);

        // then
        assertGroup(2, 3, 4)           // это старя забитая комната
                .notIn(5, 6, 7, 8, 9)  // это старя забитая комната
                .isNull(0, 1)
                .check();

        // when
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);
        playerWantsToPlay(team5Disposable);

        // then
        assertGroup(2, 3, 4)           // это старя забитая комната
                .notIn(5, 6, 7, 8, 9)  // это старя забитая комната
                .notIn(10, 11, 12)     // а вот тут еще три места будет
                .isNull(0, 1)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTraining_whenRemove() {
        // given
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);

        assertGroup(0)     // все комнаты на первом уровне одиночные как single
                .notIn(1)  // -- " --
                .notIn(2)  // -- " --
                .notIn(3)  // -- " --
                .check();

        // when
        remove(0);

        // then
        assertGroup(1)     // все комнаты на первом уровне одиночные как single
                .notIn(2)  // -- " --
                .notIn(3)  // -- " --
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(training4);

        // then
        assertGroup(1)     // все комнаты на первом уровне одиночные как single
                .notIn(2)  // -- " --
                .notIn(3)  // -- " --
                .notIn(4)  // -- " --
                .isNull(0)
                .check();

        // when
        nextLevel(1);
        nextLevel(2);
        nextLevel(4);

        // then
        // then
        assertGroup(1)     // второй уровень single
                .notIn(2)  // второй уровень single
                .notIn(3)  // все там же - первый уровень single
                .notIn(4)  // второй уровень single
                .isNull(0)
                .check();

        //
        nextLevel(1);
        nextLevel(2);
        nextLevel(4);

        // then
        // then
        assertGroup(1)     // третий предпоследний уровень single
                .notIn(2)  // третий предпоследний уровень single
                .notIn(3)  // все там же - первый уровень single
                .notIn(4)  // третий предпоследний уровень single
                .isNull(0)
                .check();

        // when
        nextLevel(1);
        nextLevel(2);
//        nextLevel(4); // этот не и дет дальше

        // then
        assertGroup(3)        // все там же - первый уровень single
                .notIn(4)     // этот остался на третьем предпоследнем уровне single
                .notIn(1, 2)  // а эти двое перешли на multiple
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(training4);
        playerWantsToPlay(training4);

        // then
        assertGroup(3)        // все там же - первый уровень single
                .notIn(4)     // этот остался на третьем предпоследнем уровне single
                .notIn(1, 2)  // а эти двое перешли на multiple
                .notIn(5)     // первый уровень single
                .notIn(6)     // первый уровень single
                .isNull(0)
                .check();

        // when
        nextLevel(4);

        // then
        assertGroup(3)        // все там же - первый уровень single
                .notIn(1, 2, 4)  // 4 тоже дошел до multiple
                .notIn(5)     // первый уровень single
                .notIn(6)     // первый уровень single
                .isNull(0)
                .check();

        // when
        nextLevel(3);
        nextLevel(3);
        nextLevel(3);

        nextLevel(5);
        nextLevel(5);
        nextLevel(5);

        nextLevel(6);
        nextLevel(6);
//        nextLevel(6); // этот не и дет дальше

        // then
        assertGroup(6)                       // завис на предпоследнем
                .notIn(1, 2, 4, 3, 5)        // все дошли до конца кроме 6
                .isNull(0)
                .check();

        // when
        nextLevel(6);

        // then
        assertGroup(1, 2, 4, 3, 5, 6)        // все дошли до конца
                .isNull(0)
                .check();

    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenMultiple_whenRemove() {
        // given
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        assertGroup(0, 1, 2, 3, 4) // multiple - тут комната многоразовая и безразмерная
                .check();

        // when
        remove(0);

        // then
        assertGroup(1, 2, 3, 4)
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        // then
        assertGroup(1, 2, 3, 4, 5, 6, 7) // multiple - тут комната многоразовая и безразмерная
                .isNull(0)
                .check();

        // when
        remove(5);

        // then
        assertGroup(1, 2, 3, 4, 6, 7) // multiple - тут комната многоразовая и безразмерная
                .isNull(0, 5)
                .check();

        // when
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        // then
        assertGroup(1, 2, 3, 4, 6, 7, 8, 9, 10) // multiple - тут комната многоразовая и безразмерная
                .isNull(0, 5)
                .check();
    }

    private void remove(int index) {
        playerGames.remove(players.get(index));
    }

    private void nextLevel(int index) {
        String name = players.get(index).getId();
        PlayerGame playerGame = playerGames.get(name);

        LevelProgress progress = playerGame.getGame().getProgress();
        progress.change(progress.getCurrent() + 1, progress.getCurrent());

        JSONObject save = new JSONObject();
        progress.saveTo(save);
        playerGames.setLevel(name, save);
    }

    @Test
    public void shouldMixSeveralGameTypes() {
        // given
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        assertGroup(0, 1, 2, 3, 4)   // multiple - тут комната многоразовая и безразмерная
                .check();

        // when
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        // then
        assertGroup(0, 1, 2, 3, 4)   // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 6, 7)      // quadro - комната одноразовая по 4 за раз
                .check();

        // when
        playerWantsToPlay(single);

        // then
        assertGroup(0, 1, 2, 3, 4) // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 6, 7)    // quadro - комната одноразовая по 4 за раз. осталось 1 место
                .notIn(8)          // single - одноразовая одноместная комната
                .check();

        // when
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        // then
        assertGroup(0, 1, 2, 3, 4) // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 6, 7)    // quadro - комната одноразовая по 4 за раз. осталось 1 место
                .notIn(8)          // single - одноразовая одноместная комната
                .notIn(9, 10)      // tournament - двухместная одноразовая комната, уже занята
                .check();

        // when
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);

        // then
        assertGroup(0, 1, 2, 3, 4) // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 6, 7)    // quadro - комната одноразовая по 4 за раз. осталось 1 место
                .notIn(8)          // single - одноразовая одноместная комната
                .notIn(9, 10)      // tournament - двухместная одноразовая комната, уже занята
                .notIn(11, 12, 13) // triple - трехместная одноразовая комната, уже занята
                .check();

        // when
        remove(0);
        remove(6);
        remove(10);
        remove(13);

        // then
        assertGroup(1, 2, 3, 4)       // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 7)          // quadro - комната одноразовая по 4 за раз. осталось 1 место
                .notIn(8)             // single - одноразовая одноместная комната
                .notIn(9)             // tournament - двухместная одноразовая комната, хоть и была занята, но после удаления напарника пеесоздалась. свободно 1 место
                .notIn(11, 12)        // triple - трехместная одноразовая комната, уже была занята
                .isNull(0, 6, 10, 13)
                .check();

        // when
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        // then
        assertGroup(1, 2, 3, 4, 14, 15) // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 7)            // quadro - комната одноразовая по 4 за раз. осталось 1 место
                .notIn(8)               // single - одноразовая одноместная комната
                .notIn(9)               // tournament - двухместная одноразовая комната, хоть и была занята, но после удаления напарника пеесоздалась. свободно 1 место
                .notIn(11, 12)          // triple - трехместная одноразовая комната, уже была занята
                .isNull(0, 6, 10, 13)
                .check();

        // when
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        // then
        assertGroup(1, 2, 3, 4, 14, 15)  // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 7, 16)         // quadro - комната одноразовая по 4 за раз. все забито уже, тут было за все время 4 игрока
                .notIn(8)                // single - одноразовая одноместная комната
                .notIn(9)                // tournament - двухместная одноразовая комната, хоть и была занята, но после удаления напарника пеесоздалась. свободно 1 место
                .notIn(11, 12)           // triple - трехместная одноразовая комната, уже была занята
                .notIn(17, 18)           // quadro - комната одноразовая по 4 за раз. осталось 2 места
                .isNull(0, 6, 10, 13)
                .check();

        // when
        playerWantsToPlay(single);
        playerWantsToPlay(single);

        // then
        assertGroup(1, 2, 3, 4, 14, 15)  // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 7, 16)         // quadro - комната одноразовая по 4 за раз. все забито уже, тут было за все время 4 игрока
                .notIn(8)                // single - одноразовая одноместная комната
                .notIn(9)                // tournament - двухместная одноразовая комната, хоть и была занята, но после удаления напарника пеесоздалась. свободно 1 место
                .notIn(11, 12)           // triple - трехместная одноразовая комната, уже была занята
                .notIn(17, 18)           // quadro - комната одноразовая по 4 за раз. осталось 2 места
                .notIn(19)               // single - одноразовая одноместная комната
                .notIn(20)               // single - одноразовая одноместная комната
                .isNull(0, 6, 10, 13)
                .check();

        // when
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        // then
        assertGroup(1, 2, 3, 4, 14, 15)  // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 7, 16)         // quadro - комната одноразовая по 4 за раз. все забито уже, тут было за все время 4 игрока
                .notIn(8)                // single - одноразовая одноместная комната
                .notIn(9, 21)            // tournament - двухместная одноразовая комната. уже занята
                .notIn(11, 12)           // triple - трехместная одноразовая комната, уже была занята
                .notIn(17, 18)           // quadro - комната одноразовая по 4 за раз. осталось 2 места
                .notIn(19)               // single - одноразовая одноместная комната
                .notIn(20)               // single - одноразовая одноместная комната
                .notIn(22)               // tournament - двухместная одноразовая комната, пока есть 1 место
                .isNull(0, 6, 10, 13)
                .check();

        // when
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);

        // then
        assertGroup(1, 2, 3, 4, 14, 15)  // multiple - тут комната многоразовая и безразмерная
                .notIn(5, 7, 16)         // quadro - комната одноразовая по 4 за раз. все забито уже, тут было за все время 4 игрока
                .notIn(8)                // single - одноразовая одноместная комната
                .notIn(9, 21)            // tournament - двухместная одноразовая комната. уже занята
                .notIn(11, 12)           // triple - трехместная одноразовая комната, уже была занята
                .notIn(17, 18)           // quadro - комната одноразовая по 4 за раз. осталось 2 места
                .notIn(19)               // single - одноразовая одноместная комната
                .notIn(20)               // single - одноразовая одноместная комната
                .notIn(22)                // tournament - двухместная одноразовая комната, пока есть 1 место
                .notIn(23, 24)           // triple - трехместная одноразовая комната, свободно 1 место
                .isNull(0, 6, 10, 13)
                .check();
    }
}
