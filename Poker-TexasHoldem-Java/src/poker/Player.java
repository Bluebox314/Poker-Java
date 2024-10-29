package poker;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Player {
	private int playerID; //stores index in playerlist for handeval to give to program so it knows who won
	private final String name; //stores player name
	private int turn; //stores turn index (round level)
	private int turnsTaken; //stores number of turns taken in a given betting round (for action history) (bet round level)
	private int money; //players total money (global level)
	private int currentBet; //the outstanding bet the player currently has (bet round level)
	private boolean inRound; //stores if the player has folded (round level)
	private boolean takenTurn; //stores if player has had a turn this bet round (bet round level)
	private ArrayList<String> actionHistory = new ArrayList<String>(); //stores the actions of a player (bet round level)
	private Card hand[] = {new Card(), new Card()}; //stores the hand of the player (round level)

	Player(int playerID, String name, int turn, int money) { //simple constructor
		this.playerID = playerID;
		this.name = name;
		this.turn = turn;
		this.money = money;
		inRound = true;
		currentBet = 0;
		turnsTaken = 0;
	}
	//start of default methods
	public int getPlayerID() {
		return playerID;
	}
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public String getName() {
		return name;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getTurnsTaken() {
		return turnsTaken;
	}
	public void setTurnsTaken(int turnsTaken) {
		this.turnsTaken = turnsTaken;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getCurrentBet() {
		return currentBet;
	}
	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}
	public boolean getTakenTurn() {
		return takenTurn;
	}
	public void setTakenTurn(boolean takenTurn) {
		this.takenTurn = takenTurn;
	}
	public ArrayList<String> getActionHistory() {
		return actionHistory;
	}
	public boolean getInRound() {
		return inRound;
	}
	public void setInRound(boolean inRound) {
		this.inRound = inRound;
	}	
	public Card getHand(int index) {
		return hand[index];
	}
	public void setHand(Card card, int index) {
		hand[index] = card;
	}
	public String toString() { //only prints off the first 3 actions in actionHistory (all that will ever be needed)
		return "playerID = "+playerID+"\n"+
			   "name = "+name+"\n"+
			   "turn = "+turn+"\n"+
			   "money = "+money+"\n"+
			   "currentBet = "+currentBet+"\n"+
			   "takenTurn = "+takenTurn+"\n"+
			   "turnsTaken = "+turnsTaken+"\n"+
			   "Action History = "+actionHistory+"\n"+
			   "inRound = "+inRound+"\n"+
			   "hand = "+hand[0].displayCard()+", "+hand[1].displayCard();
	}
	//end of default methods
	
	private static Scanner printer = new Scanner(System.in); //used in takeTurn()
	
	//takeTurn() is currently a working method, but is missing some functionality as seen below with my todo list
	
	//fix issues with betting rules being violated
	//fix issues with impropper betting exceptions
	//improve maximum bet detection to not allow betting more than a player can afford to call
	//add detection for when a player must go all in because they would not be able to afford the next blind
	//add a line notifying the player of when new comCards are revealed to hand and game info
	//add a line showing how many players are still in the round after the start of a followup betting round
	//add clarification for when a player goes all in
	//add extra way of inputting bet in the form of giving a percent of your money
	public void takeTurn() throws InputMismatchException { //performs player actions for player object following input
		String response; //used to record player responses
		int intResponse; //same here but int
		takenTurn = true; //this player has now taken a turn
		turnsTaken++; //increment our turns taken
		
		//blinds code<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
		//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		//if its the first or second turn in the first round you pay blinds
		if(Poker.betRoundLoops==0&&Poker.betRound==0&&(turn==0||turn==1)) { 
			//blinds are an automatic bet, so they are coded the same way bets are, without adding to actionHistory
			if(turn==0) { //first turn is small blind
				money -= Poker.blind/2; //you lose the money you bet (blind stores the big blind, the small is half the big)
				currentBet = Poker.blind/2; //your bet is recorded
				Poker.highBet = Poker.blind; //the big blind is set as the highest bet
			} else { //second turn is the big blind
				money -= Poker.blind; //you lose the money you bet
				currentBet = Poker.blind; //your bet is recorded
			}
		}
		//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		//end of blinds code<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		
		
		//hand and game information display code<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		if(Poker.betRound>0) System.out.println("Pot: "+Poker.pot);
		System.out.print("It is "+name+"'s turn\nYour hand: "); //print player name
		System.out.println(hand[0].displayCard()+", "+hand[1].displayCard()); //print player hand
		
		
		//start of print comCards============================================================================================
		if(Poker.betRound>=1) { //if the round is the flop or greater, print 3 comCards
			System.out.println("Community Cards:");
			System.out.print(Poker.comCards[0].displayCard()+", ");
			System.out.print(Poker.comCards[1].displayCard()+", ");
			System.out.println(Poker.comCards[2].displayCard());
		}
		if(Poker.betRound>=2) { //if the round is the turn or greater, print another comCard
			System.out.print(Poker.comCards[3].displayCard());
		}
		if(Poker.betRound>=3) { //if the round is the river, print another comCard
			System.out.println(", "+Poker.comCards[4].displayCard());
		} else if(Poker.betRound==2){
			System.out.println();
		}
		//end of print comCards==============================================================================================
		
		
		//print previous player's actions====================================================================================
		int describedTurns = 0; //stores the number of turns we have printed to show this player
		int totalTurnsTaken = 0; //stores the total number of turns taken by players this round
		String playerAction; //used as an abreviation
		String actingName; //used as an abreviation
		for(int i=0;i<Poker.playerList.length;i++) { //properly assigning totalTurnsTaken
			totalTurnsTaken += Poker.playerList[i].turnsTaken;
		}
		
		int betRoundLoops = 0;
		for(int i=0;describedTurns<totalTurnsTaken-1;i++) { //display all turns before our own turn
			//we can't abreviate 
			if(Poker.betRound==0&&Poker.playerList[i].actionHistory.isEmpty()) {
				continue;
			}
			if(Poker.playerList[i].turnsTaken==0||Poker.playerList[i].actionHistory.get(betRoundLoops).equals("skip")) {
				continue;
			}
			playerAction = Poker.playerList[i].actionHistory.get(betRoundLoops); //set out abreviations
			actingName = Poker.playerList[i].getName(); //set abreviation
			
			//interpret the actionHistory variable and print the info about it
			//info is recorded as a string with the name of the action followed by metadata about it
			if(playerAction.startsWith("check")) {
				System.out.println(actingName+" checked");
				
			} else if(playerAction.startsWith("call")) { //a call action has the value called in it
				//grab the value called from player action to add to the record
				System.out.println(actingName+" called at "+playerAction.substring(4));
				
			} else if(playerAction.startsWith("bet")) { //a bet action has the value bet in it
				//grab the value bet from player action to add to the record
				System.out.println(actingName+" bet "+playerAction.substring(3));
				
			} else if(playerAction.startsWith("raise")) { //a raise action has 2 other values in it
				//grab the value raised from the player action to add to the record, along side the bet that was raised
				System.out.print(actingName+" raised "+playerAction.substring(5,playerAction.indexOf("E")));
				System.out.println(" at "+playerAction.substring(playerAction.indexOf("E")+1));
				
			} else { //only other possible action is fold, so we assume they folded
				System.out.println(actingName+" folded");
			}
			describedTurns++; //we have now described one more turn
			
			if(i==Poker.playerList.length-1) { //reset i to prevent the loop from geting a player index that doesn't exist
				betRoundLoops++; //start grabbing each players next recorded action
				i = -1; //i is incremented right after so it's set it one lower than it needs to be
			}
		} 
		betRoundLoops = 0; //reset our loop counter so next time we print actions they are printed in order
		//end of print previous players actions==============================================================================
		
		
		//print player bet status============================================================================================
		if(currentBet==Poker.highBet) { //if the player has no bet to match, they do not see a bet to match
			System.out.println("You match the highest bet at: "+currentBet);
		} else {  //print off the bet that the player has to match along with the money they already have out
			System.out.println("The bet to match: "+Poker.highBet+"\nYour bet ammount: "+currentBet);
		} 
		System.out.println("Your current balance: "+money); //print player balance
		//end of print player bet status=====================================================================================
		
		
		//start of print player action choices===============================================================================
		if(currentBet==Poker.highBet) {
			System.out.println("A: Check");
			System.out.println("B: Bet");
		} else {
			System.out.println("A: Call");
			System.out.println("B: Raise");
			System.out.println("C: Fold");
		}
		//end of print player action choices==================================================
		//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		//end of hand and game information display code<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
		
		
		//response code<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>
		//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		response = printer.nextLine(); //ask for player input
			//start of check method==========================================================================================
		if(response.equals("a")&&currentBet==Poker.highBet) { //if response was a and your option was check, run check code
			actionHistory.add("check"); //your action is recorded
			for(int i=0;i<42;i++) System.out.println("."); //print 35 lines and end method
			return;
			//end of check code==============================================================================================
		
			
			//start of call code=============================================================================================
		} else if(response.equals("a")&&currentBet!=Poker.highBet) { //if response was a and option was call, run call code
			actionHistory.add("call"+Poker.highBet); //your action is recorded
			money -= Poker.highBet-currentBet; //you lose the difference between what you already had bet and what you match
			currentBet = Poker.highBet; //your bet is equal to the ammount the last guy who bet betted
			
			for(int i=0;i<42;i++) System.out.println("."); //print 35 lines and end method
			return;
			//end of call code===============================================================================================
		
			
			//start of bet code==============================================================================================
		} else if(response.equals("b")) { //if response was b the run bet/raise code (raise is differentiated in code)
			int raiseOrBet; //stores whether the player raised or bet money
			if(currentBet==Poker.highBet) { //if the player bet money they see "Bet how much?"
				raiseOrBet = 1;
				System.out.println("Bet how much?\n"
						+ "100% of your money is: "+money+"\n"
						+ "15% of your money is: "+(int)(money*.15)+"\n"
						+ "30% of your money is: "+(int)(money*.3)+"\n"
						+ "50% of your money is: "+(int)(money*.5));
			} else { //if you didn't bet, you raised; if the player raised money they see "Raise by"
				raiseOrBet = 0;
				//run the code for calling to set the player ready to bet
				money -= Poker.highBet-currentBet; //lose the difference between what you already had bet and what you match
				currentBet = Poker.highBet; //your bet is equal to the ammount the last guy who bet betted
				
				System.out.println("Raise by?\n"
						+ "100% of your money is: "+money+"\n"
						+ "15% of your money is: "+(int)(money*.15)+"\n"
						+ "30% of your money is: "+(int)(money*.3)+"\n"
						+ "50% of your money is: "+(int)(money*.5));
			}
			intResponse = printer.nextInt();
			printer.nextLine(); //reset for next string test	
			
			
			if(intResponse<Poker.blind || intResponse>money) { //in case of silly billys
				System.out.println("Invalid bet. Your bet must be <= your max money and >= the big blind");
				turnsTaken--; //decrement turnsTaken so this turn isn't counted twice
				takeTurn();
				return; //return for incorrect bet info
			}
			
			
			if(raiseOrBet==1) { //if you bet then it records a bet and the ammount you bet
				actionHistory.add("bet"+intResponse); //your action is recorded
			} else { //if you raised then it records a raise, the ammount you raised by, and value of the bet you raised
				//the E in the center is used to seperate the info for when we translate it
				actionHistory.add("raise"+intResponse+"E"+Poker.highBet); //your action is recorded
			}
			money -= intResponse; //you lose exactly what you bet
			currentBet += intResponse; //your last bet is added to what you just bet
			Poker.highBet = currentBet; //the bet to match is the total money you've bet
			
			for(int i=0;i<42;i++) System.out.println("."); //print 35 lines and end method
			return;
			//end of bet code================================================================================================
		
			
			//start of fold code=============================================================================================
		} else if(response.equals("c")) { //if response was c, then run fold code
			actionHistory.add("fold"); //your action is recorded
			inRound = false; //a folded player is no longer in the round
			Poker.pot += currentBet; //you lose all money you previously bet this bet round after folding
			for(int i=0;i<42;i++) System.out.println("."); //print 35 lines and end method
			return;
			//end of fold code===============================================================================================
		
			
			//undefined response code========================================================================================
		} else { //if response was invalid, run undefined response code
			System.out.println("Cannot define input, please retry turn");
			turnsTaken--; //without reseting this, the console tries to retrieve a null action from this turn
			takeTurn(); //take the turn over
			return;
			//end of undefined response code=================================================================================
		}
		//end of response code<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
		//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><
	}
} //end of class
