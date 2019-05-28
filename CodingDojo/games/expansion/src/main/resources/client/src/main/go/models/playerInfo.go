package models

type PlayerInfo struct {
	ForcesFree    int
	TerritorySize int
	ForcesTotal   int
	MinesCount    int

	Mines  map[Point]bool
	Forces map[Point]int
}
