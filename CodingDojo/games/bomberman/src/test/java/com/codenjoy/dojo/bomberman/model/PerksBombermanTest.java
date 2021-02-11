package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.bomberman.model.perks.*;
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class PerksBombermanTest extends AbstractBombermanTest {

    @Test
    public void shouldPerkBeDropped_whenWallIsDestroyed() {
        // given
        givenBoardWithDestroyWalls(6);
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 5, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%
        when(heroDice.next(anyInt())).thenReturn(10, 30); // must drop 1 perk

        hero.act();
        field.tick();

        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.right();
        field.tick();

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "# ☺  #\n" +
                "#҉# ##\n" +
                "H҉҉  #\n" +
                "#H####\n");

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "# ☺  #\n" +
                "# # ##\n" +
                "+    #\n" +
                "# ####\n");
    }

    // новый бобмер не может появиться на перке
    @Test
    public void shouldHeroCantSpawnOnPerk() {
        // given
        givenBoardWithDestroyWalls(6);

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 4, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%

        when(heroDice.next(anyInt())).thenReturn(10); // must drop 2 perks

        hero.act();
        field.tick();

        hero.right();
        field.tick();

        field.tick();

        field.tick();

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "#҉# ##\n" +
                "H҉Ѡ  #\n" +
                "#H####\n");

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "+ Ѡ  #\n" +
                "#+####\n");

        // when
        // вот он последний тик перед взрывом, тут все и случится
        dice(heroDice,
                0, 1,   // пробуем разместить героя поверх перка1
                1, 0,   // пробуем разместить героя поверх перка2
                3, 3);  // а потом в свободное место
        field.tick();
        newGameForDied(); // это сделает сервер

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#  ☺ #\n" +
                "# # ##\n" +
                "+    #\n" +
                "#+####\n");
    }

    // BBRI = Bomb Blast Radius Increase perk
    // проверяем, что перков может появиться два
    // проверяем, что перки не пропадают на следующий тик
    // проверяем, что перк можно подобрать
    @Test
    public void shouldHeroAcquirePerk_whenMoveToFieldWithPerk() {
        // given
        givenBoardWithDestroyWalls(6);

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 4, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%
        PerksSettingsWrapper.setPickTimeout(50);

        when(heroDice.next(anyInt())).thenReturn(10); // must drop 2 perks

        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        field.tick();

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "#҉# ##\n" +
                "H҉҉☺ #\n" +
                "#H####\n");

        assertEquals("[]", field.perks().toString());

        verifyAllEvents("[KILL_DESTROY_WALL, KILL_DESTROY_WALL]");

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "+  ☺ #\n" +
                "#+####\n");

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "+  ☺ #\n" +
                "#+####\n");

        hero.left();
        field.tick();

        hero.left();
        field.tick();

        int before = hero.scores();
        assertEquals(2*DefaultGameSettings.KILL_WALL_SCORE, before);

        // when
        // go for perk
        hero.left();
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "☺    #\n" +
                "#+####\n");

        verifyAllEvents("[CATCH_PERK]");
        assertEquals(before + DefaultGameSettings.CATCH_PERK_SCORE, hero.scores());
        assertEquals("Hero had to acquire new perk", 1, player.getHero().getPerks().size());
    }

    // проверяем, что перк удалится с поля через N тиков если его никто не возьмет
    @Test
    public void shouldRemovePerk_whenPickTimeout() {
        // given
        givenBoardWithDestroyWalls(6);

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 4, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%
        PerksSettingsWrapper.setPickTimeout(5);

        when(heroDice.next(anyInt())).thenReturn(10); // must drop 2 perks

        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        field.tick();

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "#҉# ##\n" +
                "H҉҉☺ #\n" +
                "#H####\n");

        assertEquals("[]", field.perks().toString());


        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "+  ☺ #\n" +
                "#+####\n");

        assertEquals("[{PerkOnBoard {BOMB_BLAST_RADIUS_INCREASE('+') value=4, timeout=3, timer=3, pick=4} at [0,1]}, " +
                "{PerkOnBoard {BOMB_BLAST_RADIUS_INCREASE('+') value=4, timeout=3, timer=3, pick=4} at [1,0]}]", field.perks().toString());

        // when
        field.tick();
        field.tick();
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "+  ☺ #\n" +
                "#+####\n");

        assertEquals("[{PerkOnBoard {BOMB_BLAST_RADIUS_INCREASE('+') value=4, timeout=3, timer=3, pick=1} at [0,1]}, " +
                "{PerkOnBoard {BOMB_BLAST_RADIUS_INCREASE('+') value=4, timeout=3, timer=3, pick=1} at [1,0]}]", field.perks().toString());

        // when
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "   ☺ #\n" +
                "# ####\n");

        assertEquals("[]", field.perks().toString());

    }

    // проверяем, что уничтожение перка порождает злого-анти-митчопера :)
    @Test
    public void shouldDropPerk_generateNewMeatChopper() {
        shouldHeroAcquirePerk_whenMoveToFieldWithPerk();
        reset(listener);

        hero.right();
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                " ☺   #\n" +
                "#+####\n");

        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        hero.up();
        field.tick();

        // перед взрывом
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# #☺##\n" +
                " 1   #\n" +
                "#+####\n");

        // все тихо
        verifyAllEvents("[]");

        // when
        field.tick();

        // перк разрушен
        // а вместо него злой митчопер
        asrtBrd("#H####\n" +
                "#҉# ##\n" +
                "#҉   #\n" +
                "#҉#☺##\n" +
                "҉҉҉҉҉H\n" +
                "#x####\n");

        // пошел сигнал об этом
        verifyAllEvents("[DROP_PERK, KILL_DESTROY_WALL, KILL_DESTROY_WALL]");

        // такой себе хак, мы в домике
        hero.move(3, 4);
        field.walls().add(new DestroyWall(1, 2));
        field.walls().add(new DestroyWall(1, 3));
        field.walls().add(new Wall(1, 4));

        // when
        field.tick();

        // митчопер начал свое движение
        asrtBrd("#+####\n" +
                "#☼#☺##\n" +
                "##   #\n" +
                "### ##\n" +
                " x   +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+####\n" +
                "#☼#☺##\n" +
                "##   #\n" +
                "#H# ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+####\n" +
                "#☼#☺##\n" +
                "#H   #\n" +
                "# # ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+####\n" +
                "#☼#☺##\n" +
                "# x  #\n" +
                "# # ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+####\n" +
                "#☼H☺##\n" +
                "#    #\n" +
                "# # ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+####\n" +
                "#☼ Ѡ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "     +\n" +
                "# ####\n");

        verifyAllEvents("[DIED]");

        field.tick();

        asrtBrd("#+####\n" +
                "#☼ Ѡ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "     +\n" +
                "# ####\n");

        dice(heroDice,
                1, 1);
        field.tick();
        newGameForDied(); // это сделает сервер

        field.tick();

        asrtBrd("#+####\n" +
                "#☼ +##\n" +  // антимитчопер превратился обратно в перк
                "#    #\n" +
                "# # ##\n" +
                " ☺   +\n" +
                "# ####\n");
    }

    // а теперь пробуем убить анти-митчопера
    @Test
    public void shouldDropPerk_generateNewMeatChopper_thenKillIt() {
        when(level.bombsCount()).thenReturn(2);

        shouldHeroAcquirePerk_whenMoveToFieldWithPerk();
        reset(listener);

        hero.right();
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                " ☺   #\n" +
                "#+####\n");

        hero.act();
        hero.up();
        field.tick();

        field.tick();

        hero.act();
        hero.up();
        field.tick();

        hero.right();
        field.tick();

        // перед взрывом
        asrtBrd("######\n" +
                "# # ##\n" +
                "# ☺  #\n" +
                "#3# ##\n" +
                " 1   #\n" +
                "#+####\n");

        // все тихо
        verifyAllEvents("[]");

        // when
        field.tick();

        // перк разрушен
        // а вместо него злой митчопер
        asrtBrd("#H####\n" +
                "#҉# ##\n" +
                "#҉☺  #\n" +
                "#2# ##\n" +
                "҉҉҉҉҉H\n" +
                "#x####\n");

        // пошел сигнал об этом
        verifyAllEvents("[DROP_PERK, KILL_DESTROY_WALL, KILL_DESTROY_WALL]");

        // when
        field.tick();

        // митчопер начал свое движение
        asrtBrd("#+####\n" +
                "# # ##\n" +
                "# ☺  #\n" +
                "#1# ##\n" +
                " x   +\n" +
                "# ####\n");

        // when
        field.tick();

        // митчопер нарвался
        asrtBrd("#+####\n" +
                "# # ##\n" +
                "#҉☺  #\n" +
                "H&H ##\n" +
                " ҉   +\n" +
                "# ####\n");

        // пошел сигнал об этом
        verifyAllEvents("[KILL_MEAT_CHOPPER, KILL_DESTROY_WALL, KILL_DESTROY_WALL]");

        // when
        field.tick();

        // митчопер нарвался
        asrtBrd("#+####\n" +
                "# # ##\n" +
                "# ☺  #\n" +
                "+++ ##\n" +
                "     +\n" +
                "# ####\n");

        verifyAllEvents("[]");
    }


    // генерим три митчопера и смотрим как они бегут за мной
    @Test
    public void shouldDropPerk_generateThreeMeatChoppers() {
        shouldDropPerk_generateNewMeatChopper_thenKillIt();

        // бамбанули между двух перков, хак (перк при этом не взяли)
        hero.move(1, 2);
        hero.act();

        // строим оборону
        field.walls().destroy(pt(5, 5));
        field.walls().destroy(pt(4, 4));
        field.walls().add(new Wall(4, 4));
        field.walls().destroy(new Wall(4, 5));
        field.walls().add(new Wall(4, 5));
        field.walls().destroy(pt(5, 4));
        hero.move(5, 5);
        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼ \n" +
                "#    #\n" +
                "+4+ ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();
        field.tick();
        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼ \n" +
                "#    #\n" +
                "+1+ ##\n" +
                "     +\n" +
                "# ####\n");

        verifyAllEvents("[]");

        // породили три чудовища
        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼ \n" +
                "#҉   #\n" +
                "xxx ##\n" +
                " ҉   +\n" +
                "# ####\n");

        verifyAllEvents("[DROP_PERK, DROP_PERK, DROP_PERK]");

        // и они пошли за нами
        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼ \n" +
                "Hxx  #\n" +
                "    ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼ \n" +
                " xxx #\n" +
                "    ##\n" +
                "     +\n" +
                "# ####\n");

        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼ \n" +
                "  xxx#\n" +
                "    ##\n" +
                "     +\n" +
                "# ####\n");

        verifyAllEvents("[]");
    }

    // если анти-митчоперы не могут найти к тебе короткий путь - они выпиливаются
    // вместо них будут перки
    @Test
    public void shouldDropPerk_generateTwoMeatChoppers_noWayNoPain() {
        shouldDropPerk_generateThreeMeatChoppers();

        // но стоит забарикадироваться
        field.walls().add(new Wall(5, 4));
        field.tick();

        // как митчоперы нормальнеют
        asrtBrd("#+##☼☺\n" +
                "# # ☼☼\n" +
                "  &&&#\n" +
                "    ##\n" +
                "     +\n" +
                "# ####\n");

        // и после выпиливаются
        field.tick();

        asrtBrd("#+##☼☺\n" +
                "# # ☼☼\n" +
                "  +++#\n" +  // антимитчоперы превратились обратно в перки
                "    ##\n" +
                "     +\n" +
                "# ####\n");

        verifyAllEvents("[]");
    }

    // если мы вызвали потустороннюю нечисть, то наш суицид ее успокоит, отправив обратно
    @Test
    public void shouldDropPerk_generateNewMeatChopper_thenSuicide_willKillChopperAlso() {
        shouldHeroAcquirePerk_whenMoveToFieldWithPerk();
        reset(listener);

        hero.right();
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                " ☺   #\n" +
                "#+####\n");

        hero.act();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        hero.up();
        field.tick();

        // перед взрывом
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# #☺##\n" +
                " 1   #\n" +
                "#+####\n");

        // все тихо
        verifyAllEvents("[]");

        // when
        field.tick();

        // перк разрушен
        // а вместо него злой митчопер
        asrtBrd("#H####\n" +
                "#҉# ##\n" +
                "#҉   #\n" +
                "#҉#☺##\n" +
                "҉҉҉҉҉H\n" +
                "#x####\n");

        // пошел сигнал об этом
        verifyAllEvents("[DROP_PERK, KILL_DESTROY_WALL, KILL_DESTROY_WALL]");

        // охотник идет
        field.tick();
        field.tick();

        asrtBrd("#+####\n" +
                "# # ##\n" +
                "#    #\n" +
                "#x#☺##\n" +
                "     +\n" +
                "# ####\n");

        // мувнули героя и кикнули его
        hero.die();
        field.tick();

        asrtBrd("#+####\n" +
                "# # ##\n" +
                "#    #\n" +
                "#&#Ѡ##\n" +
                "     +\n" +
                "# ####\n");

        verifyAllEvents("[DIED]");

    }

    // проверяем, что перк пропадает после таймаута
    @Test
    public void shouldPerkBeDeactivated_whenTimeout() {
        // given
        int timeout = 3; // время работы перка

        int value = 4;   // показатель его влияния, в тесте не интересно
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, value, timeout);
        PerksSettingsWrapper.setDropRatio(20);

        player.getHero().addPerk(new BombBlastRadiusIncrease(value, timeout));
        assertEquals("Hero had to acquire new perk",
                1, player.getHero().getPerks().size());

        // when
        field.tick();
        field.tick();
        field.tick();

        // then
        assertEquals("Hero had to loose perk",
                0, player.getHero().getPerks().size());
    }

    // Проверяем длинну волны взрывной в отсутствии перка BBRI
    @Test
    public void shouldBombBlastRadiusIncrease_whenNoBBRIperk() {
        // given
        givenBoardWithDestroyWalls(12);

        hero.act();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        // when
        field.tick();

        // then
        asrtBrd("############\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "#҉# # # # ##\n" +
                "HѠ҉        #\n" +
                "#H##########\n");
    }

    // Проверяем что перк BBRI увеличивает длинну волны бомбы
    @Test
    public void shouldBombBlastRadiusIncrease_whenBBRIperk() {
        // given
        givenBoardWithDestroyWalls(12);

        int value = 4;   // на сколько клеток разрывная волна увеличится (по умолчанию 1)
        int timeout = 5; // сколько это безобразие будет длиться

        player.getHero().addPerk(new BombBlastRadiusIncrease(value, timeout));

        hero.act();
        field.tick();

        field.tick();
        field.tick();
        field.tick();

        // when
        field.tick();

        // then
        asrtBrd("############\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "#҉# # # # ##\n" +
                "#҉         #\n" +
                "#҉# # # # ##\n" +
                "#҉         #\n" +
                "#҉# # # # ##\n" +
                "HѠ҉҉҉҉҉    #\n" +
                "#H##########\n");
    }

    // Проверяем что два перка BBRI увеличивают длинну волны бомбы на размер второго перка
    // При этом общее время суммируется. Но так же важно, что перк влияет только на будущие бомбы,
    // а не те, которые уже на поле. И после того как он отработает, все вернется как было.
    @Test
    public void shouldBombBlastRadiusIncreaseTwice_whenBBRIperk() {
        // given
        givenBoardWithDestroyWalls(12);

        int value = 4;   // на сколько клеток разрывная волна увеличится (по умолчанию 1)
        int timeout = 5; // сколько это безобразие будет длиться (времени должно хватить)

        player.getHero().addPerk(new BombBlastRadiusIncrease(value, timeout));

        assertEquals("[{BOMB_BLAST_RADIUS_INCREASE('+') " +
                        "value=4, timeout=5, timer=5, pick=0}]" ,
                hero.getPerks().toString());

        hero.act();
        hero.up();
        field.tick();

        hero.up();
        field.tick();

        hero.right();
        field.tick();

        hero.right();
        field.tick();

        assertEquals("[{BOMB_BLAST_RADIUS_INCREASE('+') " +
                        "value=4, timeout=5, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // второй перк взятый в самый последний момент перед взрывом
        // бомбы повлияет не на нее, а на следующую бомбу
        int newValue = 3; // проверим, что эти значения суммируются
        int newTimeout = 7;
        player.getHero().addPerk(new BombBlastRadiusIncrease(newValue, newTimeout));

        assertEquals("[{BOMB_BLAST_RADIUS_INCREASE('+') " +
                        "value=7, timeout=8, timer=8, pick=0}]" ,
                hero.getPerks().toString());

        // when
        field.tick();

        // then
        asrtBrd("############\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "#҉# # # # ##\n" +
                "#҉         #\n" +
                "#҉# # # # ##\n" +
                "#҉ ☺       #\n" +
                "#҉# # # # ##\n" +
                "H҉҉҉҉҉҉    #\n" +
                "#H##########\n");

        // when
        hero.act();
        hero.right();
        field.tick();

        hero.right();
        field.tick();

        hero.up();
        field.tick();

        hero.up();
        field.tick();

        field.tick();

        // then
        asrtBrd("###H########\n" +
                "# #҉# # # ##\n" +
                "#  ҉       #\n" +
                "# #҉# # # ##\n" +
                "#  ҉       #\n" +
                "# #҉# # # ##\n" +
                "#  ҉ ☺     #\n" +
                "# #҉# # # ##\n" +
                "H҉҉҉҉҉҉҉҉҉҉H\n" +
                "# #҉# # # ##\n" +
                "   ҉       #\n" +
                "# #H########\n");

        assertEquals("[{BOMB_BLAST_RADIUS_INCREASE('+') " +
                        "value=7, timeout=8, timer=2, pick=0}]" ,
                hero.getPerks().toString());

        // when
        field.tick();

        // последний шанс воспользоваться, но мы не будем
        assertEquals("[{BOMB_BLAST_RADIUS_INCREASE('+') " +
                        "value=7, timeout=8, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        field.tick();

        assertEquals("[]" ,
                hero.getPerks().toString());

        // ставим новую бомбу, чтобы убедиться, что больше перк не сработает
        hero.act();
        field.tick();

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        // then
        asrtBrd("### ########\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # # # # ##\n" +
                "#          #\n" +
                "# # #҉# # ##\n" +
                "#   ҉Ѡ҉    #\n" +
                "# # #҉# # ##\n" +
                "            \n" +
                "# # # # # ##\n" +
                "           #\n" +
                "# # ########\n");

        assertEquals("[]" ,
                hero.getPerks().toString());
    }

    // BCI - Bomb Count Increase perk
    @Test
    public void shouldBombCountIncrease_whenBCIperk() {
        hero.act();
        // obe bomb by default on lel 1
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻    \n");

        hero.right();
        field.tick();
        hero.act();
        // no more bombs :(
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "4☺   \n");

        // add perk that gives 1+3 = 4 player's bombs in total on the board
        player.getHero().addPerk(new BombCountIncrease(3, 3));
        hero.act();
        hero.right();
        field.tick();
        hero.act();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "34☻  \n");

        hero.right();
        field.tick();
        hero.act();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "234☻ \n");

        hero.right();
        field.tick();
        hero.act();
        // 4 bombs and no more
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1234☺\n");

    }

    // BI - Bomb Immune perk
    @Test
    public void shouldHeroKeepAlive_whenBIperk() {
        hero.act();
        hero.right();
        field.tick();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "4☺   \n");

        player.getHero().addPerk(new BombImmune(6));

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉☺   \n");

        hero.act();
        field.tick();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n");

        field.tick();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
//                " 3☺  \n");
                " ☻   \n");

        field.tick();
        field.tick();
        field.tick();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "҉Ѡ҉  \n");
    }

    // BRC - Bomb remote control perk
    @Test
    public void shouldBombBlastOnAction_whenBRCperk_caseTwoBombs() {

        when(level.bombsCount()).thenReturn(2);
        player.getHero().addPerk(new BombRemoteControl(2, 1));

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=2, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // поставили первую радиоуправляемую бомбу
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "5☺   \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=2, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // видим, что она стоит и ждет
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "5    \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=2, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // взорвали ее
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉☺   \n" +
                "҉҉   \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // ставим еще одну
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n" +
                "     \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // отошли, смотрим
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                " ☺   \n" +
                " 5   \n" +
                "     \n");

        hero.left();
        field.tick();

        // долго потикали, ничего не меняется, таймаутов нет
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                " 5   \n" +
                "     \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // взорвали ее
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺҉   \n" +
                "҉҉҉  \n" +
                " ҉   \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // ставим новую
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☻    \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // если отойдем, то увидим, что это обычная
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "3☺   \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // еще одну, у нас ведь их две
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "24☺  \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // больше не могу
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "13 ☺ \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // еще не могу
        hero.right();
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "҉    \n" +
                "҉2  ☺\n" +  // взрывная волна кстати не перекрывает бомбу
                "҉    \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // и только когда ударная волна уйдет, тогда смогу
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                " 1  ☻\n" +  // взрывная волна кстати не перекрывает бомбу
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());
    }

    @Test
    public void shouldBombBlastOnAction_whenBRCperk_caseOneBomb() {

        when(level.bombsCount()).thenReturn(1);
        player.getHero().addPerk(new BombRemoteControl(2, 1));

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=2, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // поставили первую радиоуправляемую бомбу
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "5☺   \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=2, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // видим, что она стоит и ждет
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "5    \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=2, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // взорвали ее
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉☺   \n" +
                "҉҉   \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // ставим еще одну
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n" +
                "     \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // отошли, смотрим
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                " ☺   \n" +
                " 5   \n" +
                "     \n");

        hero.left();
        field.tick();

        // долго потикали, ничего не меняется, таймаутов нет
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                " 5   \n" +
                "     \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]" ,
                hero.getPerks().toString());

        // взорвали ее
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺҉   \n" +
                "҉҉҉  \n" +
                " ҉   \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // ставим новую
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☻    \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // если отойдем, то увидим, что это обычная
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "3☺   \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // больше не могу - у меня одна
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "2 ☺  \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // больше не могу
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "1  ☺ \n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // и теперь не могу - есть еще взрывная волна
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "҉    \n" +
                "҉҉  ☺\n" +
                "҉    \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());

        // а теперь пожалуйста
        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "    ☻\n" +
                "     \n" +
                "     \n");

        assertEquals("[]" ,
                hero.getPerks().toString());
    }

    @Test
    public void shouldSuicide_whenBRCperk_shouldRemoveAfterDeath_andCollectScores() {
        destroyWallAt(0, 1);
        meatChopperAt(3, 0);

        when(level.bombsCount()).thenReturn(1);
        when(level.bombsPower()).thenReturn(3);
        player.getHero().addPerk(new BombRemoteControl(1, 1));

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]",
                hero.getPerks().toString());

        // поставили радиоуправляемую бомбу
        hero.act();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "#    \n" +
                "5☺ & \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]",
                hero.getPerks().toString());

        // идем к митчоперу на верную смерть
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "#    \n" +
                "5 ☺& \n");

        assertEquals("[{BOMB_REMOTE_CONTROL('r') " +
                        "value=1, timeout=1, timer=1, pick=0}]",
                hero.getPerks().toString());

        // самоубился и всех выпилил )
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "H    \n" +
                "҉҉҉Ѡ \n");

        verifyAllEvents("[DIED, KILL_MEAT_CHOPPER, KILL_DESTROY_WALL]");

        // только сейчас перк забрался
        assertEquals("[]",
                hero.getPerks().toString());

        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "   Ѡ \n");

        verifyAllEvents("[]");

        assertEquals("[]",
                hero.getPerks().toString());
    }

    // чертики тоже могут ставить бомбы
    // Возможность проходить через стены. Бомбер прячется под ней для других польхзователей виден как стена, и только владелец видит себя там где он сейчас
    // Взорвать разом все бомбы на поле. Вместе с ней подрываются все бомбы на поле
    // Огнемет. Командой ACT + LEFT/RIGHT/UP/DOWN посылается ударная волна как от взрыва бомбы в заданном направлении на N клеточек
    // Возможность построить стену на месте бомбера. Сам бомбер при этом прячется под ней, как в модификаторе прохода через стену
    // Возможность запустить митчепера в заданном направлении. Командой ACT + LEFT/RIGHT/UP/DOWN посылается митчепера. Если этот митчопер поймает другого бомбермена, то очки засчитаются герою (если он жив до сих пор)
    // Атомная бомба которая прожигает стены насквозь с максимальной ударной волной. О форме ударной волны надо еще подумать
    // Создаеться клон который на протяжении какого-то времени будет ходить и рандомно ставить бомбы
}
