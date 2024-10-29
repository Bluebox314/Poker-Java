package poker;


public class Card { //a largely empty class, we use it as a data structure thats it
	private final int cardID; //input parameter that derives the cards values
	private final int num; //card number value (face cards are 11-13 and ace is 1)
	private final int suit; //int 1-4 for each suit (order is: Clubs, Diamonds, Hearts, Spades)
	private final boolean isAce; //stores if the card is an ace (used when ace is considered as a higher card than king)
	
	Card() { //constructor for blank cards (for use in testing or avoiding not initialized errors)
		cardID = 0;
		num = 0;
		suit = 0;
		isAce = false;
	}
	Card(int cardValue) { //all you need to make a card is the id, the rest is derived
		cardID = cardValue;
		if(cardID<=13) {
			suit = 1;
		} else if((13<cardID) && (cardID<=26)) {
			suit = 2;
		} else if((26<cardID) && (cardID<=39)) {
			suit = 3;
		} else {
			suit = 4;
		}
		if(suit!=1) {
			num = cardID-(13*(suit-1));
		} else {
			num = cardID;
		}
		if(num==1) {
			isAce = true;
		} else {
			isAce = false;
		}
	}
	
	//start of default methods
	public int getCardID() {
		return cardID;
	}
	public int getNum() {
		return num;
	}
	public int getSuit() {
		return suit;
	}	
	public boolean isAce() {
		return isAce;
	}
	//end of default methods
	
	public String displayCard() { //lengthy method that returns full name of card (in place of toString)
		if(suit==1) {
			if(num==1) { //all face cards have hard coded answers
				return "Ace of Clubs";
			} else if(num==11) {
				return "Jack of Clubs";
			} else if(num==12) {
				return "Queen of Clubs";
			} else if(num==13) {
				return "King of Clubs";
			} else {
				return num+" of Clubs";
			}
		} else if(suit==2) {
			if(num==1) {
				return "Ace of Diamonds";
			} else if(num==11) {
				return "Jack of Diamonds";
			} else if(num==12) {
				return "Queen of Diamonds";
			} else if(num==13) {
				return "King of Diamonds";
			} else {
				return num+" of Diamonds";
			}
		} else if(suit==3) {
			if(num==1) {
				return "Ace of Hearts";
			} else if(num==11) {
				return "Jack of Hearts";
			} else if(num==12) {
				return "Queen of Hearts";
			} else if(num==13) {
				return "King of Hearts";
			} else {
				return num+" of Hearts";
			}
		} else if(suit==4) {
			if(num==1) {
				return "Ace of Spades";
			} else if(num==11) {
				return "Jack of Spades";
			} else if(num==12) {
				return "Queen of Spades";
			} else if(num==13) {
				return "King of Spades";
			} else {
				return num+" of Spades";
			}
		} else {
			return "Empty Card";
		}	
	}
}
