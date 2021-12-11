Feature: Admin page
  Admin page main features

Scenario: User cant open admin page but Admin can
  Given User registered with name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'

  # unsuccessfull login as user
  When Login as 'user1@mail.com' 'password1'
  And Open Admin page
  Then Error page opened with message 'Something wrong with your request. Please save you ticker number and ask site administrator.'

  # successfull login as admin
  When Login as 'admin@codenjoyme.com' 'admin'
  And Open Admin page
  Then Admin page opened with url '/admin?room=first'

Scenario: Admin can create/remove any room but not default
  Given Login to Admin page
  Then There are players in rooms '{first=0, sample=0, second=0, third=0}' on the admin page

  # create new room
  When Select game room 'second'
  Then Check game is 'second' and room is 'second'
  When Create new room 'newSecond'
  Then Check game is 'second' and room is 'newSecond'
  Then There are players in rooms '{first=0, newSecond=0, sample=0, second=0, third=0}' on the admin page

  When Open login page
  Then There is list of rooms '[first, newSecond, sample, second, third]' on the login form
  When Press register button
  Then There is list of rooms '[first, newSecond, sample, second, third]' on the register form

  # cant delete default game room
  When Open Admin page
  When Select game room 'first'
  Then Check game is 'first' and room is 'first'
  When Remove room
  Then Check game is 'first' and room is 'first'
  Then There are players in rooms '{first=0, newSecond=0, sample=0, second=0, third=0}' on the admin page

  # create another one room
  When Select game room 'third'
  Then Check game is 'third' and room is 'third'
  When Create new room 'newThird'
  Then Check game is 'third' and room is 'newThird'
  Then There are players in rooms '{first=0, newSecond=0, newThird=0, sample=0, second=0, third=0}' on the admin page

  When Open login page
  Then There is list of rooms '[first, newSecond, newThird, sample, second, third]' on the login form
  When Press register button
  Then There is list of rooms '[first, newSecond, newThird, sample, second, third]' on the register form

  # delete room
  When Open Admin page
  When Select game room 'newSecond'
  Then Check game is 'second' and room is 'newSecond'
  When Remove room
  Then Check game is 'second' and room is 'second'
  Then There are players in rooms '{first=0, newThird=0, sample=0, second=0, third=0}' on the admin page

  When Open login page
  Then There is list of rooms '[first, newThird, sample, second, third]' on the login form
  When Press register button
  Then There is list of rooms '[first, newThird, sample, second, third]' on the register form

  # delete room
  When Open Admin page
  When Select game room 'newThird'
  Then Check game is 'third' and room is 'newThird'
  When Remove room
  Then Check game is 'third' and room is 'third'
  Then There are players in rooms '{first=0, sample=0, second=0, third=0}' on the admin page

  When Open login page
  Then There is list of rooms '[first, sample, second, third]' on the login form
  When Press register button
  Then There is list of rooms '[first, sample, second, third]' on the register form

Scenario: Admin can close/open registration
  Given Login to Admin page
  Then Registration is active

  # close registration
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

  # open registration
  When Open Admin page
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

  # pause game
  When Click Pause game
  Then Game is paused

  When Select game room 'second'
  Then Check game is 'second' and room is 'second'
  Then Game is resumed

  When Select game room 'first'
  Then Check game is 'first' and room is 'first'
  Then Game is paused

  # resume game
  When Click Resume game
  Then Game is resumed

Scenario: Admin can change system tick time
  Given Login to Admin page
  Then Check game is 'first' and room is 'first'
  Then Timer period is 1000

  # update tick
  When Update timer period to 1101
  Then Timer period is 1101
  When Click Set timer period button
  Then Timer period is 1101

  When Select game room 'second'
  Then Check game is 'second' and room is 'second'
  Then Timer period is 1101

  # update tick
  When Update timer period to 1000
  Then Timer period is 1000
  When Click Set timer period button
  Then Timer period is 1000

  When Select game room 'first'
  Then Check game is 'first' and room is 'first'
  Then Timer period is 1000

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

  # pause game
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

  # resume game
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

  # inactivity enabled
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
  Then All levels are '(0)[1,1]=map1, (1)[1,2]=map2, (2)[2]=map3, (3)[3,1]=map4'

  # change map
  When Change map value at 0 to 'MAP1'
  When Change map value at 2 to 'MAP3'
  When Save all level maps

  When Open Admin page
  When Select game room 'third'
  Then All levels are '(0)[1,1]=MAP1, (1)[1,2]=map2, (2)[2]=MAP3, (3)[3,1]=map4'

  # change order
  When Change map key at 0 to '[Level] Map[2]'
  When Change map key at 2 to '[Level] Map[1,1]'
  When Save all level maps

  When Open Admin page
  When Select game room 'third'
  Then All levels are '(0)[1,1]=MAP3, (1)[1,2]=map2, (2)[2]=MAP1, (3)[3,1]=map4'

  # remove maps
  When Change map key at 1 to ''
  When Change map key at 3 to ''
  When Save all level maps

  When Open Admin page
  When Select game room 'third'
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1'

  # add new map
  When Add new map
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3]='

  When Change map key at 2 to '[Level] Map[3,1]'
  When Change map value at 2 to 'new1'
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3,1]=new1'

  When Add new map
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3,1]=new1, (3)[3,2]='
  When Change map value at 3 to 'new2'
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3,1]=new1, (3)[3,2]=new2'
  When Save all level maps

  When Open Admin page
  When Select game room 'third'
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3,1]=new1, (3)[3,2]=new2'

  # add new map with change map and order
  When Add new map
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3,1]=new1, (3)[3,2]=new2, (4)[3,3]='
  When Change map key at 4 to '[Level] Map[1,2]'
  When Change map value at 4 to 'new3'
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3,1]=new1, (3)[3,2]=new2, (4)[1,2]=new3'
  When Save all level maps

  When Open Admin page
  When Select game room 'third'
  Then All levels are '(0)[1,1]=MAP3, (1)[1,2]=new3, (2)[2]=MAP1, (3)[3,1]=new1, (4)[3,2]=new2'

  # add new map with remove maps
  When Change map key at 1 to ''
  When Change map key at 3 to ''
  When Change map key at 4 to ''
  Then All levels are '(0)[1,1]=MAP3, (1)<EMPTY1>=new3, (2)[2]=MAP1, (3)<EMPTY2>=new1, (4)<EMPTY3>=new2'
  When Add new map
  Then All levels are '(0)[1,1]=MAP3, (1)<EMPTY1>=new3, (2)[2]=MAP1, (3)<EMPTY2>=new1, (4)<EMPTY3>=new2, (5)[3]='
  When Change map value at 5 to 'MAP2'
  Then All levels are '(0)[1,1]=MAP3, (1)<EMPTY1>=new3, (2)[2]=MAP1, (3)<EMPTY2>=new1, (4)<EMPTY3>=new2, (5)[3]=MAP2'
  When Save all level maps

  When Open Admin page
  When Select game room 'third'
  Then All levels are '(0)[1,1]=MAP3, (1)[2]=MAP1, (2)[3]=MAP2'
