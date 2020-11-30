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

//  Created by Hell_Ghost on 10.09.13.

#import <Foundation/Foundation.h>

typedef enum{
	/// This is your Bomberman
	NONE = -1,
	BOMBERMAN = 0,              // this is what he usually looks like
	BOMB_BOMBERMAN,             // this is if he is sitting on own bomb
	DEAD_BOMBERMAN,             // oops, your Bomberman is dead (don't worry, he will appear somewhere in next move)
	                            // you're getting -200 for each death
	
	/// this is other players Bombermans
	OTHER_BOMBERMAN,		    // this is what other Bombermans looks like
	OTHER_BOMB_BOMBERMAN,	    // this is if player just set the bomb
	OTHER_DEAD_BOMBERMAN,	    // enemy corpse (it will disappear shortly, right on the next move)
                            	// if you've done it you'll get +1000
	
	/// the bombs
	BOMB_TIMER_5,               // after bomberman set the bomb, the timer starts (5 tacts)
	BOMB_TIMER_4,               // this will blow up after 4 tacts
	BOMB_TIMER_3,               // this after 3
	BOMB_TIMER_2,               // two
	BOMB_TIMER_1,               // one
	BOOM,		                // Boom! this is what is bomb does, everything that is destroyable got destroyed
	
	/// walls
	WALL,				        // indestructible wall - it will not fall from bomb
	DESTROY_WALL,		        // this wall could be blowed up
	DESTROYED_WALL,		        // this is how broken wall looks like, it will dissapear on next move
	                            // if it's you did it - you'll get +10 points.
	
	/// meatchoppers
	MEAT_CHOPPER,		        // this guys runs over the board randomly and gets in the way all the time
	                            // if it will touch bomberman - it will die
    DEAD_MEAT_CHOPPER,          // you'd better kill this piece of ... meat, you'll get +100 point for it
	                            // this is chopper corpse

    /// perks
    BOMB_BLAST_RADIUS_INCREASE, // Bomb blast radius increase. Applicable only to new bombs. The perk is temporary.
    BOMB_COUNT_INCREASE,        // Increase available bombs count. Number of extra bombs can be set in settings. Temporary.
    BOMB_REMOTE_CONTROL,        // Bomb blast not by timer but by second act. Number of RC triggers is limited and can be set in settings.
    BOMB_IMMUNE,                // Do not die after bomb blast (own bombs and others as well). Temporary.

    /// none
	NONE				        // this is the only place where you can move your Bomberman

}GameObjectType;

@interface GameObject : NSObject {
	int x;
	int y;
	GameObjectType type;
	BOOL isBarrier;
}

+ (GameObject*)createWithSymbol:(NSString*)symbol;
@property (nonatomic, readonly) BOOL isBarrier;
@property (nonatomic, readonly) GameObjectType type;
@property (nonatomic, assign) int x;
@property (nonatomic, assign) int y;

@end
