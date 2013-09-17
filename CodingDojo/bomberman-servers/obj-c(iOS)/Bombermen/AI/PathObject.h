//
//  PathObject.h
//  Bombermen
//
//  Created by Hell_Ghost on 16.09.13.
//
//

#import <Foundation/Foundation.h>
#import "GameObject.h"
@interface PathObject : NSObject {
	GameObject *node;
	PathObject *parent;
	BOOL isOpen;
	int g;
	int h;
	int f;
}

@property (nonatomic, assign) int g;
@property (nonatomic, assign) int h;
@property (nonatomic, assign) int f;

@property (nonatomic, retain) GameObject *node;
@property (nonatomic, retain) PathObject *parent;
@property (nonatomic, assign) BOOL isOpen;
@end
