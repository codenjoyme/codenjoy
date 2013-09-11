//
//  GameObject.m
//  Bombermen
//
//  Created by Hell_Ghost on 11.09.13.
//
//

#import "GameObject.h"

@implementation GameObject
@synthesize x;
@synthesize y;
@synthesize type;

+ (GameObject*)createWithSymbol:(NSString*)symbol {
		return [[[GameObject alloc] initWithSymbol:symbol] autorelease];
}

- (GameObject*)initWithSymbol:(NSString*)symbol {
	self = [super init];
	if (self) {
		type = NONE;
		if ([symbol isEqualToString:@" "]) {
			type = SPACE;
		} else
			if ([symbol isEqualToString:@"☼"]) {
				type = WALL;
			} else
				if ([symbol isEqualToString:@"#"]) {
					type = DESTROY_WALL;
				} else
					if ([symbol isEqualToString:@"H"]) {
						type = DESTROYED_WALL;
					} else
						if ([symbol isEqualToString:@"&"]) {
							type = MEAT_CHOPPER;
						} else
							if ([symbol isEqualToString:@"x"]) {
								type = DEAD_MEAT_CHOPPER;
							} else
								if ([symbol isEqualToString:@"☻"]) {
									type = BOMB_BOMBERMAN;
								} else
									if ([symbol isEqualToString:@"☺"]) {
										type = BOMBERMAN;
									} else
										if ([symbol isEqualToString:@"Ѡ"]) {
											type = DEAD_BOMBERMAN;
										} else
											if ([symbol isEqualToString:@"♥"]) {
												type = OTHER_BOMBERMAN;
											} else
												if ([symbol isEqualToString:@"♠"]) {
													type = OTHER_BOMB_BOMBERMAN;
												} else
													if ([symbol isEqualToString:@"♣"]) {
														type = OTHER_DEAD_BOMBERMAN;
													} else
														if ([symbol isEqualToString:@"҉"]) {
															type = BOOM;
														} else
															if ([symbol isEqualToString:@"1"]) {
																type = BOMB_TIMER_1;
															} else
																if ([symbol isEqualToString:@"2"]) {
																	type = BOMB_TIMER_2;
																} else
																	if ([symbol isEqualToString:@"3"]) {
																		type = BOMB_TIMER_3;
																	} else
																		if ([symbol isEqualToString:@"4"]) {
																			type = BOMB_TIMER_4;
																		} else
																			if ([symbol isEqualToString:@"5"]) {
																				type = BOMB_TIMER_5;
																			}

	}
	return self;
}
@end
