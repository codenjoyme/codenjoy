package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.model.perks.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PerksBombermanTest extends AbstractBombermanTest {

    @Test
    public void shouldPerkBeDropped_whenWallIsDestroyed() {
        // given
        givenBoardWithDestroyWalls(6);
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 5, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%
        when(bombermanDice.next(anyInt())).thenReturn(10, 30); // must drop 1 perk

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
                "+҉҉  #\n" +
                "#H####\n");
    }

    // BBRI = Bomb Blast Radius Increase perk
    // проверяем, что перков может появиться два
    // проверяем, что перки не пропадают на следующий тик
    // проверяем, что перк можно подобрать
    @Test
    public void shouldBombermanAcquirePerk_whenMoveToFieldWithPerk() {
        // given
        givenBoardWithDestroyWalls(6);

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 4, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%

        when(bombermanDice.next(anyInt())).thenReturn(10); // must drop 2 perks

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
                "+҉҉☺ #\n" +
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

        // when
        // go for perk
        hero.left();
        field.tick();

        hero.left();
        field.tick();

        hero.left();
        field.tick();

        // then
        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "☺    #\n" +
                "#+####\n");

        assertEquals("Hero had to acquire new perk", 1, player.getHero().getPerks().size());
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
                        "value=4, timeout=5, timer=1, pick=-4}]" ,
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
                        "value=7, timeout=8, timer=2, pick=-6}]" ,
                hero.getPerks().toString());

        // when
        field.tick();

        // последний шанс воспользоваться, но мы не будем
        assertEquals("[{BOMB_BLAST_RADIUS_INCREASE('+') " +
                        "value=7, timeout=8, timer=1, pick=-7}]" ,
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
    public void shouldBombBlastOnAction_whenBRCperk() {

        when(level.bombsCount()).thenReturn(3);
        player.getHero().addPerk(new BombRemoteControl(2));

        hero.act();
        hero.right();
        field.tick();
//        hero.act();
//        field.tick();
        hero.up();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "5    \n");

        hero.act();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉☻   \n" +
                "҉҉   \n");

        hero.up();
        field.tick();
        hero.left();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                " 5   \n" +
                "     \n");

        hero.act();
        field.tick();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☻    \n" +
                "     \n" +
                "     \n");

        hero.right();
        field.tick();
        hero.act();
        field.tick();
        hero.right();
        field.tick();

        asrtBrd("     \n" +
                "     \n" +
                "33☺  \n" +
                "     \n" +
                "     \n");
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
