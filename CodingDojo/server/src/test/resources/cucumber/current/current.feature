Feature: Current feature
  Current feature I want to run separately

Scenario: Admin can pause/resume game only in this room
  Given Login to Admin page
  Then Check game room is 'first'
  Then Game is resumed

  When Click Pause game
  Then Game is paused

  When Select game room 'second'
  Then Check game room is 'second'
  Then Game is resumed

  When Select game room 'first'
  Then Check game room is 'first'
  Then Game is paused

  When Click Resume game
  Then Game is resumed