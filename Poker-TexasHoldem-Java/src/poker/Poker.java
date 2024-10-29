package poker;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
//hit the plus sign left to a method declaration to see its contents
public class Poker {
	
	public static Player[] playerList; //stores the player objects
	public static int pot = 0; //stores money to be won in a round (round level)
	public static Card comCards[] = new Card[5]; //the set of 5 community cards (round level)
	public static int highBet = 500; //stores the highest current bet (starts at big blind) (bet round level)
	public static int betRound = 0; //stores how many betting rounds have been played in one round (round level)
	public static int betRoundLoops = 0; //stores the number of turns the player where turn=0 has taken (bet round level)
	public static int blind = 500; //stores blind values (small blind is half of big, this is big blind) (main level)
	public static ArrayList<Card> deck; //stores 52 cards (round level)
	
	private static Scanner printer = new Scanner(System.in);
	
	public static void main(String[] args) {
		deck = makeDeck();
		
		//this block creates our player data, with names and our first turn order
		System.out.println("Welcome to poker, please input your # of players!");
		int numOfPlayers;
		try {
			numOfPlayers = printer.nextInt(); 
		} catch(InputMismatchException e) {
			System.out.println("Invalid character type\numOfPlayers set to 1");
			numOfPlayers = 1;
		}
		printer.nextLine(); //reset for next line
		
		playerList = new Player[numOfPlayers]; //playerList can be a normal list as it will not change in game
		String newPlayerName;
		System.out.println("Input the first players name: ");
		for(int i=0;i<numOfPlayers;i++) {
			newPlayerName = printer.nextLine();
			playerList[i] = new Player(i, newPlayerName, i, 15000);
			if(i+1<numOfPlayers) {
				System.out.println("Next player:");
			}
		}
		//we also create the object list of players
		
		//start of game code
		System.out.println("Type enter to begin the game!");
		printer.nextLine();
		
		deal(); //deals com cards and hands from DECK
		//Start of test code-------------------------------------------------------------------------------------------------
		//currently the handEval always prints out its calculations (showing remaining players hands)
		
		//play 3 rounds and see all player data after rounds finish
		///*
		playRound();
		playRound();
		playRound();
		
		for(int i=0;i<playerList.length;i++) {
			System.out.println("\n"+playerList[i].toString());
		}
		//*/
		
		
		//print out each players hand and evaluate them to determine who wins based off hand value
		/*
		System.out.println("Player #:"+findRoundWinner()+" won the round\n");
		
		System.out.println("ComCards:");
		for(int i=0;i<comCards.length;i++) {
			System.out.println(comCards[i].displayCard());
		}
		System.out.println();
			
		for(int i=0;i<playerList.length;i++) {
			System.out.println(playerList[i].getPlayerID()+":");
			System.out.println(playerList[i].getHand(0).displayCard());
			System.out.println(playerList[i].getHand(1).displayCard());
			System.out.println();
		}
		*/
		//End of test code---------------------------------------------------------------------------------------------------
		printer.close();
		System.out.close();
	} //end of main
	
	//unused early method for ending the game
	public static boolean checkForWinner() { //method that checks if any one player has all the money (unused)
		for(int i=0;i<playerList.length;i++) {
			if(playerList[i].getMoney()==(15000*playerList.length)) {
				return true;
			}
		}
		return false;
	}
	
	//card dealing
	public static void deal() { //gives each player two cards, and creates community cards
		int cardPick;
		for(int i=0;i<playerList.length;i++) {
			for(int ii=0;ii<2;ii++) {
				cardPick = (int)(Math.random()*deck.size());
				playerList[i].setHand(deck.get(cardPick), ii); 
				deck.remove(cardPick);
			}
		}
		for(int i=0;i<5;i++) {
			cardPick = (int)(Math.random()*deck.size());
			comCards[i] = deck.get(cardPick);
			deck.remove(cardPick);
		}
	} //leaves the deck without the cards that were dealt	
	public static ArrayList<Card> makeDeck() { //create a deck of cards for use
		ArrayList<Card> deck = new ArrayList<Card>();
		for(int i = 0; i<53; i++) {	
			deck.add(new Card(i));
		}
		deck.remove(0);
		return deck;
	}
	
	//round playing
	//poor commenting on most of this section
	//not fully debugged or tested due to lack of time
	public static void playRound() { //loops until one player has won the round and the pot	
		for(int i=0;i<4;i++) { //loops through 4 betting rounds, each with different ammounts of com cards
			playBetRound(); //play round
		}
		
		//determine who won the round		
		int winningPlayer = findRoundWinner();
		//end of determine who won the round
		
		//winning player gets the money in the pot
		playerList[winningPlayer].setMoney(playerList[winningPlayer].getMoney()+pot);
		System.out.println(playerList[winningPlayer].getName()+" won this round!");
		
		//reset all the round level values
		deck = makeDeck(); //shuffle
		deal(); //sets comCards and player hands
		betRound = 0;
		pot = 0;
		for(int i=0;i<playerList.length;i++) { //reset player values
			if(playerList[i].getTurn()!=-1) {
				playerList[i].setTurn(playerList[i].getTurn()-1);
			} else {
				playerList[i].setTurn(playerList.length-1);
			}
			if(playerList[i].getMoney()>0) {
				playerList[i].setInRound(true);
			}
		}
	}	
	public static void playBetRound() { //loops until all players are done betting on a round
		int firstTurn = findLeftOfDealer();
		int playersIn = 0;
		int offset = firstTurn;
		int offsetI;
		
		for(int i=0;!isBetRoundOver();i++){ //actual betting round loop
			//check if the round is by fold code
			playersIn = 0;
			for(int j=0;j<playerList.length;j++) { //if everyone but one person folds they win the hand
				if(playerList[j].getInRound()) {
					playersIn++;
				}
			}
			if(playersIn==1) {
				return; //end bet round if only one person is left in the round
			}
			//end of check if round is over by fold code
			
			offsetI = offset+i;
			if(offsetI>=playerList.length) {
				offsetI = i-Math.abs(offset-i)-offset;
			}
			
			
			try {
				if(playerList[offsetI].getInRound()) { //only non folded players take turns
					System.out.println("Next player up: "+playerList[offsetI].getName()
									+ "\npress enter to take your turn");
					printer.nextLine(); //to avoid a player seeing anothers hand after their turn
					playerList[offsetI].takeTurn();
				} else { //if a player has folded add a placeholder to their action history
					//this is done so that when printing action history, this player's skipped turn isn't acknowledged
					playerList[offsetI].getActionHistory().add("skip");
				}
			} catch(InputMismatchException e) {
				System.out.println("A player has inputted incorrect information!\nreseting...");
				printer.nextLine(); //failing to input correct info will reset the round (may cause var reset problems)
				playBetRound();
				return;
			}
			
			if(i==playerList.length-1) { //starts us over to prevent index out of bounds
				
				betRoundLoops++;
				i = -1; //it gets incremented right after so we set it one lower than it needs
			}
		} //end of betting round
		
		//reset all betting round dependent values
		for(int i=0;i<playerList.length;i++) {
			if(playerList[i].getInRound()) { //non folding players get set up for next bet round
				pot += playerList[i].getCurrentBet(); //add each players outstanding bet to the pot
				playerList[i].setCurrentBet(0);
				playerList[i].setTurnsTaken(0);
				playerList[i].setTakenTurn(false);
				playerList[i].getActionHistory().clear();
			} else { //players who fold get reset for the next round
				playerList[i].setCurrentBet(0);
				playerList[i].setTurnsTaken(0);
				playerList[i].getActionHistory().clear();
			}
		}
		//reset poker class variables dependent on bet rounds as well
		betRoundLoops = 0;
		betRound++;
		highBet = 0;
	}

	//round playing tools
	public static boolean isBetRoundOver() { //returns true if every bet is satisfied
		for(int i=0;i<playerList.length;i++) { //checks if each player has matched the highest bet or folded
			if((playerList[i].getCurrentBet()!=highBet)||!playerList[i].getTakenTurn()) {
				if(!playerList[i].getInRound()) {
					//this loop does not need anything but returns
				} else {
					return false;
				}
			} else {
				
			}
		}
		return true;
	}
	public static int findLeftOfDealer() { //returns int index of player who will take the first turn
		for(int i=0;i<playerList.length;i++) {
			if(playerList[i].getTurn()==0) {
				return i;
			}
		}
		return 0; //failsafe
	}
	//add code to respond to a tie and split pot accordingly
	public static int findRoundWinner() { //returns int value of the index of the winning player
		ArrayList<Card> fullHand = new ArrayList<Card>(); //stores a player hand + com cards
		int winningPlayer = playerList.length; //stores the player who won the round
		double handValue;
		double highestHandValue = 0;
		
		for(int i=0;i<playerList.length;i++) { //gather the values of each players hand
			if(!playerList[i].getInRound()) { //skip players not part of the last betting round
				continue;
			}
			
			fullHand.clear();
			for(int j=0;j<7;j++) { //assign fullHand to be equal to player[i]'s hand plus the comCards
		 		if(j<=4) {
		 			fullHand.add(comCards[j]);
		 		} else {
					fullHand.add(playerList[i].getHand(j-5));
				}
			} //done assigning fullHand for this player
			
			System.out.println("\nhand eval of playerID: "+playerList[i].getPlayerID());
			for(int j=0;j<fullHand.size();j++) {
				System.out.println(fullHand.get(j).displayCard());
			}
			System.out.println("Hand value: "+evaluateHand(fullHand)+"\n");
			
			handValue = evaluateHand(fullHand); //evaluate full hand from 9.14-1.02
			
			System.out.println("Hand value used: "+handValue);
			System.out.println("Compared against: "+highestHandValue);
			
			if(handValue>highestHandValue) { //if this player's full hand is the best, then store it
				highestHandValue = handValue;
				winningPlayer = playerList[i].getPlayerID();
				System.out.println("Assigned highestHandValue to: "+highestHandValue);
				System.out.println("Player in lead right now: "+playerList[i].getPlayerID());
			}
		}
		return winningPlayer;
	}
	
	//hand eval tools (redo comments)
	//add more decimal places for the values of each card used in the 5 card hand selected, not just the high card
	public static double evaluateHand(ArrayList<Card> hand) { //returns double value of hand 9.14-1.2 (decimal is high card)
		double highCardValue = 0;
		
		if(checkStraight(hand)>0&&checkFlush(hand)>0) {
			if(checkStraight(hand)==1) {
				return 9.14;
			} else {
				return 9+(checkStraight(hand)/100);
			}			
		} else if(checkFourOfAKind(hand)>0) {
			if(checkFourOfAKind(hand)==1) {
				return 8.14;
			} else {
				return 8+(checkFourOfAKind(hand)/100);
			}			
		} else if(checkFullHouse(hand)>0) {
			return 7+(checkFullHouse(hand)/100);
		} else if(checkFlush(hand)>0) {
			return 6+(checkFlush(hand)/100);
		} else if(checkStraight(hand)>0) {
			return 5+(checkStraight(hand)/100);		
		} else if(checkThreeOfAKind(hand)>0) {
			if(checkThreeOfAKind(hand)==1) {
				return 4.14;
			} else {
				return 4+(checkThreeOfAKind(hand)/100);
			}			
		} else if(checkTwoPair(hand)>0) { 
			return 3+(checkTwoPair(hand)/100);
		} else if(checkPair(hand)>0) {
			if(checkPair(hand)==1) {
				return 2.14;
			} else {
				return 2+(checkPair(hand)/100);
			}
		} else {
			hand = sortNum(hand);
			for(int i=0;i<hand.size();i++) {
				if(highCardValue<hand.get(i).getNum()) {
					highCardValue = hand.get(i).getNum();
				}
				if(hand.get(i).isAce()) {
					highCardValue = 14;
					break;
				}
			}
			highCardValue /= 100;
			return 1+highCardValue;
		}
	}
	public static ArrayList<Card> sortSuit(ArrayList<Card> toSort) { //bubble sort of card suits
		Card temp;
		boolean swapped;
		
		//bubble sort pushes a single element to the end of the list, swapping if it finds a larger
		for(int i=0;i<toSort.size()-1;i++) {
			swapped = false;
			for(int ii=0;ii<toSort.size()-i-1;ii++) {
				
				//moves up the element if its larger than the one in front of it
				if(toSort.get(ii).getSuit()>toSort.get(ii+1).getSuit()) {
					temp = toSort.get(ii);
					toSort.set(ii, toSort.get(ii+1));
					toSort.set(ii+1,temp);
					swapped = true;
				}
				
			} //end of inner
			if(!swapped) {
				break;
			}
		} //end of outer
		
		return toSort;
	}
	public static ArrayList<Card> sortNum(ArrayList<Card> toSort) { //bubble sort of card num values (ace is at the start)
		//see previous for context, this one works the same but with suits
		Card temp;
		boolean swapped;
		for(int i=0;i<toSort.size()-1;i++) {
			swapped = false;
			for(int ii=0;ii<toSort.size()-i-1;ii++) {
				if(toSort.get(ii).getNum()>toSort.get(ii+1).getNum()) {
					temp = toSort.get(ii);
					toSort.set(ii, toSort.get(ii+1));
					toSort.set(ii+1,temp);
					swapped = true;
				}
			} //end of inner
			if(!swapped) {
				break;
			}
		}
		return toSort;
	}
	
	//check hand type methods
	//add extra decimal places for each following high card
	public static double checkFourOfAKind(ArrayList<Card> total) { //returns true if group of cards has 4 of a kind
		int sameCount = 0;
		total = sortNum(total);
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getNum()==total.get(i+1).getNum()) {
				sameCount++;
				if(sameCount==3) {
					return total.get(i).getNum();
				}
			} else {
				sameCount = 0;
			}
		}
		return 0;
	}
	public static double checkFullHouse(ArrayList<Card> total) { //returns true if group of cards has full house
		ArrayList<Card> pairCheckList = new ArrayList<Card>();
		int threeOfKindNum = 0;
		int sameCount = 0;		
		boolean hasThreeOfAKind = false;
		total = sortNum(total);	
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getNum()==total.get(i+1).getNum()) {
				sameCount++;
				if(sameCount==2) {
					for(int j=0;j<total.size();j++) {
						if(!((j==i-1)||(j==i)||(j==i+1))) {
							pairCheckList.add(total.get(j));
						} else {
							threeOfKindNum = total.get(j).getNum();
						}
					}
					hasThreeOfAKind = true;
					break;
				}
			} else {
				sameCount = 0;
			}
		}
		if(hasThreeOfAKind) {
			for(int i = 0;i<pairCheckList.size()-1;i++) {
				if(pairCheckList.get(i).getNum()==pairCheckList.get(i+1).getNum()) {
					if(threeOfKindNum>pairCheckList.get(i).getNum()) {
						return threeOfKindNum;
					} else if(threeOfKindNum==1) {
						return 14;
					} else {
						return pairCheckList.get(i).getNum();
					}
					
				} 
			}
		}
		return 0;
	}	
	//if player has a 6 or 7 card straight the hand value will be .01 or .02 off respectively
	public static double checkStraight(ArrayList<Card> total) { //returns true if group of cards has 5 card straight
		int rowCount = 0;
		total = sortNum(total);
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getNum()+1==total.get(i+1).getNum()) {
				rowCount++;
				if(rowCount==4) {
					if(total.get(i).getNum()==5) {
						return 14;
					} else {
						return total.get(i+1).getNum();
					}
				}
			} else if((total.get(i).getNum()==13) && (total.get(0).isAce()||total.get(1).isAce())) {
				rowCount++;
				if(rowCount==4) {
					return 14;
				}
			} else {
				rowCount = 0;
			}
		}
		return 0;
	}
	public static double checkFlush(ArrayList<Card> total) { //returns true if group of cards has 5 card flush
		ArrayList<Card> flushCards = new ArrayList<Card>();
		int highestNumValue=0;
		int suitCount = 0;
		int suitOfFlush;
		total = sortSuit(total);
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getSuit()==total.get(i+1).getSuit()) {
				suitCount++;
				if(suitCount==4) {
					suitOfFlush = total.get(i).getSuit();
					flushCards.addAll(total);
					flushCards = sortNum(flushCards);
					for(int j=0;j<flushCards.size();j++) {
						if(highestNumValue>flushCards.get(j).getNum()&&flushCards.get(j).getSuit()==suitOfFlush) {
							highestNumValue = flushCards.get(j).getNum();
						}
						if(flushCards.get(j).isAce()) {
							return 14;
						}
					}
					return highestNumValue;
				}
			} else {
				suitCount = 0;
			}
		}
		return 0;
	}
	public static double checkThreeOfAKind(ArrayList<Card> total) { //returns true if group of cards has 3 of a kind
		int sameCount = 0;
		total = sortNum(total);
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getNum()==total.get(i+1).getNum()) {
				sameCount++;
				if(sameCount==2) {
					return total.get(i).getNum();
				}
			} else {
				sameCount = 0;
			}
		}
		return 0;
	}
	public static double checkTwoPair(ArrayList<Card> total) { //returns true if group of cards has 2 pair
		ArrayList<Card> checkSecondPair = new ArrayList<Card>();
		int firstPairNum = 0;
		boolean hasPair = false;
		total = sortNum(total);		
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getNum()==total.get(i+1).getNum()) {
				for(int j=0;j<total.size();j++) {
					if(!((j==i)||(j==i+1))) {
						checkSecondPair.add(total.get(j));
					} else {
						firstPairNum = total.get(j).getNum();
					}
				}
				hasPair = true;
				break;
			}
		}	
		if(hasPair) {
			for(int i = 0;i<checkSecondPair.size()-1;i++) {
				if(checkSecondPair.get(i).getNum()==checkSecondPair.get(i+1).getNum()) {
					if(firstPairNum>checkSecondPair.get(i).getNum()) {
						return firstPairNum;
					} else if(firstPairNum==1||checkSecondPair.get(i).isAce()) {
						return 14;
					} else {
						return checkSecondPair.get(i).getNum();
					}
					
				}
			}
		}
		return 0;
	}
	public static double checkPair(ArrayList<Card> total) { //returns true if group of cards has a pair
		total = sortNum(total);
		for(int i = 0;i<total.size()-1;i++) {
			if(total.get(i).getNum()==total.get(i+1).getNum()) {
				return total.get(i).getNum();
			}
		}
		return 0;
	}
} //end of class
