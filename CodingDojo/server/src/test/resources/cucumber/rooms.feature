Feature: Rooms
  Game rooms feature

Scenario: The user can choose which room to play in when registering and logging in after the admin has created this room
  Given Open login page
  Then There is list of rooms '[first, sample, second]' on the login form
  When Press register button
  Then There is list of rooms '[first, sample, second]' on the register form

  Given Login to Admin page
  When Create new room 'first2' for game 'first'
  Then There is list of rooms '[first, first2, sample, second]' on the admin page

  Given Open login page
  Then There is list of rooms '[first, first2, sample, second]' on the login form
  When Press register button
  Then There is list of rooms '[first, first2, sample, second]' on the register form

  Given Login to Admin page
  Then There are players in rooms '{first=1, first2=0, sample=0, second=0}' on the admin page
  When Click logout

  Given Open registration page
  When Try to register with: name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years', game 'first', room 'first2'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first2'
  Then There are players '[Stiven Pupkin]' on the leaderboard

  Given Login to Admin page
  # admin in the 'first' room
  Then There are players in rooms '{first=1, first2=1, sample=0, second=0}' on the admin page
  When Click logout

  Given Open registration page
  When Try to register with: name 'Eva Pupkina', email 'user2@mail.com', password 'password2', city 'Moon', tech skills 'Java', company 'Home', experience '10 years', game 'first', room 'first2'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first2'
  Then There are players '[Stiven Pupkin, Eva Pupkina]' on the leaderboard

  Given Login to Admin page
  # admin in the 'first' room
  Then There are players in rooms '{first=1, first2=2, sample=0, second=0}' on the admin page
  When Click logout

  Given Open registration page
  When Try to register with: name 'Bob Pupkin', email 'user3@mail.com', password 'password3', city 'Moon', tech skills 'Java', company 'Home', experience '10 years', game 'first', room 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'
  Then There are players '[Bob Pupkin]' on the leaderboard

  Given Login to Admin page
  # admin in the 'first' room
  Then There are players in rooms '{first=2, first2=2, sample=0, second=0}' on the admin page
  When Click logout