package PJ4;

import java.util.*;


//=================================================================================
/** class PlayingCardException: It is used for errors related to Card and Deck objects
 *  Do not modify this class!
 */
class PlayingCardException extends Exception {

    /* Constructor to create a PlayingCardException object */
    PlayingCardException (){
		super ();
    }

    PlayingCardException ( String reason ){
		super ( reason );
    }
}



//=================================================================================
/** class Card : for creating playing card objects
 *  it is an immutable class.
 *  Rank - valid values are 1 to 13
 *  Suit - valid values are 0 to 3
 *  Do not modify this class!
 */
class Card {
	
    /* constant suits and ranks */
    static final String[] Suit = {"Clubs", "Diamonds", "Hearts", "Spades" };
    static final String[] Rank = {"","A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    /* Data field of a card: rank and suit */
    private int cardRank;  /* values: 1-13 (see Rank[] above) */
    private int cardSuit;  /* values: 0-3  (see Suit[] above) */

    /* Constructor to create a card */
    /* throw PlayingCardException if rank or suit is invalid */
    public Card(int suit, int rank) throws PlayingCardException { 
	if ((rank < 1) || (rank > 13))
		throw new PlayingCardException("Invalid rank:"+rank);
	else
        	cardRank = rank;
	if ((suit < 0) || (suit > 3))
		throw new PlayingCardException("Invalid suit:"+suit);
	else
        	cardSuit = suit;
    }

    /* Accessor and toString */
    /* You may impelemnt equals(), but it will not be used */
    public int getRank() { return cardRank; }
    public int getSuit() { return cardSuit; }
    public String toString() { return Rank[cardRank] + " " + Suit[cardSuit]; }

    
    /* Few quick tests here */
    public static void main(String args[])
    {
	try {
	    Card c1 = new Card(3,1);    // A Spades
	    System.out.println(c1);
	    c1 = new Card(0,10);	// 10 Clubs
	    System.out.println(c1);
	    c1 = new Card(5,10);        // generate exception here
	}
	catch (PlayingCardException e)
	{
	    System.out.println("PlayingCardException: "+e.getMessage());
	}
    }
}



//=================================================================================
/** class Decks represents : n decks of 52 playing cards
 *  Use class Card to construct n * 52 playing cards!
 *
 *  Do not add new data fields!
 *  Do not modify any methods
 *  You may add private methods 
 */

class Decks {

    /* this is used to keep track of original n*52 cards */
    private List<Card> originalDecks;   

    /* this starts with copying cards from originalDecks */
    /* it is used to play the card game                  */
    /* see reset(): resets gameDecks to originalDecks    */
    private List<Card> gameDecks;

    /* number of decks in this object */
    private int numberDecks;


    /**
     * Constructor: Creates default one deck of 52 playing cards in originalDecks and
     * 		    copy them to gameDecks.
     *              initialize numberDecks=1
     * Note: You need to catch PlayingCardException from Card constructor
     *	     Use ArrayList for both originalDecks & gameDecks
     */
    public Decks()
    {
        numberDecks = 1;
        originalDecks = new ArrayList<>();
        for(int s = 0; s < 4;s++){
            for(int r = 1; r <= 13; r++){
                try{
                Card card = new Card(s, r);
                originalDecks.add(card);
                }catch(PlayingCardException e){
                 System.out.println(e.getMessage());
                }
            }
        }
        gameDecks=new ArrayList<>(originalDecks);

        // implement this method!
    }


    /**
     * Constructor: Creates n decks (52 cards each deck) of playing cards in
     *              originalDecks and copy them to gameDecks.
     *              initialize numberDecks=n
     * Note: You need to catch PlayingCardException from Card constructor
     *	     Use ArrayList for both originalDecks & gameDecks
     */
    public Decks(int n)
    {
        numberDecks = n;
        originalDecks = new ArrayList<>();
        for(int i = 0; i < n ;i++){//creates n decks of playing cards
         for(int s = 0; s < 4;s++){
            for(int r = 1; r <= 13; r++){
                try{
                Card card = new Card(s, r);
                originalDecks.add(card);
                }catch(PlayingCardException e){
                 System.out.println(e.getMessage());
                 }
            }
          }

        } 
        gameDecks = new ArrayList<>(originalDecks);

        // implement this method!
    
    }


    /**
     * Task: Shuffles cards in gameDecks.
     * Hint: Look at java.util.Collections
     */
    public void shuffle()
    {
        java.util.Collections.shuffle(gameDecks);
        // implement this method!
    }

    /**
     * Task: Deals cards from the gameDecks.
     *
     * @param numberCards number of cards to deal
     * @return a list containing cards that were dealt
     * @throw PlayingCardException if numberCard > number of remaining cards
     *
     * Note: You need to create ArrayList to stored dealt cards
     *       and should removed dealt cards from gameDecks
     *
     */
    public List<Card> deal(int numberCards) throws PlayingCardException
    {
        if (numberCards > gameDecks.size()){
            throw new PlayingCardException("Not enough cards to deal!");
        }
        
        List<Card> cardsDealt = new ArrayList<>();
        
        for(int i = 0; i<numberCards; i++){
            cardsDealt.add(gameDecks.remove(0));
        }
        // implement this method!
        return cardsDealt;
    }

    /**
     * Task: Resets gameDecks by getting all cards from the originalDecks.
     */
    public void reset()
    {
        gameDecks = originalDecks;
        // implement this method!
    }

    /**
     * Task: Return number of remaining cards in gameDecks.
     */
    public int remainSize()
    {
	return gameDecks.size();
    }

    /**
     * Task: Returns a string representing cards in the gameDecks 
     */
    public String toString()
    {
	return ""+gameDecks;
    }


    /* Quick test                   */
    /*                              */
    /* Do not modify these tests    */
    /* Generate 2 decks of cards    */
    /* Loop 2 times:                */
    /*   Deal 30 cards for 4 times  */
    /*   Expect exception last time */
    /*   reset()                    */

    public static void main(String args[]) {

        System.out.println("*******    Create 2 decks of cards      *********\n\n");
        Decks decks  = new Decks(2);
         
	for (int j=0; j < 2; j++)
	{
        	System.out.println("\n************************************************\n");
        	System.out.println("Loop # " + j + "\n");
		System.out.println("Before shuffle:"+decks.remainSize()+" cards");
		System.out.println("\n\t"+decks);
        	System.out.println("\n==============================================\n");

                int numHands = 4;
                int cardsPerHand = 30;

        	for (int i=0; i < numHands; i++)
	 	{
	    		decks.shuffle();
		        System.out.println("After shuffle:"+decks.remainSize()+" cards");
		        System.out.println("\n\t"+decks);
			try {
            		    System.out.println("\n\nHand "+i+":"+cardsPerHand+" cards");
            		    System.out.println("\n\t"+decks.deal(cardsPerHand));
            		    System.out.println("\n\nRemain:"+decks.remainSize()+" cards");
		            System.out.println("\n\t"+decks);
        	            System.out.println("\n==============================================\n");
			}
			catch (PlayingCardException e) 
			{
		 	        System.out.println("*** In catch block:PlayingCardException:Error Msg: "+e.getMessage());
			}
		}


		decks.reset();
	}
    }

}
