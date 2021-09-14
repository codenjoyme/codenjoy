package com.codenjoy.dojo.loderunner.game;

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


import com.codenjoy.dojo.loderunner.model.items.enemy.EnemyJoystick;
import org.junit.Test;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ENEMIES_COUNT;
import static org.junit.Assert.assertEquals;

public class EnemyMultiplayerTest extends AbstractGameTest {

    @Override
    protected void givenFl(String map) {
        super.givenFl(map);
        enemies.forEach(EnemyJoystick::disableMock);
    }

    private void removePlayer(int index) {
        field.remove(players.get(index));
        players.remove(index);
        listeners.remove(index);
    }

    // чертик идет за тобой
    @Test
    public void shouldEnemyGoToHero() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼     »☼" +
                "☼H#####☼" +
                "☼H     ☼" +
                "☼###H  ☼" +
                "☼►  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    « ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼«     ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼Q     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H  »  ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼► «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼Ѡ  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        events.verifyAllEvents("[KILL_HERO]");
        assertEquals(true, game().isGameOver());

        dice(1, 4);
        game().newGame();

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H  ►  ☼\n" +
                "☼###H  ☼\n" +
                "☼ » H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    // чертик стоит на месте, если ко мне нет пути
    @Test
    public void shouldEnemyStop_whenNoPathToHero() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼     ►☼" +
                "☼     #☼" +
                "☼      ☼" +
                "☼###H  ☼" +
                "☼»  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼     ►☼\n" +
                "☼     #☼\n" +
                "☼      ☼\n" +
                "☼###H  ☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        tick();
        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼     ►☼\n" +
                "☼     #☼\n" +
                "☼      ☼\n" +
                "☼###H  ☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    // чертик идет за тобой по более короткому маршруту
    @Test
    public void shouldEnemyGoToHeroShortestWay() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼     »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldEnemyGoToHeroShortestWay2() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼»     ☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼Q####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    // другой чертик чертику не помеха
    @Test
    public void shouldEnemyGoToHeroShortestWayGetRoundOther() {
        settings.integer(ENEMIES_COUNT, 2);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼»    »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼Q####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldEnemyGoToHeroShortestWayGetRoundOther2() {
        settings.integer(ENEMIES_COUNT, 2);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼» »   ☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼ «    ☼\n" +
                "☼Q####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    // если чертику не достать одного он бежит за другим а не зависает
    @Test
    public void shouldEnemyGoToNewHeroIfOneIsHidden() {
        settings.integer(ENEMIES_COUNT, 1);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼   ►  ☼" +
                "☼######☼" +
                "☼      ☼" +
                "☼###H##☼" +
                "☼»  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();
        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   ►  ☼\n" +
                "☼######☼\n" +
                "☼      ☼\n" +
                "☼###H##☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        givenPlayer(1, 4);
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   (  ☼\n" +
                "☼######☼\n" +
                "☼►     ☼\n" +
                "☼###H##☼\n" +
                "☼ » H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   (  ☼\n" +
                "☼######☼\n" +
                "☼Ѡ     ☼\n" +
                "☼###H##☼\n" +
                "☼   H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [KILL_HERO]\n");
    }

    // каждый чертик бежит за своим героем, даже если к нему занятый уже герой ближе
    @Test
    public void shouldEveryEnemyRunsAfterHisHero_evenIfThereIsAnotherHeroNearbyWhoIsAlreadyBeingHunted() {
        settings.integer(ENEMIES_COUNT, 2);
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼»  ► »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼ » (  ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼  »(  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_HERO]\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H   «H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z» ☼\n" +
                "☼H####H☼\n" +
                "☼H  « H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z »☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###Q##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►Q  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [KILL_HERO]\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ѠH  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        // больше не за кем охотитья - охотники стоят на месте
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ѠH  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ѠH  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        // даже если на поле никого нет, чертики стоят на месте
        removePlayer(1);
        removePlayer(0);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 1);

        // но стоит двоим ребятам появиться на поле
        // как вдруг охотники начнут охотиться каждый за своим
        givenPlayer(1, 2);
        givenPlayer(5, 6);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    ► ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼( «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 3);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    ► ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼(« H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 3);

        // если один вдруг пропадет, то его охотник переключится
        removePlayer(0);

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    ► ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼ « H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 3);

        tick();

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    ►»☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  »H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 3);

        // и после того как нагонят оставшегося, снова зависнут
        tick();

        events.verifyAllEvents("[KILL_HERO]");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    Ѡ ☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  »H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 3);

        tick();

        events.verifyAllEvents("[]");

        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼    Ѡ ☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  »H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n", 3);
    }
}
