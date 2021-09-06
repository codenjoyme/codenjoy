Feature: Teams
  Game teams feature

Scenario: Admin can change team for any player
  Given User registered with name 'Stiven Pupkin', email 'user1@mail.com', password 'password1', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'
  Given User registered with name 'Eva Pupkina', email 'user2@mail.com', password 'password2', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'
  Given User registered with name 'Bob Pupkin', email 'user3@mail.com', password 'password3', city 'Moon', tech skills 'Java', company 'Home', experience '10 years'

  When Login as 'user1@mail.com' 'password1' in game 'sample'
  When Click logout

  When Login as 'user2@mail.com' 'password2' in game 'sample'
  When Click logout

  When Login as 'user3@mail.com' 'password3' in game 'sample'
  When Click logout

  Given Login to Admin page
  When Select game room 'sample'
  When Click LoadAll players
  When Enter value 'input-score' = '10' for the 'input-readable' is 'Stiven Pupkin' and click Save
  When Enter value 'input-score' = '20' for the 'input-readable' is 'Eva Pupkina' and click Save
  When Enter value 'input-score' = '30' for the 'input-readable' is 'Bob Pupkin' and click Save

  When Click ViewGame for the 'input-readable' is 'Eva Pupkina'
  And Save page url as 'EVA_BOARD'
  Then There are players '{Bob Pupkin={score=30}, Eva Pupkina={you=*, score=20}, Stiven Pupkin={score=10}}' on the leaderboard

  Given Open page with url '/admin?room=sample'
  When Enter value 'input-team' = '1' for the 'input-readable' is 'Eva Pupkina' and click Save
  Given Open page with url '<EVA_BOARD>'
  Then There are players '{Bob Pupkin={team=[1], score=30}, Eva Pupkina={team=[2], you=*, score=20}, Stiven Pupkin={team=[1], score=10}}' on the leaderboard

  Given Open page with url '/admin?room=sample'
  When Enter value 'input-team' = '1' for the 'input-readable' is 'Stiven Pupkin' and click Save
  Given Open page with url '<EVA_BOARD>'
  Then There are players '{Bob Pupkin={team=[1], score=30}, Eva Pupkina={team=[2], you=*, score=20}, Stiven Pupkin={team=[2], score=10}}' on the leaderboard

  Given Open page with url '/admin?room=sample'
  When Enter value 'input-team' = '1' for the 'input-readable' is 'Bob Pupkin' and click Save
  Given Open page with url '<EVA_BOARD>'
  Then There are players '{Bob Pupkin={score=30}, Eva Pupkina={you=*, score=20}, Stiven Pupkin={score=10}}' on the leaderboard