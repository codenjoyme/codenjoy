// vendor
import React from 'react';

// proj
// import potion from './images/sprite/potion.png';
import potion_blast_radius_increase from './images/sprite/potion_blast_radius_increase.png';
import hero_potion from './images/sprite/hero_potion.png';
import potion_count_increase from './images/sprite/potion_count_increase.png';
import potion_immune from './images/sprite/potion_immune.png';
import potion_remote_control from './images/sprite/potion_remote_control.png';
import potion_timer_1 from './images/sprite/potion_timer_1.png';
import potion_timer_2 from './images/sprite/potion_timer_2.png';
import potion_timer_3 from './images/sprite/potion_timer_3.png';
import potion_timer_4 from './images/sprite/potion_timer_4.png';
import potion_timer_5 from './images/sprite/potion_timer_5.png';
import hero from './images/sprite/hero.png';
import blast from './images/sprite/blast.png';
import hero_dead from './images/sprite/hero_dead.png';
import ghost_dead from './images/sprite/ghost_dead.png';
import treasure_box from './images/sprite/treasure_box.png';
import treasure_box_opening from './images/sprite/treasure_box_opening.png';
import ghost from './images/sprite/ghost.png';
import none from './images/sprite/none.png';
import other_hero_potion from './images/sprite/other_hero_potion.png';
import other_hero from './images/sprite/other_hero.png';
import other_hero_dead from './images/sprite/other_hero_dead.png';
import enemy_hero from './images/sprite/enemy_hero.png';
import enemy_hero_potion from './images/sprite/enemy_hero_potion.png';
import enemy_hero_dead from './images/sprite/enemy_hero_dead.png';
import wall from './images/sprite/wall.png';

//own

export default [
    {
        image:       hero,
        title:       `HERO ('☺')`,
        description: `Ваша Моллі.`,
    },
    {
        image:       hero_potion,
        title:       `HERO_POTION ('☻')`,
        description: `Ваша Моллі, якщо вона сидить на зіллі що має вибухнути.`,
    },
    {
        image:       hero_dead,
        title:       `HERO_DEAD ('Ѡ')`,
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
        image:       other_hero_potion,
        title:       `OTHER_HERO_POTION ('♠')`,
        description: `Моллі супротивника, яка сидить на зіллі що має вибухнути.`
    },
    {
        image:       other_hero_dead,
        title:       `OTHER_HERO_DEAD ('♣')`,
        description: `Так виглядає мертва Моллі супротивника.
Якщо це ви її підірвали - ви отримаєте бонусні бали.`
    },
    {
        image:       enemy_hero,
        title:       `ENEMY_HERO ('ö')`,
        description: `Моллі ворога.`,
    },
    {
        image:       enemy_hero_potion,
        title:       `ENEMY_HERO_POTION ('Ö')`,
        description: `Моллі ворога, яка сидить на зіллі що має вибухнути.`
    },
    {
        image:       enemy_hero_dead,
        title:       `ENEMY_HERO_DEAD ('ø')`,
        description: `Так виглядає мертва Моллі ворога.
Якщо це ви її підірвали - ви отримаєте бонусні бали.`
    },
    {
        image:       potion_timer_5,
        title:       `POTION_TIMER_5 ('5')`,
        description: `Після того як Моллі зварить зілля таймер
почне працювати (всього 5 тіків/секунд). Скоріше за все ви не побачите
не цей символ, а HERO_POTION('☻'), але пам'ятайте - 5 секунд і вибух.
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
        image:       blast,
        title:       `BLAST ('҉')`,
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
        image:       treasure_box_opening,
        title:       `TREASURE_BOX_OPENING ('H')`,
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
        image:       ghost_dead,
        title:       `GHOST_DEAD ('x')`,
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
