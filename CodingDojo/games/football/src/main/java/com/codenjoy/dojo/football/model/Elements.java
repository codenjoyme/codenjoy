package com.codenjoy.dojo.football.model;

import com.codenjoy.dojo.services.CharElements;

/**
 * Тут указана легенда всех возможных объектов на поле и их состояний.
 * Важно помнить, что для каждой енумной константы надо создать спрайт в папке \src\main\webapp\resources\sprite.
 */
public enum Elements implements CharElements {

	NONE(' '),       // например это пустое место, куда можно перейти герою
    WALL('☼'),       // а это внешняя разметка поля, через которую я хочу чтобы проходить нельзя было
    HERO('☺'),		 // а это мой герой
    HERO_W_BALL('☻'),// герой с мячом
    BALL('*'),		 // а это мяч
    STOPPED_BALL('∙'),		 // а это маленький мяч (остановленный)
    TOP_GOAL('┴'),	 // верхние ворота
    BOTTOM_GOAL('┬'),// нижние ворота
    MY_GOAL('='),	 // мои ворота
    ENEMY_GOAL('⌂'), // чужие ворота
    HITED_GOAL('x'), // гол в ворота
    HITED_MY_GOAL('#'), // гол в мои ворота
    TEAM_MEMBER('♦'),		 // член моей команды
    TEAM_MEMBER_W_BALL('♥'), // член моей команды с мячем
    ENEMY('♣'),		 		 // член второй команды
    ENEMY_W_BALL('♠');		 // член второй команды с мячем
    
    final char ch;

    Elements(char ch) {
        this.ch = ch;
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
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
