package models

type Movement struct {
	Count     int    `json:"count"`
	Region    Point  `json:"region"`
	Direction string `json:"direction"`
}

type Increase struct {
	Count  int   `json:"count"`
	Region Point `json:"region"`
}

type Turn struct {
	Movements []Movement `json:"movements"`
	Increase  []Increase `json:"increase"`
}
