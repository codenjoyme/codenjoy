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
@synthesize isBarrier;

+ (GameObject*)createWithSymbol:(NSString*)symbol {
		return [[[GameObject alloc] initWithSymbol:symbol] autorelease];
}

- (GameObject*)initWithSymbol:(NSString*)symbol {
	self = [super init];
	if (self) {
		type = NONE;
		isBarrier = NO;
		if ([symbol isEqualToString:@" "]) {
			type = NONE;
		} else
			if ([symbol isEqualToString:@"☼"]) {
				type = WALL;
				isBarrier = YES;
			} else
				if ([symbol isEqualToString:@"#"]) {
					type = DESTROY_WALL;
					isBarrier = YES;
				} else
					if ([symbol isEqualToString:@"H"]) {
						type = DESTROYED_WALL;
						isBarrier = YES;
					} else
						if ([symbol isEqualToString:@"&"]) {
							type = MEAT_CHOPPER;
							isBarrier = YES;
						} else
							if ([symbol isEqualToString:@"x"]) {
								type = DEAD_MEAT_CHOPPER;
							} else
								if ([symbol isEqualToString:@"☻"]) {
									type = BOMB_BOMBERMAN;
									isBarrier = YES;
								} else
									if ([symbol isEqualToString:@"☺"]) {
										type = BOMBERMAN;
										//isBarier = YES;
									} else
										if ([symbol isEqualToString:@"Ѡ"]) {
											type = DEAD_BOMBERMAN;
										} else
											if ([symbol isEqualToString:@"♥"]) {
												type = OTHER_BOMBERMAN;
												isBarrier = YES;
											} else
												if ([symbol isEqualToString:@"♠"]) {
													type = OTHER_BOMB_BOMBERMAN;
													isBarrier = YES;
												} else
													if ([symbol isEqualToString:@"♣"]) {
														type = OTHER_DEAD_BOMBERMAN;
													} else
														if ([symbol isEqualToString:@"҉"]) {
															type = BOOM;
														} else
															if ([symbol isEqualToString:@"1"]) {
																type = BOMB_TIMER_1;
																isBarrier = YES;
															} else
																if ([symbol isEqualToString:@"2"]) {
																	type = BOMB_TIMER_2;
																	isBarrier = YES;
																} else
																	if ([symbol isEqualToString:@"3"]) {
																		type = BOMB_TIMER_3;
																		isBarrier = YES;
																	} else
																		if ([symbol isEqualToString:@"4"]) {
																			type = BOMB_TIMER_4;
																			isBarrier = YES;
																		} else
																			if ([symbol isEqualToString:@"5"]) {
																				type = BOMB_TIMER_5;
																				isBarrier = YES;
																			} else
                                                                                if ([symbol isEqualToString:@"+"]) {
                                                                                    type = BOMB_BLAST_RADIUS_INCREASE;
                                                                                    isBarrier = NO;
                                                                                } else
                                                                                    if ([symbol isEqualToString:@"c"]) {
                                                                                        type = BOMB_COUNT_INCREASE;
                                                                                        isBarrier = NO;
                                                                                    } else
                                                                                        if ([symbol isEqualToString:@"r"]) {
                                                                                            type = BOMB_REMOTE_CONTROL;
                                                                                            isBarrier = NO;
                                                                                        } else
                                                                                            if ([symbol isEqualToString:@"i"]) {
                                                                                                type = BOMB_IMMUNE;
                                                                                                isBarrier = NO;
                                                                                            }

	}
	return self;
}
@end
