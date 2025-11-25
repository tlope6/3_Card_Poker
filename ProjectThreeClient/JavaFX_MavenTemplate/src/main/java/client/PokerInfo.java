package client;

//shared as the server


//type : MessageType
//playerCards: List<Card>
//dealerCards : List<Card>
//ante int
// pairPlus int
//totalWinnings
//getType() : messageType
//setInfo(String) void

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


class PokerInfo implements Serializable{

    private MessageType type;
    private List<Card> playerCards;
    private List<Card> dealerCards;
    private int ante;
    private int pairPlus;
    private int totalWinnings;
    private String infoMessage;//used for setInfo()


    //constructors
    public PokerInfo() {
        this.type = MessageType.NONE;
        this.playerCards = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
    }

    public PokerInfo(MessageType type) {
        this.type = type;
        this.playerCards = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
    }

    //getter/setters
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }

    public void setDealerCards(List<Card> dealerCards) {
        this.dealerCards = dealerCards;
    }

    public int getAnte() {
        return ante;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public int getPairPlus() {
        return pairPlus;
    }

    public void setPairPlus(int pairPlus) {
        this.pairPlus = pairPlus;
    }

    public int getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(int totalWinnings) {
        this.totalWinnings = totalWinnings;
    }

    public void setInfo(String infoMessage) {
        this.infoMessage = infoMessage;
    }

    public String getInfo() {
        return infoMessage;
    }


}