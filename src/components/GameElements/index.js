// vendor
import React, { Component } from 'react';

// proj
import None from '../../styles/images/game/none.png';
import Wall from '../../styles/images/game/wall.png';
import StartFloor from '../../styles/images/game/start_floor.png';
import Apple from '../../styles/images/game/apple.png';
import Stone from '../../styles/images/game/stone.png';
import FlyingPill from '../../styles/images/game/flying_pill.png';
import FuryPill from '../../styles/images/game/fury_pill.png';
import Gold from '../../styles/images/game/gold.png';
import HeadDown from '../../styles/images/game/head_down.png';
import HeadLeft from '../../styles/images/game/head_left.png';
import HeadRight from '../../styles/images/game/head_right.png';
import HeadUp from '../../styles/images/game/head_up.png';
import HeadEvil from '../../styles/images/game/head_evil.png';
import HeadFly from '../../styles/images/game/head_fly.png';
import HeadSleep from '../../styles/images/game/head_sleep.png';
import HeadDead from '../../styles/images/game/head_dead.png';
import EnemyHeadDown from '../../styles/images/game/enemy_head_down.png';
import EnemyHeadLeft from '../../styles/images/game/enemy_head_left.png';
import EnemyHeadRight from '../../styles/images/game/enemy_head_right.png';
import EnemyHeadUp from '../../styles/images/game/enemy_head_up.png';
import EnemyHeadEvil from '../../styles/images/game/enemy_head_evil.png';
import EnemyHeadFly from '../../styles/images/game/enemy_head_fly.png';
import EnemyHeadSleep from '../../styles/images/game/enemy_head_sleep.png';
import EnemyHeadDead from '../../styles/images/game/enemy_head_dead.png';

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
        title:       'FLYING_PILL(\'%\')',
        description: 'Ангельські крила (Таблетка польоту)',
    },
    {
        image:       FuryPill,
        title:       'FURY_PILL(\'@\')',
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
        title:       'HEAD_EVIL(\'⊕\')',
        description: 'Голова Змійки Учасника із надздібностю "лють"',
    },
    {
        image:       HeadFly,
        title:       'HEAD_FLY(\'⊖\')',
        description: 'Голова Змійки Учасника із надздібностю "політ"',
    },
    {
        image:       HeadSleep,
        title:       'HEAD_SLEEP(\'⬢\')',
        description: 'Голова неактивної Змійки Учасника',
    },
    {
        image:       HeadDead,
        title:       'HEAD_DEAD(\'☻\')',
        description: 'Голова мертвої Змійки Учасника',
    },
    {
        image:       EnemyHeadDown,
        title:       'ENEMY_HEAD_DOWN(\'∨\')',
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
        title:       'ENEMY_HEAD_UP(\'∧\')',
        description: 'Голова Змійки суперника у напрямку вгору',
    },
    {
        image:       EnemyHeadEvil,
        title:       'ENEMY_HEAD_EVIL(\'⊗\')',
        description: 'Голова Змійки суперника із надздібностю "лють"',
    },
    {
        image:       EnemyHeadFly,
        title:       'ENEMY_HEAD_FLY(\'⊘\')',
        description: 'Голова Змійки суперника із надздібностю "політ"',
    },
    {
        image:       EnemyHeadSleep,
        title:       'ENEMY_HEAD_SLEEP(\'⬡\')',
        description: 'Голова неактивної Змійки суперника',
    },
    {
        image:       EnemyHeadDead,
        title:       'ENEMY_HEAD_DEAD(\'☺\')',
        description: 'Голова мертвої Змійки суперника',
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
