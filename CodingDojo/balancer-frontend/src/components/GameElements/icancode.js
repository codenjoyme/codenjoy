// vendor
import React, { Component } from 'react';

import angle_back_left from '../../styles/images/game/icancode/angle_back_left.png';
import angle_back_right from '../../styles/images/game/icancode/angle_back_right.png';
import angle_in_left from '../../styles/images/game/icancode/angle_in_left.png';
import angle_in_right from '../../styles/images/game/icancode/angle_in_right.png';
import angle_out_left from '../../styles/images/game/icancode/angle_out_left.png';
import angle_out_right from '../../styles/images/game/icancode/angle_out_right.png';
//import background from '../../styles/images/game/icancode/background.png';
import box from '../../styles/images/game/icancode/box.png';
//import door_left from '../../styles/images/game/icancode/door_left.png';
//import door_right from '../../styles/images/game/icancode/door_right.png';
import empty from '../../styles/images/game/icancode/empty.png';
import exit from '../../styles/images/game/icancode/exit.png';
import female_zombie from '../../styles/images/game/icancode/female_zombie.png';
import floor from '../../styles/images/game/icancode/floor.png';
//import fog from '../../styles/images/game/icancode/fog.png';
import gold from '../../styles/images/game/icancode/gold.png';
import hole from '../../styles/images/game/icancode/hole.png';
import laser_down from '../../styles/images/game/icancode/laser_down.png';
import laser_left from '../../styles/images/game/icancode/laser_left.png';
//import laser_machine_charging2_down from '../../styles/images/game/icancode/laser_machine_charging2_down.png';
//import laser_machine_charging2_left from '../../styles/images/game/icancode/laser_machine_charging2_left.png';
//import laser_machine_charging2_right from '../../styles/images/game/icancode/laser_machine_charging2_right.png';
//import laser_machine_charging2_up from '../../styles/images/game/icancode/laser_machine_charging2_up.png';
//import laser_machine_charging3_down from '../../styles/images/game/icancode/laser_machine_charging3_down.png';
//import laser_machine_charging3_left from '../../styles/images/game/icancode/laser_machine_charging3_left.png';
//import laser_machine_charging3_right from '../../styles/images/game/icancode/laser_machine_charging3_right.png';
//import laser_machine_charging3_up from '../../styles/images/game/icancode/laser_machine_charging3_up.png';
import laser_machine_charging_down from '../../styles/images/game/icancode/laser_machine_charging_down.png';
import laser_machine_charging_left from '../../styles/images/game/icancode/laser_machine_charging_left.png';
import laser_machine_charging_right from '../../styles/images/game/icancode/laser_machine_charging_right.png';
import laser_machine_charging_up from '../../styles/images/game/icancode/laser_machine_charging_up.png';
import laser_machine_ready_down from '../../styles/images/game/icancode/laser_machine_ready_down.png';
import laser_machine_ready_left from '../../styles/images/game/icancode/laser_machine_ready_left.png';
import laser_machine_ready_right from '../../styles/images/game/icancode/laser_machine_ready_right.png';
import laser_machine_ready_up from '../../styles/images/game/icancode/laser_machine_ready_up.png';
import laser_right from '../../styles/images/game/icancode/laser_right.png';
import laser_up from '../../styles/images/game/icancode/laser_up.png';
import male_zombie from '../../styles/images/game/icancode/male_zombie.png';
import robo from '../../styles/images/game/icancode/robo.png';
import robo_falling from '../../styles/images/game/icancode/robo_falling.png';
import robo_flying from '../../styles/images/game/icancode/robo_flying.png';
import robo_laser from '../../styles/images/game/icancode/robo_laser.png';
import robo_other from '../../styles/images/game/icancode/robo_other.png';
import robo_other_falling from '../../styles/images/game/icancode/robo_other_falling.png';
import robo_other_flying from '../../styles/images/game/icancode/robo_other_flying.png';
import robo_other_laser from '../../styles/images/game/icancode/robo_other_laser.png';
import space from '../../styles/images/game/icancode/space.png';
import start from '../../styles/images/game/icancode/start.png';
import wall_back from '../../styles/images/game/icancode/wall_back.png';
import wall_back_angle_left from '../../styles/images/game/icancode/wall_back_angle_left.png';
import wall_back_angle_right from '../../styles/images/game/icancode/wall_back_angle_right.png';
import wall_front from '../../styles/images/game/icancode/wall_front.png';
import wall_left from '../../styles/images/game/icancode/wall_left.png';
import wall_right from '../../styles/images/game/icancode/wall_right.png';
import zombie_die from '../../styles/images/game/icancode/zombie_die.png';
import zombie_start from '../../styles/images/game/icancode/zombie_start.png';
import death_ray_perk from '../../styles/images/game/icancode/death_ray_perk.png';
import unstoppable_laser_perk from '../../styles/images/game/icancode/unstoppable_laser_perk.png';
import unlimited_fire_perk from '../../styles/images/game/icancode/unlimited_fire_perk.png';


//own
import Styles from './styles.module.css';

const ELEMENTS = [
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

export class GameElements extends Component {
    render() {
        const {  settings  } = this.props;

        return (
            <div className={ Styles.gameElements }>
                { ELEMENTS.map(({ image, title, description }) => (
                    <div key={ title } className={ Styles.elementContainer }>
                        <img
                            className={ Styles.elementImage }
                            src={ image }
                            alt={ title }
                        />
                        <div className={ Styles.elementDescriptionContainer }>
                            <div className={ Styles.elementTitle }>{ title }</div>
                            <div className={ Styles.elementDescription }
                                    dangerouslySetInnerHTML={{__html: description.replace('*', '<a href="#settings">*</a>') }}/>
                        </div>
                    </div>
                )) }
            </div>
        );
    }
}
