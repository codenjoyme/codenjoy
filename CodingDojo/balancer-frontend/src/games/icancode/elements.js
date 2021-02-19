// vendor
import React, { Component } from 'react';

import angle_back_left from './images/sprite/angle_back_left.png';
import angle_back_right from './images/sprite/angle_back_right.png';
import angle_in_left from './images/sprite/angle_in_left.png';
import angle_in_right from './images/sprite/angle_in_right.png';
import angle_out_left from './images/sprite/angle_out_left.png';
import angle_out_right from './images/sprite/angle_out_right.png';
//import background from './images/sprite/background.png';
import box from './images/sprite/box.png';
//import door_left from './images/sprite/door_left.png';
//import door_right from './images/sprite/door_right.png';
import empty from './images/sprite/empty.png';
import exit from './images/sprite/exit.png';
import female_zombie from './images/sprite/female_zombie.png';
import floor from './images/sprite/floor.png';
//import fog from './images/sprite/fog.png';
import gold from './images/sprite/gold.png';
import hole from './images/sprite/hole.png';
import laser_down from './images/sprite/laser_down.png';
import laser_left from './images/sprite/laser_left.png';
//import laser_machine_charging2_down from './images/sprite/laser_machine_charging2_down.png';
//import laser_machine_charging2_left from './images/sprite/laser_machine_charging2_left.png';
//import laser_machine_charging2_right from './images/sprite/laser_machine_charging2_right.png';
//import laser_machine_charging2_up from './images/sprite/laser_machine_charging2_up.png';
//import laser_machine_charging3_down from './images/sprite/laser_machine_charging3_down.png';
//import laser_machine_charging3_left from './images/sprite/laser_machine_charging3_left.png';
//import laser_machine_charging3_right from './images/sprite/laser_machine_charging3_right.png';
//import laser_machine_charging3_up from './images/sprite/laser_machine_charging3_up.png';
import laser_machine_charging_down from './images/sprite/laser_machine_charging_down.png';
import laser_machine_charging_left from './images/sprite/laser_machine_charging_left.png';
import laser_machine_charging_right from './images/sprite/laser_machine_charging_right.png';
import laser_machine_charging_up from './images/sprite/laser_machine_charging_up.png';
import laser_machine_ready_down from './images/sprite/laser_machine_ready_down.png';
import laser_machine_ready_left from './images/sprite/laser_machine_ready_left.png';
import laser_machine_ready_right from './images/sprite/laser_machine_ready_right.png';
import laser_machine_ready_up from './images/sprite/laser_machine_ready_up.png';
import laser_right from './images/sprite/laser_right.png';
import laser_up from './images/sprite/laser_up.png';
import male_zombie from './images/sprite/male_zombie.png';
import robo from './images/sprite/robo.png';
import robo_falling from './images/sprite/robo_falling.png';
import robo_flying from './images/sprite/robo_flying.png';
import robo_laser from './images/sprite/robo_laser.png';
import robo_other from './images/sprite/robo_other.png';
import robo_other_falling from './images/sprite/robo_other_falling.png';
import robo_other_flying from './images/sprite/robo_other_flying.png';
import robo_other_laser from './images/sprite/robo_other_laser.png';
import space from './images/sprite/space.png';
import start from './images/sprite/start.png';
import wall_back from './images/sprite/wall_back.png';
import wall_back_angle_left from './images/sprite/wall_back_angle_left.png';
import wall_back_angle_right from './images/sprite/wall_back_angle_right.png';
import wall_front from './images/sprite/wall_front.png';
import wall_left from './images/sprite/wall_left.png';
import wall_right from './images/sprite/wall_right.png';
import zombie_die from './images/sprite/zombie_die.png';
import zombie_start from './images/sprite/zombie_start.png';
import death_ray_perk from './images/sprite/death_ray_perk.png';
import unstoppable_laser_perk from './images/sprite/unstoppable_laser_perk.png';
import unlimited_fire_perk from './images/sprite/unlimited_fire_perk.png';
import jump_perk from './images/sprite/jump_perk.png';
import move_boxes_perk from './images/sprite/move_boxes_perk.png';
import fire_perk from './images/sprite/fire_perk.png';

//own

export default [
	{
		image:  start,
		title: `START('S')`,
		description: `Стартова точка`,
	},
	{
		image:  exit,
		title: `EXIT('E')`,
		description: `Вихід. Саме до цієї точки герой має донести золото`,
	},
	{
		image:  floor,
		title: `FLOOR('.')`,
		description: `Підлога. На неї можна ставати.`,
	},
	{
		image:  empty,
		title: `EMPTY('-')`,
		description: `Пуста клітинка. На неї можна ставати`,
	},
	{
		image:  zombie_start,
		title: `ZOMBIE_START('Z')`,
		description: `Респаун зомбі. На неї можна ставати`,
	},
	{
		image:  hole,
		title: `HOLE('O')`,
		description: `Провалля. Сюди краще не ставати`,
	},
	{
		image:  box,
		title: `BOX('B')`,
		description: `Коробка. Її можно переміщати або перестрибнути`,
	},
	{
		image:  gold,
		title: `GOLD('$')`,
		description: `Мішечок золота. Дуже корисна річ. Хапай його та донеси до виходу.`,
	},
    {
		image:  unstoppable_laser_perk,
		title: `UNSTOPPABLE_LASER_PERK('l')`,
		description: `Перк. Неспинний лазер`,
	},
	{
    	image:  death_ray_perk,
    	title: `DEATH_RAY_PERK('r')`,
    	description: `Перк. Промінь смерті`,
    },
    {
        image:  unlimited_fire_perk,
        title: `UNLIMITED_FIRE_PERK('f')`,
        description: `Перк. Бесперервна стрільба`,
    },
    {
        image:  jump_perk,
        title: `JUMP_PERK('f')`,
        description: `Перк. Можливість стрибати`,
    },
    {
        image:  move_boxes_perk,
        title: `MOVE_BOXES_PERK('f')`,
        description: `Перк. Можливість штовхати коробки`,
    },
    {
        image:  fire_perk,
        title: `FIRE_PERK('f')`,
        description: `Перк. Можливість стріляти`,
    },
    {
		image:  robo,
		title: `ROBO('☺')`,
		description: `Герой`,
	},
	{
		image:  robo_falling,
		title: `ROBO_FALLING('o')`,
		description: `Герой падає`,
	},
	{
		image:  robo_flying,
		title: `ROBO_FLYING('*')`,
		description: `Герой стрибає`,
	},
	{
		image:  robo_laser,
		title: `ROBO_LASER('☻')`,
		description: `Герой помер`,
	},

	{
		image:  female_zombie,
		title: `FEMALE_ZOMBIE('♀')`,
		description: `Зомбі-дівчинка`,
	},
	{
		image:  male_zombie,
		title: `MALE_ZOMBIE('♂')`,
		description: `Зомбі-хлопчик`,
	},
	{
		image:  zombie_die,
		title: `ZOMBIE_DIE('✝')`,
		description: `Мертвий зомбі`,
	},
	{
    	image:  robo_other,
    	title: `ROBO_OTHER('X')`,
    	description: `Ворожий герой`,
    },
    {
    	image:  robo_other_falling,
    	title: `ROBO_OTHER_FALLING('x')`,
    	description: `Ворожий герой падає`,
    },
    {
    	image:  robo_other_flying,
    	title: `ROBO_OTHER_FLYING('^')`,
    	description: `Ворожий герой стрибає`,
    },
    {
    	image:  robo_other_laser,
    	title: `ROBO_OTHER_LASER('&')`,
    	description: `Ворожий герой помер`,
    },

	{
		image:  laser_machine_charging_left,
		title: `LASER_MACHINE_CHARGING_LEFT('˂')`,
		description: `Лазерна машина заряджається`,
	},
	{
		image:  laser_machine_charging_right,
		title: `LASER_MACHINE_CHARGING_RIGHT('˃')`,
		description: `Лазерна машина заряджається`,
	},
	{
		image:  laser_machine_charging_up,
		title: `LASER_MACHINE_CHARGING_UP('˄')`,
		description: `Лазерна машина заряджається`,
	},
	{
		image:  laser_machine_charging_down,
		title: `LASER_MACHINE_CHARGING_DOWN('˅')`,
		description: `Лазерна машина заряджається`,
	},
	{
		image:  laser_machine_ready_left,
		title: `LASER_MACHINE_READY_LEFT('◄')`,
		description: ` Лазерна машина готова стріляти`,
	},
	{
		image:  laser_machine_ready_right,
		title: `LASER_MACHINE_READY_RIGHT('►')`,
		description: ` Лазерна машина готова стріляти`,
	},
	{
		image:  laser_machine_ready_up,
		title: `LASER_MACHINE_READY_UP('▲')`,
		description: ` Лазерна машина готова стріляти`,
	},
	{
		image:  laser_machine_ready_down,
		title: `LASER_MACHINE_READY_DOWN('▼')`,
		description: ` Лазерна машина готова стріляти`,
	},
	{
		image:  laser_left,
		title: `LASER_LEFT('←')`,
		description: `Лазер, що рухається вліво`,
	},
	{
		image:  laser_right,
		title: `LASER_RIGHT('→')`,
		description: `Лазер, що рухається вправо`,
	},
	{
		image:  laser_up,
		title: `LASER_UP('↑')`,
		description: ` Лазер, що рухається вгору`,
	},
	{
		image:  laser_down,
		title: `LASER_DOWN('↓')`,
		description: ` Лазер, що рухається вниз`,
	},
	{
		image:  angle_in_left,
		title: `ANGLE_IN_LEFT('╔')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  wall_front,
		title: `WALL_FRONT('═')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  angle_in_right,
		title: `ANGLE_IN_RIGHT('┐')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  wall_right,
		title: `WALL_RIGHT('│')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  angle_back_right,
		title: `ANGLE_BACK_RIGHT('┘')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  wall_back,
		title: `WALL_BACK('─')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  angle_back_left,
		title: `ANGLE_BACK_LEFT('└')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  wall_left,
		title: `WALL_LEFT('║')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  wall_back_angle_left,
		title: `WALL_BACK_ANGLE_LEFT('┌')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  wall_back_angle_right,
		title: `WALL_BACK_ANGLE_RIGHT('╗')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  angle_out_right,
		title: `ANGLE_OUT_RIGHT('╝')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  angle_out_left,
		title: `ANGLE_OUT_LEFT('╚')`,
		description: ` Стінка. Її треба обходити`,
	},
	{
		image:  space,
		title: `SPACE(' ')`,
		description: `Елемент стіни`,
	},
];
