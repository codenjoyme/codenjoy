//
//  MainMenu.h
//  Bombermen
//
//  Created by Hell_Ghost on 10.09.13.
//  Copyright 2013 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "cocos2d.h"
#import "BombermanAPI.h"
#import "AIAnalyzer.h"

@interface MainMenu : CCLayer <UIAlertViewDelegate,BombermanAPIDelegate,AIAnalyzerDelegate> {
	NSMutableArray *redrawingObject;
	NSMutableArray *pathArray;
}
+ (CCScene*)scene;
@end
