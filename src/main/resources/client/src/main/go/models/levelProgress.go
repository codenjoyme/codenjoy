package models

type LevelProgress struct {
	Current    int
	LastPassed int
	Total      int
	Scores     bool
	multiple   bool
}
