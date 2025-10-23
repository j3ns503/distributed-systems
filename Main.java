import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        /// Task1
       /* SocketRequest request1 = new SocketRequest();
        request1.sendHTTPRequest();*/

        /*URLpackageRequest urlpackageRequest = new URLpackageRequest();
        urlpackageRequest.sendURLRequest();*/

        /// Task2
        int port = 8080;
        File root = new File("www");
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Listening on port " + port);
            while (true) {
                Socket client = server.accept();
                new Thread(new ClientHandler(client, root)).start();
            }
        } catch (IOException e)  {
            e.printStackTrace();
        }

    }
}