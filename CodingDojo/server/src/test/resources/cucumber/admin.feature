Feature: Admin page
  Admin page main features

Scenario: User cant open admin page but Admin can
  Given User registered with name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'

  When Login as 'user1@mail.com' 'password1'
  And Try open Admin page
  Then Error page opened with message 'Something wrong with your request. Please save you ticker number and ask site administrator.'

  When Login as 'admin@codenjoyme.com' 'admin'
  And Try open Admin page
  Then Admin page opened with url '/admin?room=first'

Scenario: Admin can close/open registration
  Given Login to Admin page
  Then Registration is active

  When Click Close registration
  Then Registration was closed

  When Click logout

  When Open registration page
  Then See 'Server registration was closed' registration error
  And There is no controls on registration form

  When Open login page
  Then See 'Server registration was closed' login error
  And There is no controls on login form

  When Open admin login page
  And Try to login as 'admin@codenjoyme.com' with 'admin' password
  Then Admin page opened with url '/admin?room=first'

  When Try open Admin page
  Then Registration was closed

  When Click Open registration
  Then Registration is active

  When Click logout

  When Open registration page
  And Try to register with: name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years', game 'first', room 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'
  Then User registered in database as 'Registration.User(email=user1@mail.com, id=<PLAYER_ID>, readableName=Stiven Pupkin, approved=1, code=<CODE>, data=Moon|Java|Home|10 years)'

  When Open login page
  And Try to login as 'user1@mail.com' with 'password1' password in room 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'

Scenario: Admin can pause/resume game only in this room
  Given Login to Admin page
  Then Check game is 'first' and room is 'first'
  Then Game is resumed

  When Click Pause game
  Then Game is paused

  When Select game room 'second'
  Then Check game is 'second' and room is 'second'
  Then Game is resumed

  When Select game room 'first'
  Then Check game is 'first' and room is 'first'
  Then Game is paused

  When Click Resume game
  Then Game is resumed

Scenario: When game room is paused then is no communication with websocket client
  Given User registered with name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'

  When Login as 'user1@mail.com' 'password1' in game 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'
  Then Websocket client 'client1' connected successfully to the '/board/player/<PLAYER_ID>?code=<CODE>'
  Then Websocket 'client1' send 'ACT' and got '      \n      \n      \n      \n ☺    \n      \n'
  Then Websocket 'client1' send 'ACT' and got '      \n      \n      \n      \n ☺    \n      \n'
  Then Websocket 'client1' send 'ACT' and got '      \n      \n      \n      \n ☺    \n      \n'

  When Click logout

  Given Login to Admin page
  Then Check game is 'first' and room is 'first'
  Then Game is resumed

  When Click Pause game
  Then Game is paused

  When Click logout

  When Login as 'user1@mail.com' 'password1' in game 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'
  Then Websocket client 'client1' connected successfully to the '/board/player/<PLAYER_ID>?code=<CODE>'
  Then Websocket 'client1' send 'ACT' and got nothing
  Then Websocket 'client1' send 'ACT' and got nothing
  Then Websocket 'client1' send 'ACT' and got nothing

  When Click logout

  Given Login to Admin page
  Then Check game is 'first' and room is 'first'
  Then Game is paused

  When Click Resume game
  Then Game is resumed

  When Click logout

  When Login as 'user1@mail.com' 'password1' in game 'first'
  Then Board page opened with url '/board/player/<PLAYER_ID>?code=<CODE>' in room 'first'
  Then Websocket client 'client1' connected successfully to the '/board/player/<PLAYER_ID>?code=<CODE>'
  # TODO [RK#3]: Consider separation for two clauses
  #              (e.g. @When `clause1 and clause2` ->  @When `clause1` @And `clause2`).
  Then Websocket 'client1' send 'ACT' and got '      \n      \n      \n      \n ☺    \n      \n'
  Then Websocket 'client1' send 'ACT' and got '      \n      \n      \n      \n ☺    \n      \n'
  Then Websocket 'client1' send 'ACT' and got '      \n      \n      \n      \n ☺    \n      \n'

Scenario: Admin can turn on / turn off kick for inactive players
  Given User registered with name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'
  Given User registered with name 'Eva Pupkina', email 'user2@mail.com', password 'password2', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'

  When Login as 'user1@mail.com' 'password1' in game 'sample'
  Then Websocket client 'client1' connected successfully to the '/board/player/<PLAYER_ID>?code=<CODE>'
  When Click logout

  When Login as 'user2@mail.com' 'password2' in game 'sample'
  Then Websocket client 'client2' connected successfully to the '/board/player/<PLAYER_ID>?code=<CODE>'
  When Click logout

  Given Login to Admin page
  When Select game room 'sample'
  When Click LoadAll players

  When Set inactivity kick enabled checkbox to true
  And Set inactivity timeout parameter to 10
  And Press inactivity settings save button

  Then Inactivity parameters '{kickEnabled=true, timeout=10}'
  And All players inactivity ticks are reset

  When Websocket 'client1' send 'ACT'
  # Waiting for 3 seconds to ensure that `client1` keeps on sending commands
  Then Wait for 3 seconds when refresh is true
  And Shutdown 'client1' websocket runner

  When Websocket 'client2' send 'ACT'
  # Waiting for 15 seconds to ensure that `client2` keeps on sending commands.
  # In the same time `client1` increments inactivity ticks to the specified max.
  Then Wait for 15 seconds when refresh is true
  And Shutdown 'client2' websocket runner

  Then Player 'user1@mail.com' is kicked true
  And Player 'user2@mail.com' is kicked false

Scenario: Administrator can change level maps
  Given User registered with name 'Stiven Pupkin', email 'user@mail.com', password 'password', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'

  When Login as 'user@mail.com' 'password' in game 'third'
  Then Websocket client 'client' connected successfully to the '/board/player/<PLAYER_ID>?code=<CODE>'
  When Click logout

  Given Login to Admin page
  When Select game room 'third'
  Then All levels are '{[Level] Map[1,1]=map1, [Level] Map[1,2]=map2, [Level] Map[1,3]=map3, [Level] Map[1,4]=map4, [Level] Map[2]=map5, [Level] Map[3,1]=map6, [Level] Map[3,2]=map7, [Level] Map[4,1]=map8, [Level] Map[4,2]=map9}'

  # change map
  When Change map value at 0 to 'changedMap1'
  When Change map value at 2 to 'changedMap3'
  When Save all level maps

  When Try open Admin page
  When Select game room 'third'
  Then All levels are '{[Level] Map[1,1]=changedMap1, [Level] Map[1,2]=map2, [Level] Map[1,3]=changedMap3, [Level] Map[1,4]=map4, [Level] Map[2]=map5, [Level] Map[3,1]=map6, [Level] Map[3,2]=map7, [Level] Map[4,1]=map8, [Level] Map[4,2]=map9}'

  # change order
  When Change map key at 0 to '[Level] Map[1,3]'
  When Change map key at 2 to '[Level] Map[1,1]'
  When Save all level maps

  When Try open Admin page
  When Select game room 'third'
  Then All levels are '{[Level] Map[1,1]=changedMap3, [Level] Map[1,2]=map2, [Level] Map[1,3]=changedMap1, [Level] Map[1,4]=map4, [Level] Map[2]=map5, [Level] Map[3,1]=map6, [Level] Map[3,2]=map7, [Level] Map[4,1]=map8, [Level] Map[4,2]=map9}'

  # remove map
  When Change map key at 5 to ''
  When Change map key at 7 to ''
  When Change map key at 8 to ''
  When Save all level maps

  When Try open Admin page
  When Select game room 'third'
  Then All levels are '{[Level] Map[1,1]=changedMap3, [Level] Map[1,2]=map2, [Level] Map[1,3]=changedMap1, [Level] Map[1,4]=map4, [Level] Map[2]=map5, [Level] Map[3,2]=map7}'

  # add new
  # add new with change map
  # add new with change order
  # add new with remove map
