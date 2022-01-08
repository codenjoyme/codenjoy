// vendor
import React from 'react';

import ai_down from './images/sprite/ai_down.png';
import ai_left from './images/sprite/ai_left.png';
import ai_right from './images/sprite/ai_right.png';
import ai_side_right from './images/sprite/ai_right.png';
import ai_up from './images/sprite/ai_up.png';
import ai_side_left from './images/sprite/ai_side_left.png';
import ai_prize_up from './images/sprite/ai_prize_up.png';
import ai_prize_down from './images/sprite/ai_prize_down.png';
import ai_prize_left from './images/sprite/ai_prize_left.png';
import ai_prize_right from './images/sprite/ai_prize_right.png';
import ai_prize_side_left from './images/sprite/ai_prize_side_left.png';
import ai_prize_side_right from './images/sprite/ai_prize_side_right.png';
import explosion from './images/sprite/explosion.png';
import reefs from './images/sprite/reefs.png';
import torpedo_left from './images/sprite/torpedo_left.png';
import torpedo_right from './images/sprite/torpedo_right.png';
import torpedo_up from './images/sprite/torpedo_up.png';
import torpedo_down from './images/sprite/torpedo_down.png';
import torpedo_side_left from './images/sprite/torpedo_side_left.png';
import torpedo_side_right from './images/sprite/torpedo_side_right.png';
import oil from './images/sprite/oil.png';
import water from './images/sprite/water.png';
import other_hero_down from './images/sprite/other_hero_down.png';
import other_hero_left from './images/sprite/other_hero_left.png';
import other_hero_right from './images/sprite/other_hero_right.png';
import other_hero_up from './images/sprite/other_hero_up.png';
import other_hero_side_left from './images/sprite/other_hero_side_left.png';
import other_hero_side_right from './images/sprite/other_hero_side_right.png';
import prize from './images/sprite/prize.png';
import prize_breaking_bad from './images/sprite/prize_breaking_bad.png';
import prize_immortality from './images/sprite/prize_immortality.png';
import prize_walking_on_fishnet from './images/sprite/prize_walking_on_fishnet.png';
import fishnet from './images/sprite/fishnet.png';
import hero_down from './images/sprite/hero_down.png';
import hero_left from './images/sprite/hero_left.png';
import hero_right from './images/sprite/hero_right.png';
import hero_up from './images/sprite/hero_up.png';
import hero_side_left from './images/sprite/hero_side_left.png';
import hero_side_right from './images/sprite/hero_side_right.png';
import seaweed from './images/sprite/seaweed.png';
import iceberg_huge from './images/sprite/iceberg_huge.png';
import iceberg_medium_down from './images/sprite/iceberg_medium_down.png';
import iceberg_small_down_left from './images/sprite/iceberg_small_down_left.png';
import iceberg_small_down_right from './images/sprite/iceberg_small_down_right.png';
import iceberg_small_down_down from './images/sprite/iceberg_small_down_down.png';
import iceberg_medium_left from './images/sprite/iceberg_medium_left.png';
import iceberg_small_left_right from './images/sprite/iceberg_small_left_right.png';
import iceberg_small_left_left from './images/sprite/iceberg_small_left_left.png';
import iceberg_medium_right from './images/sprite/iceberg_medium_right.png';
import iceberg_small_right_right from './images/sprite/iceberg_small_right_right.png';
import iceberg_small_up_right from './images/sprite/iceberg_small_up_right.png';
import iceberg_medium_up from './images/sprite/iceberg_medium_up.png';
import iceberg_small_up_down from './images/sprite/iceberg_small_up_down.png';
import iceberg_small_up_left from './images/sprite/iceberg_small_up_left.png';
import iceberg_small_up_up from './images/sprite/iceberg_small_up_up.png';
import prize_visibility from './images/sprite/prize_visibility.png';
import prize_no_sliding from './images/sprite/prize_no_sliding.png';


//own

export default [
	{
		image: water,
		title: `WATER(' ')`,
		description: `Вільні води, через які може рухатися герой`,
	},
	{
		image: reefs,
		title: `REEFS('☼')`,
		description: `Риф, який (у звичайних умовах) не можна зруйнувати`,
	},
	{
		image: explosion,
		title: `EXPLOSION('Ѡ')`,
		description: `Місце вибуху`,
	},
	{
		image: oil,
		title: `OIL('#')`,
		description: `Вилив нафти. Змушує героя ковзати`,
	},
	{
		image: seaweed,
		title: `SEAWEED('%')`,
		description: `Водорості. Може приховувати героя та торпеди`,
	},
	{
		image: fishnet,
		title: `FISHNET('~')`,
		description: `Риболовецькі сітки. Не дозвляють (у звичайних умовах) рухатись герою`,
	},
	{
		image: iceberg_huge,
		title: `ICEBERG_HUGE('╬')`,
		description: `Айсберг, який можна зруйнувати 3 торпедами` ,
	},
	{
		image: iceberg_medium_down,
		title: `ICEBERG_MEDIUM_DOWN('╩')`,
		description: `Айсберг, який можна зруйнувати 2 торпедами`,
	},
	{
		image: iceberg_medium_up,
		title: `ICEBERG_MEDIUM_UP('╦')`,
		description: `Айсберг, який можна зруйнувати 2 торпедами`,
	},
	{
		image: iceberg_medium_left,
		title: `ICEBERG_MEDIUM_LEFT('╠')`,
		description: `Айсберг, який можна зруйнувати 2 торпедами`,
	},
	{
		image: iceberg_medium_right,
		title: `ICEBERG_MEDIUM_RIGHT('╣')`,
		description: `Айсберг, який можна зруйнувати 2 торпедами`,
	},
	{
		image: iceberg_small_down_down,
		title: `ICEBERG_SMALL_DOWN_DOWN('╨')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_up_up,
		title: `ICEBERG_SMALL_UP_UP('╥')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_left_left,
		title: `ICEBERG_SMALL_LEFT_LEFT('╞')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_right_right,
		title: `ICEBERG_SMALL_RIGHT_RIGHT('╡')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_left_right,
		title: `ICEBERG_SMALL_LEFT_RIGHT('│')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_up_down,
		title: `ICEBERG_SMALL_UP_DOWN('─')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_up_left,
		title: `ICEBERG_SMALL_UP_LEFT('┌')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_up_right,
		title: `ICEBERG_SMALL_UP_RIGHT('┐')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_down_left,
		title: `ICEBERG_SMALL_DOWN_LEFT('└')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: iceberg_small_down_right,
		title: `ICEBERG_SMALL_DOWN_RIGHT('┘')`,
		description: `Айсберг, який можна зруйнувати 1 торпедою`,
	},
	{
		image: torpedo_left,
		title: `TORPEDO_LEFT('•')`,
		description: `Торпеда. Власна або ворожа. Скерована вліво`,
	},
    {
        image: torpedo_right,
        title: `TORPEDO_RIGHT('¤')`,
        description: `Торпеда. Власна або ворожа. Скерована вправо`,
    },
    {
        image: torpedo_up,
        title: `TORPEDO_UP('ø')`,
        description: `Торпеда. Власна або ворожа. Скерована догори`,
    },
    {
        image: torpedo_down,
        title: `TORPEDO_DOWN('×')`,
        description: `Торпеда. Власна або ворожа. Скерована додолу`,
    },
    {
        image: torpedo_side_left,
        title: `TORPEDO_SIDE_LEFT('t')`,
        description: `Торпеда (вигляд збоку). Власна або ворожа. Скерована вліво`,
    },
    {
        image: torpedo_side_right,
        title: `TORPEDO_SIDE_RIGHT('T')`,
        description: `Торпеда (вигляд збоку). Власна або ворожа. Скерована вправо`,
    },
    {
		image: hero_up,
		title: `HERO_UP('▲')`,
		description: `Субмарина героя. Скерована догори`,
	},
	{
		image: hero_right,
		title: `HERO_RIGHT('►')`,
		description: `Субмарина героя. Скерована вправо`,
	},
	{
		image: hero_down,
		title: `HERO_DOWN('▼')`,
		description: `Субмарина героя. Скерована додолу`,
	},
	{
		image: hero_left,
		title: `HERO_LEFT('◄')`,
		description: `Субмарина героя. Скерована вліво`,
	},
    {
		image: hero_side_left,
		title: `HERO_SIDE_LEFT('h')`,
		description: `Субмарина героя (вигляд збоку). Скерована вліво`,
	},
    {
        image: hero_side_right,
        title: `HERO_SIDE_RIGHT('H')`,
        description: `Субмарина героя (вигляд збоку). Скерована вправо`,
    },
	{
		image: other_hero_up,
		title: `OTHER_HERO_UP('˄')`,
		description: `Ворожа субмарина. Скерована догори`,
	},
	{
		image: other_hero_right,
		title: `OTHER_HERO_RIGHT('˃')`,
		description: `Ворожа субмарина. Скерована вправо`,
	},
	{
		image: other_hero_down,
		title: `OTHER_HERO_DOWN('˅')`,
		description: `Ворожа субмарина. Скерована додолу`,
	},
	{
		image: other_hero_left,
		title: `OTHER_HERO_LEFT('˂')`,
		description: `Ворожа субмарина. Скерована вліво`,
	},
	{
		image: other_hero_side_left,
		title: `OTHER_HERO_SIDE_LEFT('o')`,
		description: `Ворожа субмарина (вигляд збоку). Скерована вліво`,
	},
    {
        image: other_hero_side_right,
        title: `OTHER_HERO_SIDE_RIGHT('O')`,
        description: `Ворожа субмарина (вигляд збоку). Скерована вправо`,
    },
	{
		image: enemy_hero_up,
		title: `ENEMY_HERO_UP('Ô')`,
		description: `Ворожа субмарина із іншої команди. Скерована догори`,
	},
	{
		image: enemy_hero_right,
		title: `ENEMY_HERO_RIGHT('£')`,
		description: `Ворожа субмарина із іншої команди. Скерована вправо`,
	},
	{
		image: enemy_hero_down,
		title: `ENEMY_HERO_DOWN('Ç')`,
		description: `Ворожа субмарина із іншої команди. Скерована додолу`,
	},
	{
		image: enemy_hero_left,
		title: `ENEMY_HERO_LEFT('Ð')`,
		description: `Ворожа субмарина із іншої команди. Скерована вліво`,
	},
	{
		image: enemy_hero_side_left,
		title: `ENEMY_HERO_SIDE_LEFT('e')`,
		description: `Ворожа субмарина із іншої команди (вигляд збоку). Скерована вліво`,
	},
    {
        image: enemy_hero_side_right,
        title: `ENEMY_HERO_SIDE_RIGHT('E')`,
        description: `Ворожа субмарина із іншої команди (вигляд збоку). Скерована вправо`,
    },
	{
		image: ai_up,
		title: `AI_UP('?')`,
		description: `AI-субмарина. Скерована догори`,
	},
	{
		image: ai_right,
		title: `AI_RIGHT('»')`,
		description: `AI-субмарина. Скерована вправо`,
	},
	{
		image: ai_down,
		title: `AI_DOWN('¿')`,
		description: `AI-субмарина. Скерована додолу`,
	},
	{
		image: ai_left,
		title: `AI_LEFT('«')`,
		description: `AI-субмарина. Скерована вліво`,
	},
	{
		image: ai_side_left,
		title: `AI_SIDE_LEFT('a')`,
		description: `AI-субмарина (вигляд збоку). Скерована вліво`,
	},
    {
        image: ai_side_right,
        title: `AI_SIDE_RIGHT('A')`,
        description: `AI-субмарина (вигляд збоку). Скерована вправо`,
    },
    {
        image: ai_prize_up,
        title: `AI_PRIZE_UP('î')`,
        description: `Призова AI-субмарина. Скерована догори`,
    },
    {
        image: ai_prize_right,
        title: `AI_PRIZE_RIGHT('}')`,
        description: `Призова AI-субмарина. Скерована вправо`,
    },
    {
        image: ai_prize_down,
        title: `AI_PRIZE_DOWN('w')`,
        description: `Призова AI-субмарина. Скерована додолу`,
    },
    {
        image: ai_prize_left,
        title: `AI_PRIZE_LEFT('{')`,
        description: `Призова AI-субмарина. Скерована вліво`,
    },
    {
        image: ai_prize_side_left,
        title: `AI_PRIZE_SIDE_LEFT('p')`,
        description: `Призова AI-субмарина (вигляд збоку). Скерована вліво`,
    },
    {
        image: ai_prize_side_right,
        title: `AI_PRIZE_SIDE_RIGHT('P')`,
        description: `Призова AI-субмарина (вигляд збоку). Скерована вправо`,
    },
	{
		image: prize,
		title: `PRIZE('!')`,
		description: `Приз, який випав с призової AI-субмарини`,
	},
	{
		image: prize_immortality,
		title: `PRIZE_IMMORTALITY('1')`,
		description: `Приз із невразливістю до ворожих торпед`,
	},
	{
		image: prize_breaking_bad,
		title: `PRIZE_BREAKING_BAD('2')`,
		description: `Приз, який дає можливість руйнувати айсберги та рифи за один постріл`,
	},
	{
		image: prize_walking_on_fishnet,
		title: `PRIZE_WALKING_ON_FISHNET('3')`,
		description: `Приз, який дозволяє рухатися через риболовецькі сітки`,
	},
	{
		image: prize_visibility,
		title: `PRIZE_VISIBILITY('4')`,
		description: `Надає можливість бачити техніку супротивника приховану у водоростях`,
	},
	{
		image: prize_no_sliding,
		title: `PRIZE_NO_SLIDING('5')`,
		description: `Запобігає ковзанню в нафті`,
	},
];
