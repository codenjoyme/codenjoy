// vendor
import React, { Component } from 'react';

// proj
import Apple from '../../styles/images/game/apple.png';
import BodyHorizontal from '../../styles/images/game/body_horizontal.png';
import BodyLeftDown from '../../styles/images/game/body_left_down.png';
import BodyLeftUp from '../../styles/images/game/body_left_up.png';
import BodyRightDown from '../../styles/images/game/body_right_down.png';
import BodyRightUp from '../../styles/images/game/body_right_up.png';
import BodyVertical from '../../styles/images/game/body_vertical.png';
import EnemyBodyHorizontal from '../../styles/images/game/enemy_body_horizontal.png';
import EnemyBodyLeftDown from '../../styles/images/game/enemy_body_left_down.png';
import EnemyBodyLeftUp from '../../styles/images/game/enemy_body_left_up.png';
import EnemyBodyRightDown from '../../styles/images/game/enemy_body_right_down.png';
import EnemyBodyRightUp from '../../styles/images/game/enemy_body_right_up.png';
import EnemyBodyVertical from '../../styles/images/game/enemy_body_vertical.png';
import EnemyHeadDead from '../../styles/images/game/enemy_head_dead.png';
import EnemyHeadDown from '../../styles/images/game/enemy_head_down.png';
import EnemyHeadEvil from '../../styles/images/game/enemy_head_evil.png';
import EnemyHeadFly from '../../styles/images/game/enemy_head_fly.png';
import EnemyHeadLeft from '../../styles/images/game/enemy_head_left.png';
import EnemyHeadRight from '../../styles/images/game/enemy_head_right.png';
import EnemyHeadSleep from '../../styles/images/game/enemy_head_sleep.png';
import EnemyHeadUp from '../../styles/images/game/enemy_head_up.png';
import EnemyTailEndDown from '../../styles/images/game/enemy_tail_end_down.png';
import EnemyTailEndLeft from '../../styles/images/game/enemy_tail_end_left.png';
import EnemyTailEndRight from '../../styles/images/game/enemy_tail_end_right.png';
import EnemyTailEndUp from '../../styles/images/game/enemy_tail_end_up.png';
import EnemyTailInactive from '../../styles/images/game/enemy_tail_inactive.png';
import FlyingPill from '../../styles/images/game/flying_pill.png';
import FuryPill from '../../styles/images/game/fury_pill.png';
import Gold from '../../styles/images/game/gold.png';
import HeadDead from '../../styles/images/game/head_dead.png';
import HeadDown from '../../styles/images/game/head_down.png';
import HeadEvil from '../../styles/images/game/head_evil.png';
import HeadFly from '../../styles/images/game/head_fly.png';
import HeadLeft from '../../styles/images/game/head_left.png';
import HeadRight from '../../styles/images/game/head_right.png';
import HeadSleep from '../../styles/images/game/head_sleep.png';
import HeadUp from '../../styles/images/game/head_up.png';
import None from '../../styles/images/game/none.png';
import StartFloor from '../../styles/images/game/start_floor.png';
import Stone from '../../styles/images/game/stone.png';
import TailEndDown from '../../styles/images/game/tail_end_down.png';
import TailEndLeft from '../../styles/images/game/tail_end_left.png';
import TailEndRight from '../../styles/images/game/tail_end_right.png';
import TailEndUp from '../../styles/images/game/tail_end_up.png';
import TailInactive from '../../styles/images/game/tail_inactive.png';
import Wall from '../../styles/images/game/wall.png';

//own
import Styles from './styles.module.css';

const ELEMENTS = [
    {
        image:       None,
        title:       'NONE(\' \')',
        description: 'Пусте місце – по якому може рухатись Учасник',
    },
    {
        image:       Wall,
        title:       'WALL(\'☼\')',
        description: 'Стіна, через яку не можна пройти',
    },
    {
        image:       StartFloor,
        title:       'START_FLOOR(\'#\')',
        description: 'Стартова точка. Звідси починає рух Змійка',
    },
    { image: Apple, title: 'APPLE(\'○\')', description: 'Яблуко' },
    { image: Stone, title: 'STONE(\'●\')', description: 'Гиря (Камінь)' },
    {
        image:       FlyingPill,
        title:       'FLYING_PILL(\'©\')',
        description: 'Ангельські крила (Таблетка польоту)',
    },
    {
        image:       FuryPill,
        title:       'FURY_PILL(\'®\')',
        description: 'Маска диявола (Таблетка люті)',
    },
    { image: Gold, title: 'GOLD(\'$\')', description: 'Золото' },
    {
        image:       HeadDown,
        title:       'HEAD_DOWN(\'▼\')',
        description: 'Голова Змійки Учасника у напрямку вниз',
    },
    {
        image:       HeadLeft,
        title:       'HEAD_LEFT(\'◄\')',
        description: 'Голова Змійки Учасника у напрямку вліво',
    },
    {
        image:       HeadRight,
        title:       'HEAD_RIGHT(\'►\')',
        description: 'Голова Змійки Учасника у напрямку вправо',
    },
    {
        image:       HeadUp,
        title:       'HEAD_UP(\'▲\')',
        description: 'Голова Змійки Учасника у напрямку вгору',
    },
    {
        image:       HeadEvil,
        title:       'HEAD_EVIL(\'♥\')',
        description: 'Голова Змійки Учасника із надздібностю "лють"',
    },
    {
        image:       HeadFly,
        title:       'HEAD_FLY(\'♠\')',
        description: 'Голова Змійки Учасника із надздібностю "політ"',
    },
    {
        image:       HeadSleep,
        title:       'HEAD_SLEEP(\'&\')',
        description: 'Голова неактивної Змійки Учасника',
    },
    {
        image:       HeadDead,
        title:       'HEAD_DEAD(\'☻\')',
        description: 'Голова мертвої Змійки Учасника',
    },

    {
        title:       'TAIL_END_DOWN(\'╙\')',
        description: 'Хвіст Змійки Учасника у напрямку вниз',
        image:       TailEndDown,
    },
    {
        title:       'TAIL_END_LEFT(\'╘\')',
        description: 'Хвіст Змійки Учасника у напрямку вліво',
        image:       TailEndLeft,
    },
    {
        title:       'TAIL_END_UP(\'╓\')',
        description: 'Хвіст Змійки Учасника у напрямку вгору',
        image:       TailEndUp,
    },
    {
        title:       'TAIL_END_RIGHT(\'╕\')',
        description: 'Хвіст Змійки Учасника у напрямку вправо',
        image:       TailEndRight,
    },
    {
        title:       'TAIL_INACTIVE(\'~\')',
        description: 'Хвіст неактивної Змійки Учасника',
        image:       TailInactive,
    },

    {
        title:       'BODY_HORIZONTAL(\'═\')',
        description: 'Тулуб Змійки Учасника у горизонтальному положені',
        image:       BodyHorizontal,
    },
    {
        title:       'BODY_VERTICAL(\'║\')',
        description: 'Тулуб Змійки Учасника у вертикальному положені',
        image:       BodyVertical,
    },
    {
        title:       'BODY_LEFT_DOWN(\'╗\')',
        description:
            'Тулуб Змійки Учасника при повороті вліво під час підняття',
        image: BodyLeftDown,
    },
    {
        title:       'BODY_LEFT_UP(\'╝\')',
        description:
            'Тулуб Змійки Учасника при повороті вліво під час спускання',
        image: BodyLeftUp,
    },
    {
        title:       'BODY_RIGHT_DOWN(\'╔\')',
        description:
            'Тулуб Змійки Учасника при повороті вправо під час підняття',
        image: BodyRightDown,
    },
    {
        title:       'BODY_RIGHT_UP(\'╚\')',
        description:
            'Тулуб Змійки Учасника при повороті вправо під час спускання',
        image: BodyRightUp,
    },

    {
        image:       EnemyHeadDown,
        title:       'ENEMY_HEAD_DOWN(\'˅\')',
        description: 'Голова Змійки Учасника у напрямку вниз',
    },
    {
        image:       EnemyHeadLeft,
        title:       'ENEMY_HEAD_LEFT(\'<\')',
        description: 'Голова Змійки суперника у напрямку вліво',
    },
    {
        image:       EnemyHeadRight,
        title:       'ENEMY_HEAD_RIGHT(\'>\')',
        description: 'Голова Змійки суперника у напрямку вправо',
    },
    {
        image:       EnemyHeadUp,
        title:       'ENEMY_HEAD_UP(\'˄\')',
        description: 'Голова Змійки суперника у напрямку вгору',
    },
    {
        image:       EnemyHeadEvil,
        title:       'ENEMY_HEAD_EVIL(\'♣\')',
        description: 'Голова Змійки суперника із надздібностю "лють"',
    },
    {
        image:       EnemyHeadFly,
        title:       'ENEMY_HEAD_FLY(\'♦\')',
        description: 'Голова Змійки суперника із надздібностю "політ"',
    },
    {
        image:       EnemyHeadSleep,
        title:       'ENEMY_HEAD_SLEEP(\'ø\')',
        description: 'Голова неактивної Змійки суперника',
    },
    {
        image:       EnemyHeadDead,
        title:       'ENEMY_HEAD_DEAD(\'☺\')',
        description: 'Голова мертвої Змійки суперника',
    },

    {
        title:       'ENEMY_TAIL_END_DOWN(\'¤\')',
        description: 'Хвіст Змійки суперника у напрямку вниз',
        image:       EnemyTailEndDown,
    },
    {
        title:       'ENEMY_TAIL_END_LEFT(\'×\')',
        description: 'Хвіст Змійки суперника у напрямку вліво',
        image:       EnemyTailEndLeft,
    },
    {
        title:       'ENEMY_TAIL_END_UP(\'æ\')',
        description: 'Хвіст Змійки суперника у напрямку вгору',
        image:       EnemyTailEndUp,
    },
    {
        title:       'ENEMY_TAIL_END_RIGHT(\'ö\')',
        description: 'Хвіст Змійки суперника у напрямку вправо',
        image:       EnemyTailEndRight,
    },
    {
        title:       'ENEMY_TAIL_INACTIVE(\'*\' )',
        description: 'Хвіст неактивної Змійки суперника',
        image:       EnemyTailInactive,
    },

    {
        title:       'ENEMY_BODY_HORIZONTAL(\'─\')',
        description: 'Тулуб Змійки Суперника у горизонтальному положені',
        image:       EnemyBodyHorizontal,
    },
    {
        title:       'ENEMY_BODY_VERTICAL(\'│\')',
        description: 'Тулуб Змійки Суперника у вертикальному положені',
        image:       EnemyBodyVertical,
    },
    {
        title:       'ENEMY_BODY_LEFT_DOWN(\'┐\')',
        description:
            'Тулуб Змійки Суперника при повороті вліво під час підняття',
        image: EnemyBodyLeftDown,
    },
    {
        title:       'ENEMY_BODY_LEFT_UP(\'┘\')',
        description:
            'Тулуб Змійки Суперника при повороті вліво під час спускання',
        image: EnemyBodyLeftUp,
    },
    {
        title:       'ENEMY_BODY_RIGHT_DOWN(\'┌\')',
        description:
            'Тулуб Змійки Суперника при повороті вправо під час підняття',
        image: EnemyBodyRightDown,
    },
    {
        title:       'ENEMY_BODY_RIGHT_UP(\'└\')',
        description:
            'Тулуб Змійки Суперника при повороті вправо під час спускання',
        image: EnemyBodyRightUp,
    },
];

export class GameElements extends Component {
    render() {
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
                            <div className={ Styles.elementDescription }>
                                { description }
                            </div>
                        </div>
                    </div>
                )) }
            </div>
        );
    }
}
