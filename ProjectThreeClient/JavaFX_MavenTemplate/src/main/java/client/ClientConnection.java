//socket socket
//in obejctinputstream
//out objectoutputstream
//connect(ip, port) void
//send(pokerinfo) void
//recieve() : PokerInfo
package client;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientConnection extends Thread {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private Consumer<PokerInfo> onReceive;

    // accessing the client connection portion to the server
    public ClientConnection(String ip, int port, Consumer<PokerInfo> callback) {
        this.onReceive = callback;

        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (Exception e) {
            System.out.println("Client failed to connect");
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();

                if (obj instanceof PokerInfo) {
                    PokerInfo info = (PokerInfo) obj;
                    onReceive.accept(info);
                }
            }
        } catch (Exception e) {
            System.out.println("Connection closed");
        }
    }

    public void send(PokerInfo info) {
        try {
            out.writeObject(info);
            out.flush();
        } catch (Exception e) {
            System.out.println("Failed to send");
        }
    }

    public void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            System.out.println("Failed to close");
        }
    }
}
