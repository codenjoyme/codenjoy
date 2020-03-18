package bomberman

const (
	BOMBERMAN      = '☺' // this is what he usually looks like
	BOMB_BOMBERMAN = '☻' // this is if he is sitting on own bomb
	DEAD_BOMBERMAN = 'Ѡ' // oops your Bomberman is dead (don"`t worry he will appear somewhere in next move)
	// you"`re getting -200 for each death

	/// this is other players Bombermans
	OTHER_BOMBERMAN      = '♥' // this is what other Bombermans looks like
	OTHER_BOMB_BOMBERMAN = '♠' // this is if player just set the bomb
	OTHER_DEAD_BOMBERMAN = '♣' // enemy corpse (it will disappear shortly right on the next move)
	// if you"`ve done it you"`ll get +1000

	/// the bombs
	BOMB_TIMER_5 = '5' // after bomberman set the bomb the timer starts (5 tacts)
	BOMB_TIMER_4 = '4' // this will blow up after 4 tacts
	BOMB_TIMER_3 = '3' // this after 3
	BOMB_TIMER_2 = '2' // two
	BOMB_TIMER_1 = '1' // one
	BOOM         = '҉' // Boom! this is what is bomb does everything that is destroyable got destroyed

	/// walls
	WALL             = '☼' // indestructible wall - it will not fall from bomb
	DESTROYABLE_WALL = '#' // this wall could be blowed up
	DESTROYED_WALL   = 'H' // this is how broken wall looks like it will dissapear on next move
	// if it"`s you did it - you"`ll get +10 points.

	/// meatchoppers
	MEAT_CHOPPER = '&' // this guys runs over the board randomly and gets in the way all the time
	// if it will touch bomberman - it will die
	// you"`d better kill this piece of ... meat you"`ll get +100 point for it
	DEAD_MEAT_CHOPPER = 'x' // this is chopper corpse

	/// a void
	NONE = ' ' // this is the only place where you can move your Bomberman

	BoardSize = 33
	N         = 3 // Blast size
)

type Action string

const (
	UP    Action = "UP"
	DOWN  Action = "DOWN"
	RIGHT Action = "RIGHT"
	LEFT  Action = "LEFT"
	ACT   Action = "ACT"
	STOP  Action = "STOP"
	// Combinations
	UPA    Action = "UP,ACT"
	DOWNA  Action = "DOWN,ACT"
	RIGHTA Action = "RIGHT,ACT"
	LEFTA  Action = "LEFT,ACT"
	// Vice versa, order matters
	AUP    Action = "ACT,UP"
	ADOWN  Action = "ACT,DOWN"
	ARIGHT Action = "ACT,RIGHT"
	ALEFT  Action = "ACT,LEFT"
)

func (m Action) IsValid() bool {
	switch m {
	case UP, DOWN, RIGHT, LEFT, ACT, STOP, UPA, DOWNA, RIGHTA, LEFTA, AUP, ADOWN, ARIGHT, ALEFT:
		return true
	default:
		return false
	}
}
