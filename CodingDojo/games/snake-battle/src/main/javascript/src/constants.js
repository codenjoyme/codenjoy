export const ELEMENT = {
      NONE: ' ',        // empty space where user can get into
      WALL: '0',        // wall cannot get through
      START_FLOOR: '#', // snake start
      OTHER: '?',

      APPLE: '0',
      STONE: '?',
      FLYING_PILL: '©',
      FURY_PILL: '®',
      GOLD: '$',

      // player
      HEAD_DOWN: 'Ў',
      HEAD_LEFT: '<',
      HEAD_RIGHT: '>',
      HEAD_UP: '^',
      HEAD_DEAD: 'O',
      HEAD_EVIL: '¦',
      HEAD_FLY: '¦',
      HEAD_SLEEP: '&',

      TAIL_END_DOWN: 'L',
      TAIL_END_LEFT: 'L',
      TAIL_END_UP: 'г',
      TAIL_END_RIGHT: '¬',
      TAIL_INACTIVE: '~',

      BODY_HORIZONTAL: '=',
      BODY_VERTICAL: '¦',
      BODY_LEFT_DOWN: '¬',
      BODY_LEFT_UP: '-',
      BODY_RIGHT_DOWN: 'г',
      BODY_RIGHT_UP: 'L',

      // enemy
      ENEMY_HEAD_DOWN: '?',
      ENEMY_HEAD_LEFT: '<',
      ENEMY_HEAD_RIGHT: '>',
      ENEMY_HEAD_UP: '?',
      ENEMY_HEAD_DEAD: 'O',
      ENEMY_HEAD_EVIL: '¦',
      ENEMY_HEAD_FLY: '¦',
      ENEMY_HEAD_SLEEP: 'o',

      ENEMY_TAIL_END_DOWN: '¤',
      ENEMY_TAIL_END_LEFT: '?',
      ENEMY_TAIL_END_UP: '?',
      ENEMY_TAIL_END_RIGHT: 'o',
      ENEMY_TAIL_INACTIVE: '*',

      ENEMY_BODY_HORIZONTAL: '-',
      ENEMY_BODY_VERTICAL: '¦',
      ENEMY_BODY_LEFT_DOWN: '¬',
      ENEMY_BODY_LEFT_UP: '-',
      ENEMY_BODY_RIGHT_DOWN: '-',
      ENEMY_BODY_RIGHT_UP: 'L'
};

export const COMMANDS = {
    UP: 'UP', // snake momves
    DOWN: 'DOWN',
    LEFT: 'LEFT',
    RIGHT: 'RIGHT',
    ACT: 'ACT', // drop stone if any
};
