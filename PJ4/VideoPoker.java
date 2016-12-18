package PJ4;

import com.sun.javafx.scene.EnteredExitedHandler;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import java.util.*;
import javax.sound.sampled.FloatControl;

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
    private static final int startingBalance = 1000;
    private static final int numberOfCards = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers = {1, 2, 3, 5, 6, 9, 25, 50, 250};//0,1,2,3,4,5,6,7,8
    private static final String[] goodHandTypes = {
        "Royal Pair", "Two Pair", "Three of a Kind", "Straight", "Flush	",
        "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};//0 = rp, 1= tp, 2= tok, 3=s,4=f,5=fh,6=fourofkind,7=sf, 8= rf

    // must use only one deck
    private final Decks oneDeck;

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private int playerBalance;
    private int playerBet;

    Scanner in = new Scanner(System.in);

    /**
     * default constructor, set balance = startingBalance
     */
    public VideoPoker() {
        this(startingBalance);
        playerBet = 0;
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
        List<Integer> sortedHand = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {//out of index error??
            sortedHand.add(playerHand.get(i).getRank());
        }
        Collections.sort(sortedHand);

        if (royalFlush(sortedHand)) {
            System.out.println(goodHandTypes[8]);
            playerBalance += (playerBet * multipliers[8]);
        } else if (straightFlush(sortedHand)) {
            System.out.println(goodHandTypes[7]);
            playerBalance += (playerBet * multipliers[7]);
        } else if (fourKind(sortedHand)) {
            System.out.println(goodHandTypes[6]);
            playerBalance += (playerBet * multipliers[6]);
        } else if (fullhouse(sortedHand)) {
            System.out.println(goodHandTypes[5]);
            playerBalance += (playerBet * multipliers[5]);
        } else if (flush()) {
            System.out.println(goodHandTypes[4]);
            playerBalance += (playerBet * multipliers[4]);
        } else if (straight(sortedHand)) {
            System.out.println(goodHandTypes[3]);
            playerBalance += (playerBet * multipliers[3]);
        } else if (threeKind(sortedHand)) {
            System.out.println(goodHandTypes[2]);
            playerBalance += (playerBet * multipliers[2]);
        } else if (twoPair(sortedHand)) {
            System.out.println(goodHandTypes[1]);
            playerBalance += (playerBet * multipliers[1]);
        } else if (royalPair()) {
            System.out.println(goodHandTypes[0]);
            playerBalance += (playerBet * multipliers[0]);
        } else {
            System.out.println("Sorry, you lost!");
        }
        // implement this method!
    }

    /**
     * ***********************************************
     * add additional private methods here ....
     *
     ************************************************
     */
    private boolean royalPair() {
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

    private boolean twoPair(List<Integer> g) {
        int count = 0;//counts how many pairs there are
        for (int i = 0; i < g.size() - 1; i++) {
            if (g.get(i) == g.get(i + 1)) {
                count++;
                i += 2;
            }
        }
        return count == 2;
    }

    private boolean threeKind(List<Integer> a) {
        int count = 0;
        for (int i = 0; i < playerHand.size() - 1; i++) {
            if (a.get(i) != a.get(i + 1)) {
                break;
            } else {
                count++;
            }
        }
        return count == 2;
    }

    private boolean straight(List<Integer> b) {
        int count = 0;
        for (int i = 0; i < b.size() - 1; i++) {
            if (b.get(i) + 1 == b.get(i + 1)) {
                count++;
            }
        }
        return count == 4;
    }

    private boolean flush() {
        int count = 0;
        for (int i = 0; i < playerHand.size() - 1; i++) {
            if (playerHand.get(i).getSuit() == playerHand.get(i + 1).getSuit()) {
                count++;
            }
        }
        return count == 4;
    }

    private boolean fullhouse(List<Integer> c) {
        int count = 0;
        for (int i = 3; i < c.size() - 1; i++) {
            if (threeKind(c) && c.get(i) == c.get(i + 1)) {
                count++;
            }
        }
        return count == 1;
    }

    private boolean fourKind(List<Integer> d) {
        int count = 0;
        for (int i = 0; i < playerHand.size() - 1; i++) {
            if (d.get(i) != d.get(i + 1)) {
                break;
            } else {
                count++;
            }
        }
        return count == 3;
    }

    private boolean straightFlush(List<Integer> e) {
        return straight(e) && flush();
    }

    private boolean royalFlush(List<Integer> f) {
        int count = 0;
        if (f.get(0) == 1) {
            count++;
            for (int i = 1; i < playerHand.size() - 1; i++) {
                if (f.get(i) + 1 == f.get(i + 1)) {
                    count++;
                }
            }
        }
        return count == 4;
    }

    private void replaceHand() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter positions of cards to keep (e.g. 1 4 5 ): ");
        List<Card> keptCards = new ArrayList<>();
        List<Card> newCards = new ArrayList<>();
        String tokens = input.nextLine();
        Scanner inChar = new Scanner(tokens).useDelimiter("\\s*");

        while (inChar.hasNextInt()) {
            int position = inChar.nextInt();
            if (position >= 1) {
                keptCards.add(playerHand.get(position - 1));
            } else {
                break;
            }
        }

        for (int count = 0; count < playerHand.size() - keptCards.size(); count++) {
            try {
                newCards = oneDeck.deal(count + 1);
            } catch (PlayingCardException e) {
                e.getMessage();
            }
        }
        keptCards.addAll(newCards);
        playerHand = keptCards;
        System.out.println(playerHand);
    }

    public void play() {
        /**
         * The main algorithm for single player poker game
         *
         * Steps: showPayoutTable()
         *
         * ++ show balance, get bet verify bet value, update balance reset deck,
         * shuffle deck, deal cards and display cards, ask for positions of
         * cards to keep get positions in one input line update cards check
         * hands, display proper messages update balance if there is a payout if
         * balance = O: end of program else ask if the player wants to play a
         * new game if the answer is "no" : end of program else :
         * showPayoutTable() if user wants to see it go to ++
         */
        showPayoutTable();
        boolean continueToPlay = true;
        while (continueToPlay) {
            System.out.println("-----------------------------------");
            System.out.println("Balance: $" + playerBalance);
            boolean placeBet = true;
            //place bet
            while (placeBet) {
                System.out.print("Enter bet: $");
                playerBet += in.nextInt();            
                if (playerBalance >= playerBet && playerBet >= 0) {
                    playerBalance -= playerBet;
                    placeBet = false;
                } else {//when player bet is greater than balance
                    System.out.println("You don't have enough money. Try a different bet.");
                    playerBet -= playerBet;
                }
            }
            //reset deck and shuffle
            oneDeck.reset();
            oneDeck.shuffle();

            //deal new player hand
            playerHand = new ArrayList<>();
            try {
                playerHand = oneDeck.deal(numberOfCards);
            } catch (PlayingCardException e) {
                e.getMessage();
            }
            //print out current hand
            System.out.println(playerHand);
            //ask for positions to keep and update card hand
            replaceHand();
            //check hands
            checkHands();
            
            System.out.println("Your updated balance is: $" + playerBalance);

            //end game
            if (playerBalance == 0) {
                System.out.println("Your balance is $0. Goodbye!");
                continueToPlay = false;
                //otherwise ask if want to play new game
            } else {
                //show remaining balance
                System.out.println("\n\nYour balance: $" + playerBalance + ", one more game (y or n)");
                String yorn = in.next();
                if (yorn.equals("n")) {
                    continueToPlay = false;
                } else if (yorn.equals("y")) {
                    System.out.println("\n\nWant to see payout table? (y or n)");
                    String p = in.next();
                    if (p.equals("y")) {
                        showPayoutTable();
                    }
                } else {
                    System.out.println("Wrong input. Ending game now.");
                    continueToPlay = false;
                }
            }

        }

    }
    // implement this method!

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
