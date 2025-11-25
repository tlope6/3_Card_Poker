//deck : deck
//totalWinnings : int
//handNumber : int
//startHand(int, int): void
// play() : void
//fold() : void
// reset() : void

import java.util.ArrayList;

public class GameEngine{
    private Deck deck;
    private int totalWinnings;
    private int handNumber;

    private ArrayList<Card> playerCards;
    private ArrayList<Card> dealerCards;

    private int ante;
    private int pairPlus;

    public GameEngine() {
        deck = new Deck();
        totalWinnings = 0;
        handNumber = 0;
    }


    //start a new hand
    public void startHand(int ante, int pairPlus) {
        this.ante = ante;
        this.pairPlus = pairPlus;

        deck.shuffle();
        playerCards = deck.dealHand();
        dealerCards = deck.dealHand();

        handNumber++;
    }

    //player chooses play
    public void play() {
        int roundWin = 0;

        boolean qualifies = ThreeCardLogic.dealerQualifies(dealerCards);
        int compare = ThreeCardLogic.compareHands(dealerCards, playerCards);

        //pair plus always evaluated
        roundWin += ThreeCardLogic.evalPPWinnings(playerCards, pairPlus);

        if (!qualifies) {
            //dealer doesn't qualify ante returned
            roundWin += ante;
        } else {

            //dealer qualifies so normal comparison
            if (compare == 1) {

                //playwer wins ante + play bet (1:1)
                roundWin += ante * 2;
            } else if( compare == -1) {
                //player loses both ante + pairPlus already considered
                roundWin -= ante;
            }
        }

        totalWinnings += roundWin;
    }


    //player chooses to fold
    public void fold() {
        //lost ante + pairPlus
        int lost = -(ante + pairPlus);
        totalWinnings += lost;
    }


    //reset between the games
    public void reset() {
        playerCards = null;
        dealerCards = null;
        ante = 0;
        pairPlus = 0;
    }


    //assistance for ClientHandler to build PokerInfo
    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    public ArrayList<Card> getDealerCards() {
        return dealerCards;
    }

    public int getTotalWinnings() {
        return totalWinnings;
    }

    public int getAnte() {
        return ante;
    }

    public int getPairPlus() {
        return pairPlus;
    }

    public int getHandNumber() {
        return handNumber;
    }
}
