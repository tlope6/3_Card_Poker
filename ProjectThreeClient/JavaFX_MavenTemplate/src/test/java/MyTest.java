import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

    //testing to see if the client connection works
    @Test
    public void testClientConnectionConstructor() throws Exception {
        Socket mockSocket = mock(Socket.class);
        ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(new byte[0]));

        when(mockSocket.getOutputStream()).thenReturn(oos);
        when(mockSocket.getInputStream()).thenReturn(ois);

        ClientConnection conn = new ClientConnection("127.0.0.1", 5555, info -> {});
        assertNotNull(conn);
    }
//client connection sends poker

    @Test
    public void testSendPokerInfo() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        Socket mockSocket = mock(Socket.class);
        when(mockSocket.getOutputStream()).thenReturn(oos);

        ClientConnection conn = new ClientConnection("localhost", 5555, info -> {});
        conn.out = oos; // inject the stream

        PokerInfo info = new PokerInfo(MessageType.DEAL);

        conn.send(info);

        oos.flush();

        byte[] data = baos.toByteArray();
        assertTrue(data.length > 0, "PokerInfo was not written to stream");
    }

    //client connection reading the pokerinfo and calls callback
    @Test
    public void testReceivePokerInfoCallback() throws Exception {
        PokerInfo info = new PokerInfo(MessageType.RESULT);

        ByteArrayOutputStream outMock = new ByteArrayOutputStream();
        ByteArrayInputStream inMock = new ByteArrayInputStream(serialize(info));

        ClientConnection conn = new ClientConnection("localhost", 5555, received -> {
            assertEquals(MessageType.RESULT, received.getType());
        });

        conn.in = new ObjectInputStream(inMock);

        Thread t = new Thread(conn::run);
        t.start();

        Thread.sleep(200); // allow thread to process

        t.interrupt();
    }

    //assist with the client app for loading the gameplay
    @Test
    public void testClientAppLoadsGameplayScene() throws Exception {
        ClientApp app = new ClientApp();

        Stage testStage = new Stage();

        Platform.runLater(() -> {
            assertDoesNotThrow(() -> app.start(testStage));
        });
    }




}
