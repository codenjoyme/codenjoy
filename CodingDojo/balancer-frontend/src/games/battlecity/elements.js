// vendor
import React, { Component } from 'react';

import ai_tank_down from './images/sprite/ai_tank_down.png';
import ai_tank_left from './images/sprite/ai_tank_left.png';
import ai_tank_prize from './images/sprite/ai_tank_prize.png';
import ai_tank_right from './images/sprite/ai_tank_right.png';
import ai_tank_up from './images/sprite/ai_tank_up.png';
import bang from './images/sprite/bang.png';
import battle_wall from './images/sprite/battle_wall.png';
import bullet from './images/sprite/bullet.png';
import ice from './images/sprite/ice.png';
import none from './images/sprite/none.png';
import other_tank_down from './images/sprite/other_tank_down.png';
import other_tank_left from './images/sprite/other_tank_left.png';
import other_tank_right from './images/sprite/other_tank_right.png';
import other_tank_up from './images/sprite/other_tank_up.png';
import prize from './images/sprite/prize.png';
import prize_breaking_walls from './images/sprite/prize_breaking_walls.png';
import prize_immortality from './images/sprite/prize_immortality.png';
import prize_walking_on_water from './images/sprite/prize_walking_on_water.png';
import river from './images/sprite/river.png';
import tank_down from './images/sprite/tank_down.png';
import tank_left from './images/sprite/tank_left.png';
import tank_right from './images/sprite/tank_right.png';
import tank_up from './images/sprite/tank_up.png';
import tree from './images/sprite/tree.png';
import wall from './images/sprite/wall.png';
import wall_destroyed from './images/sprite/wall_destroyed.png';
import wall_destroyed_down from './images/sprite/wall_destroyed_down.png';
import wall_destroyed_down_left from './images/sprite/wall_destroyed_down_left.png';
import wall_destroyed_down_right from './images/sprite/wall_destroyed_down_right.png';
import wall_destroyed_down_twice from './images/sprite/wall_destroyed_down_twice.png';
import wall_destroyed_left from './images/sprite/wall_destroyed_left.png';
import wall_destroyed_left_right from './images/sprite/wall_destroyed_left_right.png';
import wall_destroyed_left_twice from './images/sprite/wall_destroyed_left_twice.png';
import wall_destroyed_right from './images/sprite/wall_destroyed_right.png';
import wall_destroyed_right_twice from './images/sprite/wall_destroyed_right_twice.png';
import wall_destroyed_right_up from './images/sprite/wall_destroyed_right_up.png';
import wall_destroyed_up from './images/sprite/wall_destroyed_up.png';
import wall_destroyed_up_down from './images/sprite/wall_destroyed_up_down.png';
import wall_destroyed_up_left from './images/sprite/wall_destroyed_up_left.png';
import wall_destroyed_up_twice from './images/sprite/wall_destroyed_up_twice.png';


//own

export default [
	{
		image: none,
		title: `NONE(' ')`,
		description: ``,
	},
	{
		image: battle_wall,
		title: `BATTLE_WALL('☼')`,
		description: ``,
	},
	{
		image: bang,
		title: `BANG('Ѡ')`,
		description: ``,
	},
	{
		image: ice,
		title: `ICE('#')`,
		description: ``,
	},
	{
		image: tree,
		title: `TREE('%')`,
		description: ``,
	},
	{
		image: river,
		title: `RIVER('~')`,
		description: ``,
	},
	{
		image: wall,
		title: `WALL('╬')`,
		description: ``,
	},
	{
		image: wall_destroyed_down,
		title: `WALL_DESTROYED_DOWN('╩')`,
		description: ``,
	},
	{
		image: wall_destroyed_up,
		title: `WALL_DESTROYED_UP('╦')`,
		description: ``,
	},
	{
		image: wall_destroyed_left,
		title: `WALL_DESTROYED_LEFT('╠')`,
		description: ``,
	},
	{
		image: wall_destroyed_right,
		title: `WALL_DESTROYED_RIGHT('╣')`,
		description: ``,
	},
	{
		image: wall_destroyed_down_twice,
		title: `WALL_DESTROYED_DOWN_TWICE('╨')`,
		description: ``,
	},
	{
		image: wall_destroyed_up_twice,
		title: `WALL_DESTROYED_UP_TWICE('╥')`,
		description: ``,
	},
	{
		image: wall_destroyed_left_twice,
		title: `WALL_DESTROYED_LEFT_TWICE('╞')`,
		description: ``,
	},
	{
		image: wall_destroyed_right_twice,
		title: `WALL_DESTROYED_RIGHT_TWICE('╡')`,
		description: ``,
	},
	{
		image: wall_destroyed_left_right,
		title: `WALL_DESTROYED_LEFT_RIGHT('│')`,
		description: ``,
	},
	{
		image: wall_destroyed_up_down,
		title: `WALL_DESTROYED_UP_DOWN('─')`,
		description: ``,
	},
	{
		image: wall_destroyed_up_left,
		title: `WALL_DESTROYED_UP_LEFT('┌')`,
		description: ``,
	},
	{
		image: wall_destroyed_right_up,
		title: `WALL_DESTROYED_RIGHT_UP('┐')`,
		description: ``,
	},
	{
		image: wall_destroyed_down_left,
		title: `WALL_DESTROYED_DOWN_LEFT('└')`,
		description: ``,
	},
	{
		image: wall_destroyed_down_right,
		title: `WALL_DESTROYED_DOWN_RIGHT('┘')`,
		description: ``,
	},
	{
		image: wall_destroyed,
		title: `WALL_DESTROYED(' ')`,
		description: ``,
	},
	{
		image: bullet,
		title: `BULLET('•')`,
		description: ``,
	},
	{
		image: tank_up,
		title: `TANK_UP('▲')`,
		description: ``,
	},
	{
		image: tank_right,
		title: `TANK_RIGHT('►')`,
		description: ``,
	},
	{
		image: tank_down,
		title: `TANK_DOWN('▼')`,
		description: ``,
	},
	{
		image: tank_left,
		title: `TANK_LEFT('◄')`,
		description: ``,
	},
	{
		image: other_tank_up,
		title: `OTHER_TANK_UP('˄')`,
		description: ``,
	},
	{
		image: other_tank_right,
		title: `OTHER_TANK_RIGHT('˃')`,
		description: ``,
	},
	{
		image: other_tank_down,
		title: `OTHER_TANK_DOWN('˅')`,
		description: ``,
	},
	{
		image: other_tank_left,
		title: `OTHER_TANK_LEFT('˂')`,
		description: ``,
	},
	{
		image: ai_tank_up,
		title: `AI_TANK_UP('?')`,
		description: ``,
	},
	{
		image: ai_tank_right,
		title: `AI_TANK_RIGHT('»')`,
		description: ``,
	},
	{
		image: ai_tank_down,
		title: `AI_TANK_DOWN('¿')`,
		description: ``,
	},
	{
		image: ai_tank_left,
		title: `AI_TANK_LEFT('«')`,
		description: ``,
	},
	{
		image: ai_tank_prize,
		title: `AI_TANK_PRIZE('◘')`,
		description: ``,
	},
	{
		image: prize,
		title: `PRIZE('!')`,
		description: ``,
	},
	{
		image: prize_immortality,
		title: `PRIZE_IMMORTALITY('1')`,
		description: ``,
	},
	{
		image: prize_breaking_walls,
		title: `PRIZE_BREAKING_WALLS('2')`,
		description: ``,
	},
	{
		image: prize_walking_on_water,
		title: `PRIZE_WALKING_ON_WATER('3')`,
		description: ``,
	},
];
