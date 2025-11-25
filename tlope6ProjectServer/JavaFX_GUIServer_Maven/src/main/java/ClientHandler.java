//socket : Socket
//int : ObjectInputStream
//out : ObjectOutPutStream
//engine : GameEngine
//run() : void
// handleRequest(PokerInfo)
//sendResponse(PokerInfo)

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private GameEngine engine;
    private Consumer<String> callback;

    public ClientHandler(Socket socket, Consumer<String> callback) {
        this.socket = socket;
        this.callback = callback;
        this.engine = new GameEngine();
    }

    @Override
    public void run() {

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            callback.accept("Client connected");


            //main server loop

            while (true) {
                PokerInfo request = (PokerInfo) in.readObject();
                PokerInfo response = handleRequest(request);
                sendResponse(response);
            }

        } catch (Exception e) {
            callback.accept("Client disconnected");
        }
    }


    //handle request logic

    private PokerInfo handleRequest(PokerInfo req) {
        switch(req.getType()) {

            case DEAL :
                //start a new hand
                engine.startHand(req.getAnte(), req.getPairPlus());
                return buildDealResponse(req);
            case PLAY :
                //player wants to continue
                engine.play();
                return buildPlayResponse();
            case FOLD :
                //player wants to fold
                engine.fold();
                return buildFoldResponse();
            case NEW_GAME :
                engine.reset();
                PokerInfo info = new PokerInfo(MessageType.NEW_GAME_READY);
                info.setInfo("New game ready.");
                return info;

            default :
                PokerInfo err = new PokerInfo(MessageType.ERROR);
                err.setInfo("Invalid request from client");
                return err;
        }
    }

    //response functions that go from clienthandler to pokerinfo
    private PokerInfo buildDealResponse(PokerInfo req) {
        PokerInfo info = new PokerInfo(MessageType.DEAL);

        info.setPlayerCards(engine.getPlayerCards());
        info.setDealerCards(new ArrayList<>());
        info.setAnte(req.getAnte());
        info.setPairPlus(req.getPairPlus());
        info.setInfo("Cards dealt. Choose PLAY or FOLD");

        return info;
    }

    private PokerInfo buildPlayResponse() {
        PokerInfo info = new PokerInfo(MessageType.RESULT);

        info.setPlayerCards(engine.getPlayerCards());
        info.setDealerCards(engine.getDealerCards());
        info.setTotalWinnings(engine.getTotalWinnings());
        info.setInfo("Result calculated");

        return info;
    }

    private PokerInfo buildFoldResponse() {
        PokerInfo info = new PokerInfo(MessageType.RESULT);

        info.setPlayerCards(engine.getPlayerCards());
        info.setDealerCards(engine.getDealerCards());
        info.setTotalWinnings(engine.getTotalWinnings());
        info.setInfo("Player folded");

        return info;
    }


    //sending message from pokerserver
    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (Exception e) {}
    }


    //send response back to client
    private void sendResponse(PokerInfo info) {
        try {
            out.writeObject(info);
            out.flush();
        } catch (IOException e) {
            callback.accept("Failed to send response to client");
        }
    }
    public void close() throws IOException {
        socket.close();
    }
}