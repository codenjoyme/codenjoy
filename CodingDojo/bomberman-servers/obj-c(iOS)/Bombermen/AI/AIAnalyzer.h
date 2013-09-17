//
//  AIAnalyzer.h
//  Bombermen
//
//  Created by Hell_Ghost on 15.09.13.
//
//

#import <Foundation/Foundation.h>
#import "BombermanAPI.h"
#import "PathObject.h"

@protocol AIAnalyzerDelegate;
@interface AIAnalyzer : NSObject {
	id <AIAnalyzerDelegate> delegate;	
@private
	GameObject *bomber;
	NSMutableArray *openList;
	NSMutableArray *closedList;
	NSMutableArray *currentPath;
}
@property (nonatomic, assign) id <AIAnalyzerDelegate> delegate;
+ (AIAnalyzer*)sharedAnalizer;
- (void)analyze;
@end

@protocol AIAnalyzerDelegate <NSObject>

@optional
- (void)drawPathPoint:(NSArray*)pathPoint;
@end
