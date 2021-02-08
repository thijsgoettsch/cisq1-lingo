Feature: Starting a new game
  As a User,
  I want to start a new game,
  So that I can play a new game
Scenario: A new game has been started
  When I start a new game
  Then I should be able to play the game

Feature: Guessing a word
  As a User,
  I want to be able to fill in a 5,6 or 7 letter word,
  So that I can try to attempt a guess
Scenario Outline: Guessing a word
  Given I am playing a game,
  When My "<guess>" has the same amount of letters as the "<word>"
  Then I get to see "<feedback>" which tells me if on of the letters are correct, absent or present
  Examples:
    | word       | guess     | feedback                                             |
    | world      | earth     | INVALID, INVALID, CORRECT, INVALID, INVALID          |
    | laptop     | laptop    | CORRECT, CORRECT, CORRECT, CORRECT, CORRECT, CORRECT |

Feature: Get feedback
  As a User,
  I want to be able to see feedback on the attempt I made for guessing a word,
  So that I know what letter is correct, incorrect or in the wrong spot

Feature: Starting a new round
  As a User,
  I want to be able to start a new round,
  So that I can continue playing the game

Feature: Continue game later on
  As a User,
  I want to be able to return to an already started game
  So that I can pause the game and keep playing with the progression later on