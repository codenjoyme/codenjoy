namespace ICanCode.Api
{
    public enum Element : short
    {
        // empty space where player can go
        EMPTY = (short)'-',
        FLOOR = (short)'.',

        // walls
        ANGLE_IN_LEFT = (short)'╔',
        WALL_FRONT = (short)'═',
        ANGLE_IN_RIGHT = (short)'┐',
        WALL_RIGHT = (short)'│',
        ANGLE_BACK_RIGHT = (short)'┘',
        WALL_BACK = (short)'─',
        ANGLE_BACK_LEFT = (short)'└',
        WALL_LEFT = (short)'║',
        WALL_BACK_ANGLE_LEFT = (short)'┌',
        WALL_BACK_ANGLE_RIGHT = (short)'╗',
        ANGLE_OUT_RIGHT = (short)'╝',
        ANGLE_OUT_LEFT = (short)'╚',
        SPACE = (short)' ',

        // laser machine
        LASER_MACHINE_CHARGING_LEFT = (short)'˂',
        LASER_MACHINE_CHARGING_RIGHT = (short)'˃',
        LASER_MACHINE_CHARGING_UP = (short)'˄',
        LASER_MACHINE_CHARGING_DOWN = (short)'˅',

        // lase machine ready
        LASER_MACHINE_READY_LEFT = (short)'◄',
        LASER_MACHINE_READY_RIGHT = (short)'►',
        LASER_MACHINE_READY_UP = (short)'▲',
        LASER_MACHINE_READY_DOWN = (short)'▼',

        // other stuff
        START = (short)'S',
        EXIT = (short)'E',
        HOLE = (short)'O',
        BOX = (short)'B',
        ZOMBIE_START = (short)'Z',
        GOLD = (short)'$',

        // your robot
        ROBO = (short)'☺',
        ROBO_FALLING = (short)'o',
        ROBO_FLYING = (short)'*',
        ROBO_LASER = (short)'☻',

        // other robot
        ROBO_OTHER = (short)'X',
        ROBO_OTHER_FALLING = (short)'x',
        ROBO_OTHER_FLYING = (short)'^',
        ROBO_OTHER_LASER = (short)'&',

        // laser
        LASER_LEFT = (short)'←',
        LASER_RIGHT = (short)'→',
        LASER_UP = (short)'↑',
        LASER_DOWN = (short)'↓',

        // zombie
        FEMALE_ZOMBIE = (short)'♀',
        MALE_ZOMBIE = (short)'♂',
        ZOMBIE_DIE = (short)'✝',

        // system elements, don't touch it
        FOG = (short)'F',
        BACKGROUND = (short)'G'
    }
}
