import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class serverTest {
    //testing the server starting
    @Test
    public void testServerStartsSuccessfully() {
        PokerServer server = new PokerServer(5555);
        assertDoesNotThrow(() -> server.startServer());
    }

    //server rejects invalid ports
    @Test
    public void testInvalidPortLow() {
        assertThrows(IllegalArgumentException.class,
                () -> new PokerServer(100));  // too low
    }

    @Test
    public void testInvalidPortHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> new PokerServer(99999));  // too high
    }

    //server accepts 2 clients only
    @Test
    public void testServerAcceptsOnlyTwoClients() throws Exception {
        PokerServer server = new PokerServer(6000);
        new Thread(server::startServer).start();

        Socket c1 = new Socket("localhost", 6000);
        Socket c2 = new Socket("localhost", 6000);

        // 3rd client should be refused (throw or disconnect)
        assertThrows(IOException.class,
                () -> new Socket("localhost", 6000));

        c1.close();
        c2.close();
    }
    //deak nessage requires both bets
    @Test
    public void testDealRequiresBets() throws Exception {
        ClientHandler handler = new ClientHandler(mock(Socket.class), 1);

        PokerInfo badDeal = new PokerInfo(MessageType.DEAL);
        badDeal.setAnte(0);
        badDeal.setPairPlus(0);

        PokerInfo response = handler.processMessage(badDeal);

        assertEquals(MessageType.ERROR, response.getType());
        assertTrue(response.getMessage().contains("Bets required"));
    }

    //deal generates 3 cards for each player

    @Test
    public void testDealGeneratesCards() throws Exception {
        PokerServer server = new PokerServer(6001);

        ClientHandler p1 = new ClientHandler(mock(Socket.class), 1);
        ClientHandler p2 = new ClientHandler(mock(Socket.class), 2);

        server.players[0] = p1;
        server.players[1] = p2;

        PokerInfo deal = new PokerInfo(MessageType.DEAL);
        deal.setAnte(10);
        deal.setPairPlus(10);

        server.processDeal();

        assertEquals(3, p1.getCards().size());
        assertEquals(3, p2.getCards().size());
    }

    //card are unique and no duplicates
    @Test
    public void testCardsAreUnique() {
        Deck deck = new Deck();
        deck.shuffle();

        List<Card> dealt = new ArrayList<>();

        for (int i = 0; i < 20; i++) {  // draw 20 cards
            Card c = deck.draw();
            assertFalse(dealt.contains(c), "Duplicate card found!");
            dealt.add(c);
        }
    }


    //seeing attempt of pressing play before deal which should give an error
    @Test
    public void testPlayBeforeDeal() throws Exception {
        ClientHandler player = new ClientHandler(mock(Socket.class), 1);

        PokerInfo playMsg = new PokerInfo(MessageType.PLAY);
        PokerInfo result = player.processMessage(playMsg);

        assertEquals(MessageType.ERROR, result.getType());
        assertTrue(result.getMessage().contains("DEAL first"));
    }


    //fold before deal = error
    @Test
    public void testFoldBeforeDeal() throws Exception {
        ClientHandler player = new ClientHandler(mock(Socket.class), 1);

        PokerInfo foldMsg = new PokerInfo(MessageType.FOLD);
        PokerInfo result = player.processMessage(foldMsg);

        assertEquals(MessageType.ERROR, result.getType());
    }


    //evualting the game (in this case if dealer does not qualify
    @Test
    public void testDealerDoesNotQualify() {
        ThreeCardLogic logic = new ThreeCardLogic();

        List<Card> dealer = List.of(
                new Card(11, 0),  // J (does NOT qualify)
                new Card(3, 1),
                new Card(5, 2)
        );

        boolean qualifies = logic.dealerQualifies(dealer);

        assertFalse(qualifies);
    }

    //straight flush in game
    @Test
    public void testStraightFlushBeatsAll() {
        ThreeCardLogic logic = new ThreeCardLogic();

        List<Card> sf = List.of(
                new Card(10, 2),
                new Card(11, 2),
                new Card(12, 2)
        );

        List<Card> pair = List.of(
                new Card(7, 1),
                new Card(7, 3),
                new Card(2, 0)
        );

        int winner = logic.compareHands(sf, pair);

        assertEquals(1, winner); // player wins
    }
    //server doesnt crash on bad input
    @Test
    public void testMalformedInput() throws Exception {
        ClientHandler handler = new ClientHandler(mock(Socket.class), 1);

        PokerInfo bad = null;

        assertDoesNotThrow(() -> handler.processMessage(bad));
    }
    //restting the gaem between rounds
    @Test
    public void testResetBetweenRounds() {
        PokerServer server = new PokerServer(6002);

        server.resetGame();

        assertEquals(0, server.currentBets[0]);
        assertEquals(0, server.currentBets[1]);
        assertNull(server.players[0].getCards());
        assertNull(server.players[1].getCards());
    }
    //server sends round results to clients
    @Test
    public void testServerSendsResults() throws Exception {
        PokerServer server = new PokerServer(6003);

        ClientHandler p1 = mock(ClientHandler.class);
        ClientHandler p2 = mock(ClientHandler.class);

        server.players[0] = p1;
        server.players[1] = p2;

        server.sendResultsToPlayers("TEST RESULT");

        verify(p1, times(1)).sendMessage(any(PokerInfo.class));
        verify(p2, times(1)).sendMessage(any(PokerInfo.class));
    }




}
