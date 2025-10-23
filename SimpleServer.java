import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    private final int port;
    private final File rootDir;

    public SimpleServer(int port, File rootDir) {
        this.port = port;
        this.rootDir = rootDir;
    }

    public void start() throws IOException {
        if (!rootDir.isDirectory()) {
            throw new IllegalArgumentException("rootDir must be a directory: " + rootDir);
        }
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SimpleHttpServer listening on port " + port + " serving " + rootDir.getAbsolutePath());
            while (true) {
                Socket client = serverSocket.accept();
                // Für jede eingehende Verbindung neuen Thread starten
                new Thread(new ClientHandler(client, rootDir)).start();
            }
        }
    }
}
