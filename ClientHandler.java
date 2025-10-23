import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

class ClientHandler implements Runnable {
    private final Socket socket;
    private final File rootDir;

    ClientHandler(Socket socket, File rootDir) {
        this.socket = socket;
        this.rootDir = rootDir;
    }
    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // 1) Lese nur die erste Zeile (Request-Line) und gib sie aus
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // (Optional) — lese und verwerfe die restlichen Header bis zur leeren Zeile
            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
                // nichts tun
            }

            // 2) Sende eine sehr einfache, korrekte HTTP-Antwort
            String body = "<html><body><h1>Hello from Simple Server</h1></body></html>";
            out.print("HTTP/1.1 200 OK\r\n");
            out.print("Content-Type: text/html; charset=utf-8\r\n");
            out.print("Content-Length: " + body.getBytes("UTF-8").length + "\r\n");
            out.print("Connection: close\r\n");
            out.print("\r\n");
            out.print(body);
            out.flush();

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

}
