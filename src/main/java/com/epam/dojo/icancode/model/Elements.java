package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.CharElements;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.model.items.Box;
import com.epam.dojo.icancode.model.items.Exit;
import com.epam.dojo.icancode.model.items.Floor;
import com.epam.dojo.icancode.model.items.Gold;
import com.epam.dojo.icancode.model.items.Hero;
import com.epam.dojo.icancode.model.items.Hole;
import com.epam.dojo.icancode.model.items.Laser;
import com.epam.dojo.icancode.model.items.LaserMachine;
import com.epam.dojo.icancode.model.items.None;
import com.epam.dojo.icancode.model.items.Start;
import com.epam.dojo.icancode.model.items.Wall;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {
    EMPTY('-', None.class),
    FLOOR('.', Floor.class),

    ANGLE_IN_LEFT('╔', Wall.class),
    WALL_FRONT('═', Wall.class),
    ANGLE_IN_RIGHT('┐', Wall.class),
    WALL_RIGHT('│', Wall.class),
    ANGLE_BACK_RIGHT('┘', Wall.class),
    WALL_BACK('─', Wall.class),
    ANGLE_BACK_LEFT('└', Wall.class),
    WALL_LEFT('║', Wall.class),
    WALL_BACK_ANGLE_LEFT('┌', Wall.class),
    WALL_BACK_ANGLE_RIGHT('╗', Wall.class),
    ANGLE_OUT_RIGHT('╝', Wall.class),
    ANGLE_OUT_LEFT('╚', Wall.class),
    SPACE(' ', Wall.class),

    ROBO('☺', Hero.class),
    ROBO_FALLING('o', Hero.class),
    ROBO_FLYING('*', Hero.class),
    ROBO_LASER('☻', Hero.class),

    ROBO_OTHER('X', Hero.class),
    ROBO_OTHER_FALLING('x', Hero.class),
    ROBO_OTHER_FLYING('^', Hero.class),
    ROBO_OTHER_LASER('&', Hero.class),

    LASER_MACHINE_CHARGING_LEFT('˂', LaserMachine.class),
    LASER_MACHINE_CHARGING_RIGHT('˃', LaserMachine.class),
    LASER_MACHINE_CHARGING_UP('˄', LaserMachine.class),
    LASER_MACHINE_CHARGING_DOWN('˅', LaserMachine.class),

    LASER_MACHINE_READY_LEFT('◄', LaserMachine.class),
    LASER_MACHINE_READY_RIGHT('►', LaserMachine.class),
    LASER_MACHINE_READY_UP('▲', LaserMachine.class),
    LASER_MACHINE_READY_DOWN('▼', LaserMachine.class),

    LASER_LEFT('←', Laser.class),
    LASER_RIGHT('→', Laser.class),
    LASER_UP('↑', Laser.class),
    LASER_DOWN('↓', Laser.class),

    START('S', Start.class),
    EXIT('E', Exit.class),
    GOLD('$', Gold.class),
    HOLE('O', Hole.class),
    BOX('B', Box.class),
    FOG('F', Wall.class);

    private static volatile Dictionary<String, Elements> elementsMap;

    public final Class<BaseItem> itsClass;

    private final char ch;

    Elements(char ch, Class itsClass) {
        this.ch = ch;
        this.itsClass = itsClass;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        if (elementsMap == null) {
            makeElementsMap();
        }

        Elements result = elementsMap.get(String.valueOf(ch));

        if (result == null) {
            throw new IllegalArgumentException("No such element for " + ch);
        }

        return result;
    }

    private static void makeElementsMap() {
        elementsMap = new Hashtable<>();

        for (Elements el : Elements.values()) {
            elementsMap.put(el.toString(), el);
        }
    }
}
