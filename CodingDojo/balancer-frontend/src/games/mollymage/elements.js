// vendor
import React, { Component } from 'react';

// proj
// import potion from './images/sprite/potion.png';
import potion_blast_radius_increase from './images/sprite/potion_blast_radius_increase.png';
import potion_hero from './images/sprite/potion_hero.png';
import potion_count_increase from './images/sprite/potion_count_increase.png';
import potion_immune from './images/sprite/potion_immune.png';
import potion_remote_control from './images/sprite/potion_remote_control.png';
// import potion_timer_0 from './images/sprite/potion_timer_0.png';
import potion_timer_1 from './images/sprite/potion_timer_1.png';
import potion_timer_2 from './images/sprite/potion_timer_2.png';
import potion_timer_3 from './images/sprite/potion_timer_3.png';
import potion_timer_4 from './images/sprite/potion_timer_4.png';
import potion_timer_5 from './images/sprite/potion_timer_5.png';
import hero from './images/sprite/hero.png';
import boom from './images/sprite/boom.png';
import dead_hero from './images/sprite/dead_hero.png';
import dead_ghost from './images/sprite/dead_ghost.png';
import treasure_box from './images/sprite/treasure_box.png';
import opened_treasure_box from './images/sprite/opened_treasure_box.png';
import ghost from './images/sprite/ghost.png';
import none from './images/sprite/none.png';
import other_potion_hero from './images/sprite/other_potion_hero.png';
import other_hero from './images/sprite/other_hero.png';
import other_dead_hero from './images/sprite/other_dead_hero.png';
import enemy_potion_hero from './images/sprite/enemy_potion_hero.png';
import enemy_hero from './images/sprite/enemy_hero.png';
import enemy_dead_hero from './images/sprite/enemy_dead_hero.png';
import wall from './images/sprite/wall.png';

//own

export default [
    {
        image:       hero,
        title:       `HERO ('☺')`,
        description: `Ваша Моллі.`,
    },
    {
        image:       potion_hero,
        title:       `POTION_HERO ('☻')`,
        description: `Ваша Моллі, якщо вона сидить на зіллі що має вибухнути.`,
    },
    {
        image:       dead_hero,
        title:       `DEAD_HERO ('Ѡ')`,
        description: `Йойкс! Ваша Моллі померла. Нехвилюйтеся, вона з'явиться
десь на полі із новим Раудндом ви зможете нею керувати, але цілком ймовірно за
це ви отримаєте штрафні бали.`,
    },
    {
        image:       other_hero,
        title:       `OTHER_HERO ('♥')`,
        description: `Моллі супротивника.`,
    },
    {
        image:       other_potion_hero,
        title:       `OTHER_POTION_HERO ('♠')`,
        description: `Моллі супротивника, яка сидить на зіллі що має вибухнути.`
    },
    {
        image:       other_dead_hero,
        title:       `OTHER_DEAD_HERO ('♣')`,
        description: `Так виглядає мертва Моллі супротивника.
Якщо це ви її підірвали - ви отримаєте бонусні бали.`
    },
    {
        image:       enemy_hero,
        title:       `ENEMY_HERO ('♡')`,
        description: `Моллі ворога.`,
    },
    {
        image:       other_potion_hero,
        title:       `ENEMY_POTION_HERO ('♤')`,
        description: `Моллі ворога, яка сидить на зіллі що має вибухнути.`
    },
    {
        image:       other_dead_hero,
        title:       `ENEMY_DEAD_HERO ('♧')`,
        description: `Так виглядає мертва Моллі ворога.
Якщо це ви її підірвали - ви отримаєте бонусні бали.`
    },
    {
        image:       potion_timer_5,
        title:       `POTION_TIMER_5 ('5')`,
        description: `Після того як Моллі зварить зілля таймер
почне працювати (всього 5 тіків/секунд). Скоріше за все ви не побачите
не цей символ, а POTION_HERO('☻'), але пам'ятайте - 5 секунд і вибух.
Тре тікати швидко.`
    },
    {
        image:       potion_timer_4,
        title:       `POTION_TIMER_4 ('4')`,
        description: `Це зілля вибухне через 4 тіків.`
    },
    {
        image:       potion_timer_3,
        title:       `POTION_TIMER_3 ('3')`,
        description: `Це зілля вибухне через 3 тіки.`
    },
    {
        image:       potion_timer_2,
        title:       `POTION_TIMER_2 ('2')`,
        description: `Це зілля вибухне через 2 тіки.`
    },
    {
        image:       potion_timer_1,
        title:       `POTION_TIMER_1 ('1')`,
        description: `Це зілля вибухне через 1 тік.`
    },
    {
        image:       boom,
        title:       `BOOM ('҉')`,
        description: `Бам! Це те, як зілля вибухає. При цьому
все, що може бути зруйновано – зруйнується разом із вашою Моллі,
якщо заздалегідь не заховатися.`
    },
    {
        image:       wall,
        title:       `WALL ('☼')`,
        description: `Неруйнівні стіни - їм вибухи бомб не страшні.`
    },
    {
        image:       treasure_box,
        title:       `TREASURE_BOX ('#')`,
        description: `А ця скринька може бути відчинена вибухом.
Часом всередині можна щось знайти.`
    },
    {
        image:       opening_treasure_box,
        title:       `OPENING_TREASURE_BOX ('H')`,
        description: `Ця скринька відчиняється, вона пропаде
в наступну секунду, а на її місці можно знайти щось корисне.
Якщо це ви зробили - отримаєте бонусні бали.`
    },
    {
        image:       ghost,
        title:       `GHOST ('&')`,
        description: `Цей малий бігає по полю в довільному порядку.
Якщо він доторкнеться до Моллі - та помре, краще б вам знищити
його, за це ви отримаєте бонусні бали. Інакше тікайте!`
    },
    {
        image:       dead_ghost,
        title:       `DEAD_GHOST ('x')`,
        description: `Це привид, якого вбили. Так-так в нас це можливо.
Якщо це ви зробили - отримаєте бонусні бали.`
    },
    {
        image:       potion_blast_radius_increase,
        title:       `POTION_BLAST_RADIUS_INCREASE ('+')`,
        description: `Збільшує радіус* вибуху зілля. Діє лише для новозвареного зілля.`
    },
    {
        image:       potion_count_increase,
        title:       `POTION_COUNT_INCREASE ('c')`,
        description: `Збільшує кількість* доступного гравцю зілля.`
    },
    {
        image:       potion_immune,
        title:       `POTION_IMMUNE ('i')`,
        description: `Дає імунітет до вибухів зілля (навіть чужих).`
    },
    {
        image:       potion_remote_control,
        title:       `POTION_REMOTE_CONTROL ('r')`,
        description: `Дистанційне керування зіллям. Зілля вибухає
при повторній дії ACT команди. В наявності є декілька* спроб.`
    },
    {
        image:       none,
        title:       `NONE ('')`,
        description: `Вільна секція, куди ви можете направити свою Моллі.`
    }
];
