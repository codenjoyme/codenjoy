package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.hero.HeroData;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by indigo on 2018-07-05.
 */
public class SingleTest {

    private GameField field;
    private GamePlayer player;
    private PrinterFactory factory;
    private Joystick joystick;
    private Single single;
    private PlayerHero hero;
    private HeroData heroData;

    @Before
    public void setup() {
        field = mock(GameField.class);
        player = mock(GamePlayer.class);
        factory = mock(PrinterFactory.class);
        joystick = mock(Joystick.class);
        hero = mock(PlayerHero.class);
        heroData = mock(HeroData.class);

        when(player.getHero()).thenReturn(hero);

        single = new Single(field, player, factory);
    }

    @Test
    public void callJoystickFromPlayer_ifRealizedInPlayer() {
        // given
        when(player.getJoystick()).thenReturn(joystick);

        // when
        Joystick joystick = single.getJoystick();

        // then
        assertSame(this.joystick, joystick);
    }

    @Test
    public void callJoystickFromHero_ifNotRealizedInPlayer() {
        // given
        when(player.getJoystick()).thenReturn(null);

        // when
        Joystick joystick = single.getJoystick();

        // then
        assertSame(this.hero, joystick);
    }

    @Test
    public void callHeroDataFromPlayer_ifRealizedInPlayer() {
        // given
        when(player.getHeroData()).thenReturn(heroData);

        // when
        HeroData heroData = single.getHero();

        // then
        assertSame(this.heroData, heroData);
    }

    @Test
    public void callHeroDataFromHero_ifNotRealizedInPlayer_singleplayer() {
        // given
        when(player.getHeroData()).thenReturn(null);
        when(hero.getX()).thenReturn(3);
        when(hero.getY()).thenReturn(5);

        // when
        HeroData heroData = single.getHero();

        // then
        assertEquals("HeroData[coordinate=[3,5], level=0, " +
                        "multiplayer=false, additionalData=null]",
                heroData.toString());
    }

    @Test
    public void callHeroDataFromHero_ifNotRealizedInPlayer_multiplayer() {
        // given
        single = new Single(field, player, factory, MultiplayerType.MULTIPLE);

        when(player.getHeroData()).thenReturn(null);
        when(hero.getX()).thenReturn(7);
        when(hero.getY()).thenReturn(9);

        // when
        HeroData heroData = single.getHero();

        // then
        assertEquals("HeroData[coordinate=[7,9], level=0, " +
                        "multiplayer=true, additionalData=null]",
                heroData.toString());
    }
}
