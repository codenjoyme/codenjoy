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
﻿#include "Element.h"

Element::Element(Char el) {
	elem.first = valueOf(el);
	elem.second = el;
}

Element::Element(String name) {
	elem.second = Elements.at(name);
	elem.first = name;
}

Char Element::getChar() const {
	return elem.second;
}

String Element::valueOf(Char ch) const {
	for (auto i : Elements) {
		if (i.second == ch) return i.first;
	}
	throw std::invalid_argument("Element::valueOf(Char ch): No such Elment for " + ch);
}

bool Element::operator==(const Element& el) const {
	return elem == el.elem;
}

ElementMap Element::initialiseElements() {
	ElementMap mapOfElements;

	/// This is your Bomberman
	mapOfElements[LL("BOMBERMAN")] =  LL('☺');                 // this is what he usually looks like
	mapOfElements[LL("BOMB_BOMBERMAN")] =  LL('☻');            // this is if he is sitting on own bomb
	mapOfElements[LL("DEAD_BOMBERMAN")] =  LL('Ѡ');            // oops, your Bomberman is dead  (don't worry, he will appear somewhere in next move
													           // you're getting -200 for each death

	/// this is other players Bombermans
	mapOfElements[LL("OTHER_BOMBERMAN")] =  LL('♥');           // this is what other Bombermans looks like
	mapOfElements[LL("OTHER_BOMB_BOMBERMAN")] =  LL('♠');      // this is if player just set the bomb
	mapOfElements[LL("OTHER_DEAD_BOMBERMAN")] =  LL('♣');      // enemy corpse , it will disappear shortly, right on the next move
								                               // if you've done it you'll get +1000

	/// the bombs
	mapOfElements[LL("BOMB_TIMER_5")] =  LL('5');              // after bomberman set the bomb, the timer starts  ("5 tacts
	mapOfElements[LL("BOMB_TIMER_4")] =  LL('4');              // this will blow up after 4 tacts
	mapOfElements[LL("BOMB_TIMER_3")] =  LL('3');              // this after 3
	mapOfElements[LL("BOMB_TIMER_2")] =  LL('2');              // two
	mapOfElements[LL("BOMB_TIMER_1")] =  LL('1');              // one
	mapOfElements[LL("BOOM")] =  LL('҉');                      // Boom! this is what is bomb does, everything that is destroyable got destroyed

	/// walls
	mapOfElements[LL("WALL")] =  LL('☼');                      // indestructible wall - it will not fall from bomb
	mapOfElements[LL("DESTROY_WALL")] =  LL('#');              // this wall could be blowed up
	mapOfElements[LL("DESTROYED_WALL")] =  LL('H');            // this is how broken wall looks like, it will dissapear on next move
								                               // if it's you did it - you'll get +10 points.

	/// meatchoppers
	mapOfElements[LL("MEAT_CHOPPER")] =  LL('&');              // this guys runs over the board randomly and gets in the way all the time
													           // if it will touch bomberman - it will die
													           // you'd better kill this piece of ... meat, you'll get +100 point for it
	mapOfElements[LL("DEAD_MEAT_CHOPPER")] =  LL('x');         // this is chopper corpse

    /// perks
    mapOfElements[LL("BOMB_BLAST_RADIUS_INCREASE")] = LL('+'); // Bomb blast radius increase. Applicable only to new bombs. The perk is temporary.
    mapOfElements[LL("BOMB_COUNT_INCREASE")]        = LL('c'); // Increase available bombs count. Number of extra bombs can be set in settings. Temporary.
    mapOfElements[LL("BOMB_REMOTE_CONTROL")]        = LL('r'); // Bomb blast not by timer but by second act. Number of RC triggers is limited and can be set in settings.
    mapOfElements[LL("BOMB_IMMUNE")]                = LL('i'); // Do not die after bomb blast (own bombs and others as well). Temporary.

	/// a void
	mapOfElements[LL("NONE")] =  LL(' ');                      // this is the only place where you can move your Bomberman
	return mapOfElements;
};

const ElementMap Element::Elements = Element::initialiseElements();
