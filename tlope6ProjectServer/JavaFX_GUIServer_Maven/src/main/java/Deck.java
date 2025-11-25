//card : List<Card>
//deal(n:int) : void
//shuffle() : void


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck {

    private List<Card> cards;
    private int index;

    public Deck(){
        cards = new ArrayList<Card>();
        reset();
    }


    //create a full 52-deck

    public void reset(){
        cards.clear();

        for (int suit = 0; suit < 4; suit++) {
            for (int rank = 2; rank <= 14; rank++) { //11 = J, 12, = Q, 13 = K, 14 = Ace
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
        index = 0;
    }

    //shuffle the deck
    public void shuffle(){
        Collections.shuffle(cards);
    }

    //deal one card
    public Card deal() {
        if (index >= cards.size()) {
            reset();
        }

        return cards.get(index++);
    }

    //deal 3-card hand

    public ArrayList<Card> dealHand() {
        ArrayList<Card> hand = new ArrayList<>();
        hand.add(deal());
        hand.add(deal());
        hand.add(deal());
        return hand;

    }

}