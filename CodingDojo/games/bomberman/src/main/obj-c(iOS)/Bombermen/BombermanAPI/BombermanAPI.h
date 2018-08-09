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
#import "GameObject.h"
#import "SRWebSocket.h"

#define DRAW_MODE 1

typedef enum{
	Idle,
	Up,
	Down,
	Left,
	Right
}Direction;

@protocol BombermanAPIDelegate;
@interface BombermanAPI : NSObject <SRWebSocketDelegate> {
	id <BombermanAPIDelegate> delegate;
@private
	NSString *userName;
	SRWebSocket *_webSocket;
	
	GameObject *bomber;
	BOOL isDead;
	NSMutableArray *allObjects;
	NSMutableArray *allBarriers;
	NSMutableArray *meatchopers;
	NSMutableArray *walls;
	NSMutableArray *destroyWalls;
	NSMutableArray *enemies;
	NSMutableArray *bombs;
	int boardSize;
}
#pragma mark - GETTERS PROPERTY
@property (nonatomic, assign) id <BombermanAPIDelegate> delegate;
@property (nonatomic, readonly) NSString *userName; // Возвращает имя юзера
@property (nonatomic, readonly) int boardSize; // Возвращает размер доски
@property (nonatomic, readonly) GameObject *bomber; // Возвращает объект бомбера
@property (nonatomic, readonly) BOOL isDead; // Проверяет жив ли мой бомбер

+ (BombermanAPI*)sharedApi; // Инстанс API
- (void)newGameWithUserName:(NSString*)name; // Создает новую игру с указанным именем

#pragma mark - GETTERS METHODS -
- (NSArray*)getWalls; // Возвращает бетонные(Неразрушимые) стены
- (NSArray*)getMeatChoppers; // Возвращает ботов
- (NSArray*)getDestroyWalls; // Возвращает разрушаемые стены
- (NSArray*)getOtherBombers; // Возвращает вражеских бомберов
- (NSArray*)getBombs; // Возвращает бомбы
- (NSArray*)getBarriers; // Возвращает непроходимые места
- (GameObject*)objectInCoordinates:(int)x y:(int)y; // Возвращает обьект по текущим координатам
- (BOOL)isElement:(GameObjectType)type atX:(int)x y:(int)y; // находится ли в позиции  x, y заданный элемент?
- (BOOL)isBarrierAtPointX:(int)x y:(int)y; // Координаты всех объектов препятствующих движению
- (BOOL)isElementsInPositionX:(int)x y:(int)y ofElementsType:(NSArray*)elements; // находится ли в позиции  x, y что-нибудь из заданного набора
- (BOOL)isElementNear:(GameObjectType)element atX:(int)x y:(int)y; // Есть ли вокруг заданный елемент
- (NSArray*)nearElementsAtX:(int)x y:(int)y; // Рядом стоящие обьекты
- (int)nearCountOfElementType:(GameObjectType)element atX:(int)x y:(int)y; // сколько элементов заданного типа есть вокруг клетки с x, y
- (int)nearCountAtX:(int)x y:(int)y ofElementsType:(NSArray*)elements; // сколько элементов заданного типа есть вокруг клетки с x, y
- (NSArray*)getFutureBlasts; // координаты потенциально опасных мест, где бомба может разорваться.
#pragma mark -

#pragma mark - SETTER METODS -
- (void)setDirection:(Direction)dir withAction:(BOOL)act;
@end

@protocol BombermanAPIDelegate <NSObject>

@optional
- (void)stepIsOver; //Вызывается когда данные получены и распарсены
#if DRAW_MODE
// Используется для отрисовки
- (void)wallDataReceived:(NSArray*)wallData;
- (void)redrawElemet:(GameObject*)element;
- (void)clearField;
#endif
@end
