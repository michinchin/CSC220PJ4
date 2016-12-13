package PJ4;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import java.util.*;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.freeslots.com/poker.htm
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */
 /* This is the video poker game class.
 * It uses Decks and Card objects to implement video poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */
public class VideoPoker {

    // default constant values
    private static final int startingBalance = 100;
    private static final int numberOfCards = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers = {1, 2, 3, 5, 6, 9, 25, 50, 250};
    private static final String[] goodHandTypes = {
        "Royal Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush	",
        "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    /**
     * default constructor, set balance = startingBalance
     */
    public VideoPoker() {
        this(startingBalance);

    }

    /**
     * constructor, set given balance
     */
    public VideoPoker(int balance) {
        this.playerBalance = balance;
        oneDeck = new Decks(1);
    }

    /**
     * This display the payout table based on multipliers and goodHandTypes
     * arrays
     */
    private void showPayoutTable() {
        System.out.println("\n\n");
        System.out.println("Payout Table   	      Multiplier   ");
        System.out.println("=======================================");
        int size = multipliers.length;
        for (int i = size - 1; i >= 0; i--) {
            System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
        }
        System.out.println("\n\n");
    }

    /**
     * Check current playerHand using multipliers and goodHandTypes arrays Must
     * print yourHandType (default is "Sorry, you lost") at the end of function.
     * This can be checked by testCheckHands() and main() method.
     */
    private void checkHands() {
        
        // implement this method!
    }

    /**
     * ***********************************************
     * add additional private methods here ....
     *
     ************************************************
     */
    private boolean jacksObetter() {
        int jack = 0;
        int queen = 0;
        int king = 0;
        int ace = 0;

        for (int i = 0; i < playerHand.size(); i++) {
            switch (playerHand.get(i).getRank()) {
                case 1:
                    ace++;
                    break;
                case 11:
                    jack++;
                    break;
                case 12:
                    queen++;
                    break;
                case 13:
                    king++;
                    break;
                default:
                    break;
            }
        }
        return (ace == 2 || jack == 2 || queen == 2 || king == 2);
    }

    private boolean twoPair() {
        int count = 0;//counts how many pairs there are
        for (int i = playerHand.size(); i > 0; i--) {
            for (int k = playerHand.size() - 1; i > 0; i--) {
                int firstCard = playerHand.get(i).getRank();
                if (firstCard == playerHand.get(k).getRank()) {
                    count++;
                    i -= 2;
                } else if (firstCard == playerHand.get(k - 1).getRank()) {
                    count++;
                } else if (firstCard == playerHand.get(k - 2).getRank()) {
                    count++;
                } else if (firstCard == playerHand.get(k - 3).getRank()) {
                    count++;
                }
            }
        }

        return count == 2;
    }

    private boolean threeKind() {
        int count = 0;//counts how many matches there are
        Arrays.sort(playerHand.toArray());
        for (int i = playerHand.size(); i > 0; i--) {
            for (int k = playerHand.size() - 1; i > 0; i--) {
                int firstCard = playerHand.get(i).getRank();
                if (firstCard == playerHand.get(k).getRank() && playerHand.get(k).getRank() == playerHand.get(k - 1).getRank()) {//if first card is equal to second and third
                    count++;
                }
            }
        }
        return count == 1;

    }

    private boolean straight() {
        int count = 0;
        Arrays.sort(playerHand.toArray());
        for (int i = 0; i < playerHand.size(); i++) {
            if (playerHand.get(i).getRank() + 1 == playerHand.get(i + 1).getRank()) {
                count++;
            }
        }
        return count == 4;
    }

    private boolean flush() {
        int count = 0;
        for (int i = 0; i < playerHand.size(); i++) {
            if (playerHand.get(i).getSuit() == playerHand.get(i + 1).getSuit()) {
                count++;
            }
        }
        return count == 5;
    }

    private boolean fullhouse() {
        int count = 0;
        Arrays.sort(playerHand.toArray());
        for (int i = 2; i < playerHand.size(); i++) {
            if (threeKind() && playerHand.get(i).getRank() == playerHand.get(i + 1).getRank()) {
                count++;
            }
        }
        return count == 1;
    }

    private boolean fourKind() {
        int count = 0;
        Arrays.sort(playerHand.toArray());
        for (int i = playerHand.size(); i > 0; i--) {
            for (int k = playerHand.size() - 1; i > 0; i--) {
                int firstCard = playerHand.get(i).getRank();
                int secCard = playerHand.get(k).getRank();
                int thirdCard = playerHand.get(k - 1).getRank();
                int fourthCard = playerHand.get(k - 2).getRank();

                if (firstCard == secCard && secCard == thirdCard && thirdCard == fourthCard) {//if first card is equal to second and third and fourth
                    count++;
                }
            }
        }
        return count == 1;
    }

    private boolean straightFlush() {
        return straight() && flush();
    }

    private boolean royalFlush() {
        int count = 0;
        Arrays.sort(playerHand.toArray());
        if (playerHand.get(0).getRank() == 1) {
            for (int i = 1; i < playerHand.size(); i++) {
                if (playerHand.get(i).getRank() + 1 == playerHand.get(i + 1).getRank()) {
                count++;
                }
            }
        }
        return count == 4;
    }

    public void play() {
        /**
         * The main algorithm for single player poker game
         *
         * Steps: showPayoutTable()
         *
         * ++ show balance, get bet verify bet value, update balance reset deck,
         * shuffle deck, deal cards and display cards ask for positions of cards
         * to keep get positions in one input line update cards check hands,
         * display proper messages update balance if there is a payout if
         * balance = O: end of program else ask if the player wants to play a
         * new game if the answer is "no" : end of program else :
         * showPayoutTable() if user wants to see it goto ++
         */

        // implement this method!
    }

    /**
     * ***********************************************
     * Do not modify methods below
     * /*************************************************
     *
     * /** testCheckHands() is used to test checkHands() method checkHands()
     * should print your current hand type
     */
    public void testCheckHands() {
        try {
            playerHand = new ArrayList<Card>();

            // set Royal Flush
            playerHand.add(new Card(3, 1));
            playerHand.add(new Card(3, 10));
            playerHand.add(new Card(3, 12));
            playerHand.add(new Card(3, 11));
            playerHand.add(new Card(3, 13));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight Flush
            playerHand.set(0, new Card(3, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight
            playerHand.set(4, new Card(1, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Flush 
            playerHand.set(4, new Card(3, 5));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
            // "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };
            // set Four of a Kind
            playerHand.clear();
            playerHand.add(new Card(3, 8));
            playerHand.add(new Card(0, 8));
            playerHand.add(new Card(3, 12));
            playerHand.add(new Card(1, 8));
            playerHand.add(new Card(2, 8));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Three of a Kind
            playerHand.set(4, new Card(3, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Full House
            playerHand.set(2, new Card(1, 11));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Two Pairs
            playerHand.set(1, new Card(1, 9));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Royal Pair
            playerHand.set(0, new Card(1, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // non Royal Pair
            playerHand.set(2, new Card(3, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) {
        VideoPoker pokergame = new VideoPoker();
        pokergame.testCheckHands();
    }
}
