// vendor
import React from 'react';

import ai_down from './images/sprite/ai_down.png';
import ai_left from './images/sprite/ai_left.png';
import ai_prize from './images/sprite/ai_prize.png';
import ai_right from './images/sprite/ai_right.png';
import ai_up from './images/sprite/ai_up.png';
import bang from './images/sprite/bang.png';
import reefs from './images/sprite/reefs.png';
import torpedo from './images/sprite/torpedo.png';
import oil from './images/sprite/oil.png';
import water from './images/sprite/water.png';
import other_hero_down from './images/sprite/other_hero_down.png';
import other_hero_left from './images/sprite/other_hero_left.png';
import other_hero_right from './images/sprite/other_hero_right.png';
import other_hero_up from './images/sprite/other_hero_up.png';
import prize from './images/sprite/prize.png';
import prize_breaking_bad from './images/sprite/prize_breaking_bad.png';
import prize_immortality from './images/sprite/prize_immortality.png';
import prize_walking_on_fishnet from './images/sprite/prize_walking_on_fishnet.png';
import fishnet from './images/sprite/fishnet.png';
import hero_down from './images/sprite/hero_down.png';
import hero_left from './images/sprite/hero_left.png';
import hero_right from './images/sprite/hero_right.png';
import hero_up from './images/sprite/hero_up.png';
import seaweed from './images/sprite/seaweed.png';
import wall from './images/sprite/wall.png';
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
		image: bang,
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
		image: torpedo,
		title: `TORPEDO('•')`,
		description: `Торпеда. Власна або ворожа`,
	},
	{
		image: hero_up,
		title: `HERO_UP('▲')`,
		description: `Субмарина героя. Керується догори`,
	},
	{
		image: hero_right,
		title: `HERO_RIGHT('►')`,
		description: `Субмарина героя. Керується вправо`,
	},
	{
		image: hero_down,
		title: `HERO_DOWN('▼')`,
		description: `Субмарина героя. Керується додолу`,
	},
	{
		image: hero_left,
		title: `HERO_LEFT('◄')`,
		description: `Субмарина героя. Керується вліво`,
	},
	{
		image: other_hero_up,
		title: `OTHER_HERO_UP('˄')`,
		description: `Ворожий танк. Керується догори`,
	},
	{
		image: other_hero_right,
		title: `OTHER_HERO_RIGHT('˃')`,
		description: `Ворожа субмарина. Керується вправо`,
	},
	{
		image: other_hero_down,
		title: `OTHER_HERO_DOWN('˅')`,
		description: `Ворожа субмарина. Керується додолу`,
	},
	{
		image: other_hero_left,
		title: `OTHER_HERO_LEFT('˂')`,
		description: `Ворожа субмарина. Керується вліво`,
	},
	{
		image: ai_up,
		title: `AI_UP('?')`,
		description: `AI-субмарина. Керується догори`,
	},
	{
		image: ai_right,
		title: `AI_RIGHT('»')`,
		description: `AI-субмарина. Керується вправо`,
	},
	{
		image: ai_down,
		title: `AI_DOWN('¿')`,
		description: `AI-субмарина. Керується додолу`,
	},
	{
		image: ai_left,
		title: `AI_LEFT('«')`,
		description: `AI-субмарина. Керується вліво`,
	},
	{
		image: ai_prize,
		title: `AI_PRIZE('◘')`,
		description: `Призова AI-субмарина`,
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
