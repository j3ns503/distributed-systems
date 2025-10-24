import java.io.*;
import java.net.Socket;

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
                OutputStream out = socket.getOutputStream();
        ) {
            // 1) Lese nur die erste Zeile (Request-Line) und gib sie aus
            String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // (Optional) — lese und verwerfe die restlichen Header bis zur leeren Zeile
            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
                // nichts tun
            }

            // 3) Pfad aus der Request-Line extrahieren
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];

            if (method.equals("GET")) {
                // 4) Wenn /files angefragt, index.html senden
                File fileToSend = new File(rootDir, "index.html");
                System.out.println(fileToSend.getAbsolutePath());
                System.out.println(fileToSend.isFile());

                if (fileToSend.exists() && !fileToSend.isDirectory()) {
                    byte[] fileBytes = java.nio.file.Files.readAllBytes(fileToSend.toPath());

                    String contentType = "text/html"; // hier einfach HTML, kann erweitert werden
                    String responseHeader = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + contentType + "\r\n" +
                            "Content-Length: " + fileBytes.length + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n";

                    out.write(responseHeader.getBytes("UTF-8"));
                    out.write(fileBytes);
                    out.flush();
                } else {
                    // Datei nicht gefunden
                    String response = "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n\r\n";
                    out.write(response.getBytes("UTF-8"));
                    out.flush();
                }
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

}
