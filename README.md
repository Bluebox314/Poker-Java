# Poker In Java
This project is a small console poker game, played with Texas Holdem rules. All player action and game information is typed and printed into the console respectively. Input the number of players, each players name, and then press enter to start the game loop *note: there is not current functional game loop, the code for this is still in progress (see Poker.java).* The console will inform the user whose turn it is, and to press enter when the player is ready to take their turn. Players cannot look at the console while it is not their turn, or else they could see the players hand. The console prints off all relevant data about the game events to the player, including the community cards, other players actions, your current stack size, etc. On a turn it is possible to choose between the 3 actions by inputting the corresponding letter to the available actions listed *(see Player.java notes on issues with taking turns).*

# Images
![image](https://github.com/user-attachments/assets/5f3da6ce-9f2f-496d-a2c5-6f5a4572e261)

### WIP
This project is not finished, and there are some pieces of missing functionality and bugs. Not all bugs found are currently documented, but I've done my best to mark down a list of tasks before each method to show issues. I am not done commenting the code either, so some sections may contain less explanation. If you see any issues running the code, notify me on the issue so I can comment it on the relevant method (and fix it). As of now, the game when run will only player 3 consecutive rounds with the inputted players, before printing the toString info for each player.

### How to Run
The source code for this project is not currently runnable without an IDE, if you want to run the code you will have to copy the Eclipse project folder that all the data is stored in and paste it into the IDE as your own project. I'm working on making a runnable jar file for this project, but this isn't far on the list of priorities (it does seem easy to do however). 
