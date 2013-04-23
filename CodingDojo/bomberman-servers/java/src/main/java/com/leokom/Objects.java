package com.leokom;

/**
 * User: sanja
 * Date: 21.04.13
 * Time: 3:10
 */
public enum Objects {
    BOMBERMAN('☺'),
    BOMB_BOMBERMAN('☻'),
    DEAD_BOMBERMAN('Ѡ'),

    OTHER_BOMBERMAN('♥'),
    OTHER_BOMB_BOMBERMAN('♠'),
    OTHER_DEAD_BOMBERMAN('♣'),

    BOOM('҉'),
    BOMB_TIMER_1('1'),
    BOMB_TIMER_2('2'),
    BOMB_TIMER_3('3'),
    BOMB_TIMER_4('4'),
    BOMB_TIMER_5('5'),

    WALL('☼'),
    DESTROY_WALL('#'),
    DESTROYED_WALL('H'),

    MEAT_CHOPPER('&'),
    DEAD_MEAT_CHOPPER('x'),

    SPACE(' ');

    private char ch;
    Objects(char ch) {
        this.ch = ch;
    }
    
    public static Objects forChar( char c ) {
    	for ( Objects objects : Objects.values() ) {
    		if ( objects.ch == c ) {
    			return objects;
    		}
    	}
    	
    	throw new IllegalArgumentException( "Char is not supported: " + c );
    }
}
