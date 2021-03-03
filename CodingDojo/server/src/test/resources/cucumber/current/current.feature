Feature: Current feature
  Current feature I want to run separately

Scenario: Admin can pause/resume game
  Given Login to Admin page
  Then Game is resumed

  When Click Pause game

  Then Game is paused

  When Click Resume game

  Then Game is resumed