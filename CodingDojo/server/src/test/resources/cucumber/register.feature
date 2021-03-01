Feature: Registration
  Registration process

Scenario: Register new user
  Given Clean all registration data

  When Login page opened in browser
  When Try to login as 'user1@mail.com' with 'password1' password in game 'first'
  Then See 'Wrong email or password' login error

  When Press register button
  And Try to register with: name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years', game 'first'
  Then On game board with url '/board/player/<PLAYER_ID>?code=<CODE>&game=first'