//rank : String
// suit : String
//toString() : string


import java.io.Serializable;
public class Card implements Serializable {
	private static final long serialVersionUID = 1L;



    private int rank; //2-14 (14 = Ace)
    private int suit; // 0 = clubs, 1 = diamonds, 2 = hearts, 3 = spades



    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }


    public int getSuit() {
        return suit;
    }


    @Override
    public String toString() {
        return "Card(" + rank + ", " + suit + ")";
    }
}
