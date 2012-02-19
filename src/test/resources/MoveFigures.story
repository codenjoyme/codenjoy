Meta:

Narrative:
As a user
I want to have a control over current figure movement
So that I can play and have fun!

Scenario: Player moves current figure to the left
Given a started game
When player moves left
And Timer ticks
Then current figure at console position 4, 19
When player moves left 2 steps
Then current figure at console position 2, 19
When player moves left 3 steps
Then current figure at console position 0, 19

Scenario: Player moves current figure to the right
Given a started game
When player moves right
And Timer ticks
Then current figure at console position 6, 19
When player moves right 2 steps
Then current figure at console position 8, 19
When player moves left 3 steps
Then current figure at console position 10, 19
