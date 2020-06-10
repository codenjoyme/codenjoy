package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.model.perks.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class PerksBombermanTest extends AbstractBombermanTest {

    // Perks related test here
    @Test
    public void shouldPerkBeDropped_whenWallIsDestroyed() {
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
        field.tick();

        asrtBrd("######\n" +
                "# # ##\n" +
                "# ☺  #\n" +
                "#҉# ##\n" +
                "+҉҉  #\n" +
                "#H####\n");
    }

    @Test
    public void shouldBombermanAcquirePerk_whenMoveToFieldWithPerk() {
        // BBRI = Bomb Blast Radius Increase perk

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
        field.tick();

        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "#҉# ##\n" +
                "+҉҉☺ #\n" +
                "#+####\n");

        field.tick();

        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "+  ☺ #\n" +
                "#+####\n");

        // go for perk
        hero.left();
        field.tick();
        hero.left();
        field.tick();
        hero.left();
        field.tick();

        asrtBrd("######\n" +
                "# # ##\n" +
                "#    #\n" +
                "# # ##\n" +
                "☺    #\n" +
                "#+####\n");

        assertEquals("Hero had to acquire new perk", 1, player.getHero().getPerks().size());

    }

    @Test
    public void shouldPerkBeDeactivated_whenTimeout() {
        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 4, 3);
        PerksSettingsWrapper.setDropRatio(20);
        player.getHero().addPerk(new BombBlastRadiusIncrease(4,3));
        assertEquals("Hero had to acquire new perk", 1, player.getHero().getPerks().size());

        field.tick();
        field.tick();
        field.tick();

        assertEquals("Hero had to loose perk", 0, player.getHero().getPerks().size());
    }

    @Test
    public void shouldBombBlastRadiusIncrease_whenBBRIperk() {
        givenBoardWithDestroyWalls(6);
        player.getHero().addPerk(new BombBlastRadiusIncrease(4,3));

        hero.act();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        asrtBrd("#H####\n" +
                "#҉# ##\n" +
                "#҉   #\n" +
                "#҉# ##\n" +
                "HѠ҉҉҉H\n" +
                "#H####\n");
    }

    @Test
    public void shouldBombCountIncrease_whenBCIperk() {
        // Bomb Count Increase perk
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
        player.getHero().addPerk(new BombCountIncrease(3,3));
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

    @Test
    public void shouldHeroKeepAlive_whenBIperk() {
        // Bomb Immune perk
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

    @Test
    public void shouldBombBlastOnAction_whenBRCperk() {
        // Bomb remote control (BRC)

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
