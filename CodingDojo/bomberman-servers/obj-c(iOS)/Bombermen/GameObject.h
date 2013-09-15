//
//  GameObject.h
//  Bombermen
//
//  Created by Hell_Ghost on 11.09.13.
//
//

#import <Foundation/Foundation.h>

typedef enum{
	/// This is your Bomberman
	NONE = -1,
	BOMBERMAN = 0,  // this is what he usually looks like
	BOMB_BOMBERMAN, // this is if he is sitting on own bomb
	DEAD_BOMBERMAN, // oops, your Bomberman is dead (don't worry, he will appear somewhere in next move)
	// you're getting -200 for each death
	
	/// this is other players Bombermans
	OTHER_BOMBERMAN,		// this is what other Bombermans looks like
	OTHER_BOMB_BOMBERMAN,	// this is if player just set the bomb
	OTHER_DEAD_BOMBERMAN,	// enemy corpse (it will disappear shortly, right on the next move)
	// if you've done it you'll get +1000
	
	/// the bombs
	BOMB_TIMER_5, // after bomberman set the bomb, the timer starts (5 tacts)
	BOMB_TIMER_4, // this will blow up after 4 tacts
	BOMB_TIMER_3, // this after 3
	BOMB_TIMER_2, // two
	BOMB_TIMER_1, // one
	BOOM,		  // Boom! this is what is bomb does, everything that is destroyable got destroyed
	
	/// walls
	WALL,				// indestructible wall - it will not fall from bomb
	DESTROY_WALL,		// this wall could be blowed up
	DESTROYED_WALL,		// this is how broken wall looks like, it will dissapear on next move
	// if it's you did it - you'll get +10 points.
	
	/// meatchoppers
	MEAT_CHOPPER,		// this guys runs over the board randomly and gets in the way all the time
	// if it will touch bomberman - it will die
	// you'd better kill this piece of ... meat, you'll get +100 point for it
	DEAD_MEAT_CHOPPER,	// this is chopper corpse
	
	SPACE				// this is the only place where you can move your Bomberman
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
