# Manual Test Plan for Controller

## Prerequisites

* jdk-15.0.2

## Environment Setup

* Run the MainLoop class.

## How to scroll the scroll box

Hold your mouse inside the box and move it upwards or downwards.

## Operations and the results

### Game start scene with valid inputs
![title](screenshots/test_start/1.png)  
We successfully added three players to our game!  
![title](screenshots/test_start/2.png)  

### Game start scene with invalid inputs  
If we try to put some invalid inputs, a popup will complain about this.  
![title](screenshots/test_start/bad_inputs/1.png)
![title](screenshots/test_start/bad_inputs/2.png) 

### Play a number card
We want to play (id:2 yellow NINE) in this case  
![title](screenshots/test_play_numbercard/1.png)  
We switch to player2's turn. Player1's card has been reduced by one.
The current player's hand cards are automatically hid unless we click hid/reveal button.
The current color and current card are also updated.  
![title](screenshots/test_play_numbercard/2.png)

### Play a draw two card
Try to play a draw two card.  
![title](screenshots/test_drawtwo/1.png)  
The states are updated. Notice that player 2 has been added one card according to the 
split draw rule. There is also one card in the stack. But we can play a blue five 
to avoid penalty.  
![title](screenshots/test_drawtwo/2.png)  
We will choose to take the penalty at this round.  
![title](screenshots/test_drawtwo/3.png)  
Now player 1 has been added one card.
![title](screenshots/test_drawtwo/4.png)

### Play a skip card  
We can play a blue skip card at this turn.  
![title](screenshots/test_skip/1.png)  
Since it is a skip card, the current player can take no action but skip this round and draw one card.

![title](screenshots/test_skip/2.png)  
The player 1 has draw one card and the current card type is set to none to prevent skip forever.
![title](screenshots/test_skip/3.png)

### Play a change color card
![title](screenshots/test_changecolor/1.png)
![title](screenshots/test_changecolor/2.png)

### Check if game over
![title](screenshots/test_gameover/1.png)
![title](screenshots/test_gameover/2.png)