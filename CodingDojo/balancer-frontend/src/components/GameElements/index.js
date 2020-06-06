// vendor
import React, { Component } from 'react';

// proj
// import bomb from '../../styles/images/game/sprite/bomb.png';
import bomb_blast_radius_increase from '../../styles/images/game/sprite/bomb_blast_radius_increase.png';
import bomb_bomberman from '../../styles/images/game/sprite/bomb_bomberman.png';
import bomb_count_increase from '../../styles/images/game/sprite/bomb_count_increase.png';
import bomb_immune from '../../styles/images/game/sprite/bomb_immune.png';
import bomb_remote_control from '../../styles/images/game/sprite/bomb_remote_control.png';
// import bomb_timer_0 from '../../styles/images/game/sprite/bomb_timer_0.png';
import bomb_timer_1 from '../../styles/images/game/sprite/bomb_timer_1.png';
import bomb_timer_2 from '../../styles/images/game/sprite/bomb_timer_2.png';
import bomb_timer_3 from '../../styles/images/game/sprite/bomb_timer_3.png';
import bomb_timer_4 from '../../styles/images/game/sprite/bomb_timer_4.png';
import bomb_timer_5 from '../../styles/images/game/sprite/bomb_timer_5.png';
import bomberman from '../../styles/images/game/sprite/bomberman.png';
import boom from '../../styles/images/game/sprite/boom.png';
import dead_bomberman from '../../styles/images/game/sprite/dead_bomberman.png';
import dead_meat_chopper from '../../styles/images/game/sprite/dead_meat_chopper.png';
import destroyable_wall from '../../styles/images/game/sprite/destroyable_wall.png';
import destroyed_wall from '../../styles/images/game/sprite/destroyed_wall.png';
import meat_chopper from '../../styles/images/game/sprite/meat_chopper.png';
import none from '../../styles/images/game/sprite/none.png';
import other_bomb_bomberman from '../../styles/images/game/sprite/other_bomb_bomberman.png';
import other_bomberman from '../../styles/images/game/sprite/other_bomberman.png';
import other_dead_bomberman from '../../styles/images/game/sprite/other_dead_bomberman.png';
import wall from '../../styles/images/game/sprite/wall.png';

//own
import Styles from './styles.module.css';

const ELEMENTS = [
    {
        image:       bomberman,
        title:       `BOMBERMAN ('☺')`,
        description: `Ваш Бомбермен.`,
    },
    {
        image:       bomb_bomberman,
        title:       `BOMB_BOMBERMAN ('☻')`,
        description: `Ваш Бомбермен, якщо він сидить на бомбі.`,
    },
    {
        image:       dead_bomberman,
        title:       `DEAD_BOMBERMAN ('Ѡ')`,
        description: `Йойкс! Ваш Бомбермен помер. Нехвилюйтеся, він з'явиться
десь на полі і з новим Раудндом ви зможете їм керувати, але цілком ймовірно за
це ви отримаєте штрафні бали.`,
    },
    {
        image:       other_bomberman,
        title:       `OTHER_BOMBERMAN ('♥')`,
        description: `Бомбермен суперника.`,
    },
    {
        image:       other_bomb_bomberman,
        title:       `OTHER_BOMB_BOMBERMAN ('♠')`,
        description: `Бомбермен суперника, який сидить на бомбі.`
    },
    {
        image:       other_dead_bomberman,
        title:       `OTHER_DEAD_BOMBERMAN ('♣')`,
        description: `Так виглядає мертвий Бомбермен суперника.
Якщо це ви його підірвали - ви отримаєте бонусні бали.`
    },
    {
        image:       bomb_timer_5,
        title:       `BOMB_TIMER_5 ('5')`,
        description: `Після того як Бомбермен поставить бомбу таймер
почне працювати (всього 5 тіків/секунд). Скоріше за все ви не побачите
не цей символ, а BOMB_BOMBERMAN('☻'), але пам'ятайте - 5 секунд і вибух.
Тре тікати швидко.`
    },
    {
        image:       bomb_timer_4,
        title:       `BOMB_TIMER_4 ('4')`,
        description: `Ця бомба вибухне через 4 тіків.`
    },
    {
        image:       bomb_timer_3,
        title:       `BOMB_TIMER_3 ('3')`,
        description: `Ця бомба вибухне через 3 тіки.`
    },
    {
        image:       bomb_timer_2,
        title:       `BOMB_TIMER_2 ('2')`,
        description: `Ця бомба вибухне через 2 тіки.`
    },
    {
        image:       bomb_timer_1,
        title:       `BOMB_TIMER_1 ('1')`,
        description: `Ця бомба вибухне через 1 тік.`
    },
    {
        image:       boom,
        title:       `BOOM ('҉')`,
        description: `Бам! Це те, як бомба вибухає. При цьому
все, що може бути зруйновано – зруйнується разом із вашим Бомберменом,
якщо заздалегідь не заховатися.`
    },
    {
        image:       wall,
        title:       `WALL ('☼')`,
        description: `Неруйнівні стіни - їм вибухи бомб не страшні.`
    },
    {
        image:       destroyable_wall,
        title:       `DESTROYABLE_WALL ('#')`,
        description: `А ця стінка може бути зруйнована.`
    },
    {
        image:       destroyed_wall,
        title:       `DESTROYED_WALL ('H')`,
        description: `Це як зруйнована стіна виглядає, вона пропаде
в наступну секунду. Якщо це ви зробили - отримаєте бонусні бали.`
    },
    {
        image:       meat_chopper,
        title:       `MEAT_CHOPPER ('&')`,
        description: `Цей малий бігає по полю в довільному порядку.
Якщо він доторкнеться до Бомбермена - той помре, краще б вам знищити
цей шматок .... м'яса, за це ви отримаєте бонусні бали. Інакше тікайте!`
    },
    {
        image:       dead_meat_chopper,
        title:       `DEAD_MEAT_CHOPPER ('x')`,
        description: `Це мітчопер, який вибухнув. Якщо це ви зробили -
отримаєте бонусні бали.`
    },
    {
        image:       bomb_blast_radius_increase,
        title:       `BOMB_BLAST_RADIUS_INCREASE ('+')`,
        description: `Збільшує радіус* вибуху бомби. Діє лише для нових бомб.`
    },
    {
        image:       bomb_count_increase,
        title:       `BOMB_COUNT_INCREASE ('c')`,
        description: `Збільшує кількість* доступних гравцю бомб.`
    },
    {
        image:       bomb_immune,
        title:       `BOMB_IMMUNE ('i')`,
        description: `Дає імунітет до вибухів бомб (навіть чужих).`
    },
    {
        image:       bomb_remote_control,
        title:       `BOMB_REMOTE_CONTROL ('r')`,
        description: `Дистанційне керування детонатором. Бомба вибухає
при повторній дії ACT команди. В наявності є декілька* детонаторів.`
    },
    {
        image:       none,
        title:       `NONE ('')`,
        description: `Вільна секція, куди ви можете направити свого Бомбермена.`
    }
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
