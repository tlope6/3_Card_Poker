//evalHand(List<Card>) int
//compareHands(): int
//evalPairHands(): int
//dealerQualifes() : bool

import java.util.ArrayList;
import java.util.Collections;

public class ThreeCardLogic {


    //hand ranking values
    public static final  int HIGH_CARD = 0;
    public static final int PAIR = 1;
    public static final int FLUSH = 2;
    public static final int STRAIGHT = 3;
    public static final int THREE_OF_A_KIND = 4;
    public static final int STRAIGHT_FLUSH = 5;


    //evalHand() - return ranking of a 3-card hand

    public static int evalHand(ArrayList<Card> hand) {
        Collections.sort(hand, (a,b) -> a.getRank() - b.getRank());

        boolean flush = isFlush(hand);
        boolean straight = isStraight(hand);
        boolean three = isThree(hand);
        boolean pair = isPair(hand);

        if (straight && flush) {
            return STRAIGHT_FLUSH;
        }
        if (three) {
            return THREE_OF_A_KIND;
        }
        if (straight) {
            return STRAIGHT;
        }
        if (flush) {
            return FLUSH;
        }
        if(pair) {
            return PAIR;
        }

        return HIGH_CARD;
    }

    //pair plush evaluations
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        int rank = evalHand(hand);

        switch(rank) {
            case STRAIGHT_FLUSH:
                return bet * 40;
            case THREE_OF_A_KIND:
                return bet * 30;
            case STRAIGHT:
                return bet * 6;
            case FLUSH:
                return bet * 3;
            case PAIR:
                return bet * 1;
            default:
                return 0;
        }

    }

    //compareHands - returns
    // 1 if player wins
    // -1 if dealer wins
    // 0 if tie
    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerRank = evalHand(dealer);
        int playerRank = evalHand(player);


        //if hand types different, higher ranking wins
        return compareHighCards(dealer, player);
    }


    //helper : dealer must qualify with Queen high or better

    public static boolean dealerQualifies(ArrayList<Card> dealer) {
        Collections.sort(dealer, (a,b) -> a.getRank() - b.getRank());
        return dealer.get(2).getRank() >= 12; // Q = 12
    }


    //helper functions

    private static boolean isFlush(ArrayList<Card> hand) {
        return hand.get(0).getSuit() == hand.get(1).getSuit() &&
                hand.get(1).getSuit() == hand.get(2).getSuit();
    }

    private static boolean isStraight(ArrayList<Card> hand) {
        Collections.sort(hand, (a,b) -> a.getRank() - b.getRank());
        int r1 = hand.get(0).getRank();
        int r2 = hand.get(1).getRank();
        int r3 = hand.get(2).getRank();

        return (r2 == r1 + 1) && (r3 == r2 + 1);
    }

    private static boolean isPair(ArrayList<Card> hand) {
        return hand.get(0).getRank() == hand.get(1).getRank() ||
                hand.get(1).getRank() == hand.get(2).getRank() ||
                hand.get(0).getRank() == hand.get(2).getRank();
    }

    private static boolean isThree(ArrayList<Card> hand) {
        return hand.get(0).getRank() == hand.get(1).getRank() &&
                hand.get(1).getRank() == hand.get(2).getRank();
    }

    private static int compareHighCards(ArrayList<Card> dealer, ArrayList<Card> player) {
        Collections.sort(dealer, (a,b) -> a.getRank() - b.getRank());
        Collections.sort(player, (a,b) -> a.getRank() - b.getRank());

        for (int i = 2; i >= 0; i--) {
            int p = player.get(i).getRank();
            int d = player.get(i).getRank();

            if (p > d) return 1; //player wins
            if (d > p) return -1; //dealer wins
        }

        return 0; //tie
    }






}