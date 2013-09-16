//
//  AIAnalyzer.m
//  Bombermen
//
//  Created by Hell_Ghost on 15.09.13.
//
//

#import "AIAnalyzer.h"


static AIAnalyzer* sharedAnalizer = nil;
@implementation AIAnalyzer
@synthesize delegate;
+ (AIAnalyzer*)sharedAnalizer {
	if (!sharedAnalizer) {
		sharedAnalizer = [[AIAnalyzer alloc] init];
	}
	return sharedAnalizer;
}

- (id)init
{
    self = [super init];
    if (self) {
        openList = [[NSMutableArray alloc] init];
		closedList = [[NSMutableArray alloc] init];
		currentPath = [[NSMutableArray alloc] init];
    }
    return self;
}

- (void)analyze {
	if (![BombermanAPI sharedApi].isDead) {
		bomber = [BombermanAPI sharedApi].bomber;
		
		int cnt = [[BombermanAPI sharedApi] nearCountAtX:bomber.x y:bomber.y ofElementsType:[NSArray arrayWithObjects:[NSNumber numberWithInt:WALL],[NSNumber numberWithInt:DESTROY_WALL],[NSNumber numberWithInt:OTHER_BOMB_BOMBERMAN], nil]];
		if (cnt>=4) {
			[[BombermanAPI sharedApi] setDirection:Idle withAction:YES];
		} else {
			GameObject* nc = [self nearChopper];
			//NSLog(@"%d %d",nc.x,nc.y);
			//GameObject *ne = [self nearEnemy];
			//NSLog(@"%d %d",ne.x,ne.y);
			if ([self findPathFrom:bomber ToGameObject:nc] && [currentPath count]) {
				[[BombermanAPI sharedApi] setDirection:[self transformToDitection:bomber goal:[currentPath lastObject]] withAction:(arc4random()%3 == 2)];
			}
		}
	}
}

- (Direction)transformToDitection:(GameObject*)start goal:(GameObject*)goal {
	if (start.x == goal.x && start.y-1 == goal.y) {
		return Up;
	}
	if (start.x == goal.x && start.y+1 == goal.y) {
		return Down;
	}
	if (start.x-1 == goal.x && start.y == goal.y) {
		return Left;
	}
	if (start.x+1 == goal.x && start.y == goal.y) {
		return Right;
	}
	return Idle;
}

- (GameObject*)nearChopper {
	NSArray *chopers = [[BombermanAPI sharedApi] getMeatChoppers];
	GameObject * near = nil;
	for (GameObject *obj in chopers) {
		if (near == nil || ([self heuristicCost:near goal:bomber] > [self heuristicCost:obj goal:bomber])) {
			near = obj;
		}
	}
	return near;
}

- (GameObject*)nearEnemy {
	NSArray * enemy = [[BombermanAPI sharedApi] getOtherBombers];
	GameObject * near = nil;
	for (GameObject *obj in enemy) {
		if (near == nil || ([self heuristicCost:near goal:bomber] > [self heuristicCost:obj goal:bomber])) {
			near = obj;
		}
	}
	return near;
}

- (BOOL)findPathFrom:(GameObject*)start ToGameObject:(GameObject*)goal{
	[openList removeAllObjects];
	[closedList removeAllObjects];
	[currentPath removeAllObjects];
	
	PathObject * startObj = [[[PathObject alloc] init] autorelease];
	startObj.parent = nil;
	startObj.node = start;
	startObj.g = 0;
	startObj.h = [self heuristicCost:startObj.node goal:goal];
	startObj.f = startObj.g+startObj.h;
	[openList addObject:startObj];
	BOOL complete = NO;
	while ([openList count] && !complete) {
		int fMin = INT16_MAX;
		PathObject *current = nil;
		for (PathObject *obj in openList) {
			if (obj.f<fMin) {
				fMin = obj.f;
				current = obj;
				if (DRAW_MODE && [delegate respondsToSelector:@selector(drawPathPoint:)]) {
					[delegate drawPathPoint:[NSArray arrayWithObject:current]];
				}
			}
		}
		[closedList addObject:current];
		[openList removeObject:current];
		
		NSArray * nearPoint = [[BombermanAPI sharedApi] nearElementsAtX:current.node.x y:current.node.y];
		for (GameObject *obj in nearPoint) {
			complete = ((obj.x == goal.x) && (obj.y == goal.y));
			if (complete) {
				PathObject *parent = current.parent;
				while (parent) {
					[currentPath addObject:current.node];
					current = parent;
					parent = current.parent;
				}
				NSLog(@"Path Finded %d",[currentPath count]);
				break;
			} else
			if (!obj.isBarrier && ![self inOpenList:obj] && ![self inClosedList:obj]) {
				PathObject *pathObj = [[[PathObject alloc] init] autorelease];
				pathObj.parent = current;
				pathObj.node = obj;
				pathObj.g = 10 + pathObj.parent.g;
				pathObj.h = [self heuristicCost:pathObj.node goal:goal];
				pathObj.f = pathObj.g+pathObj.h;
				[openList addObject:pathObj];
//				if (DRAW_MODE && [delegate respondsToSelector:@selector(drawPathPoint:)]) {
//					[delegate drawPathPoint:[NSArray arrayWithObject:pathObj]];
//				}
			}
		}
	}
	return complete;
}

- (BOOL)inOpenList:(GameObject*)obj {
	for (PathObject * pathObj in openList) {
		if (pathObj.node == obj) {
			return YES;
		}
	}
	return NO;
}

- (BOOL)inClosedList:(GameObject*)obj {
	for (PathObject * pathObj in closedList) {
		if (pathObj.node == obj) {
			return YES;
		}
	}
	return NO;
}

- (int)heuristicCost:(GameObject*)start goal:(GameObject*)goal {
	return (abs(start.x-goal.x)+abs(start.y-goal.y));
}
@end
