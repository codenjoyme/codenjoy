Feature: Registration
  Registration process

Scenario: Register new user and try login/logout
  When Open login page
  And Try to login as 'user1@mail.com' with 'password1' password in room 'first'
  Then See 'Wrong email or password' login error

  When Press register button
  And Try to register with: name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years', game 'first', room 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'
  Then User registered in database as 'Registration.User(email=user1@mail.com, id=<PLAYER_ID>, readableName=Stiven Pupkin, approved=1, code=<CODE>, data=Moon|Java|Home|10 years)'

  Then Logout link present
  When Click logout
  Then We are on page with url '/'
  Then Login link present

  When Click login
  And Try to login as 'user1@mail.com' with 'password1' password in room 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'