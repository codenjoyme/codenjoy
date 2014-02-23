package com;

/**
 * User: oleksandr.baglai
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Element {

    NONE(' '),       // например это пустое место, куда можно перейти герою
    WALL('☼'),       // а это стенка, через которую я хочу чтобы проходить нельзя было
    HERO('☺'),       // а это мой герой
    OTHER_HERO('☻'), // это герои других игроков
    DEAD_HERO('X'),  // а это временное явление - трупик моего героя, которое пропадет в следующем такте
    GOLD('$'),       // это то, за чем будет охота
    BOMB('x');       // а это бомба, на которой можно подорваться

    private char ch;

    Element(char ch) {
        this.ch = ch;
    }

    public char getChar() {
        return ch;
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such Elment for " + ch);
    }
}
