import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import logserver.LogMessageOuterClass;

public class LogServer {

    private final int port;
    private final String logFilePath;

    public LogServer(int port, String logFilePath) {
        this.port = port;
        this.logFilePath = logFilePath;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("LogServer listening on port " + port);

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handleClient(client)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket client) {
        try {
            // 1) Nachricht aus Socket einlesen
            LogMessageOuterClass.LogMessage logMessage = LogMessageOuterClass.LogMessage.parseFrom(client.getInputStream());

            // 2) Nachricht in Datei schreiben, eine Zeile pro Log
            try (FileOutputStream fos = new FileOutputStream(logFilePath, true)) {
                String line = String.format("[%d] %s: %s%n",
                        logMessage.getTimestamp(),
                        logMessage.getLevel(),
                        logMessage.getMessage());
                fos.write(line.getBytes("UTF-8"));
            }

            System.out.println("Received log: " + logMessage.getMessage());

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try { client.close(); } catch (IOException ignored) {}
        }
    }
}
