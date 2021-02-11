package advisors

import (
	"github.com/arukim/expansion/game"
	m "github.com/arukim/expansion/models"
)

type Advisor interface {
	MakeTurn(b *game.Board, t *m.Turn)
}
