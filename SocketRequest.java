import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketRequest {
    public void sendHTTPRequest() {
        String internetHost = "fh-wedel.de";
        String intranetHost = "stud.fh-wedel.de";
        String host = internetHost;
        int port = 80;
        try (Socket socket = new Socket(host, port)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.print("GET / HTTP/1.1\r\n");
            output.print("Host: " + host + "\r\n");
            output.print("Connection: close\r\n");
            output.print("\r\n");
            output.flush();

            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
