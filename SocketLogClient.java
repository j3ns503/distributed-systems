import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import logserver.LogMessageOuterClass;

public class SocketLogClient {

    private final String host;
    private final int port;

    public SocketLogClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendLog(String level, String message) {
        try (Socket socket = new Socket(host, port);
             OutputStream out = socket.getOutputStream()) {

            LogMessageOuterClass.Severity severity;
            switch (level.toUpperCase()) {
                case "DEBUG": severity = LogMessageOuterClass.Severity.DEBUG; break;
                case "WARN":  severity = LogMessageOuterClass.Severity.WARN;  break;
                case "ERROR": severity = LogMessageOuterClass.Severity.ERROR; break;
                default:      severity = LogMessageOuterClass.Severity.INFO;  break;
            }

            // baue deine protobuf-Nachricht
            LogMessageOuterClass.LogMessage log = LogMessageOuterClass.LogMessage.newBuilder()
                    .setLevel(severity)
                    .setMessage(message)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            // in binärer Form an Server schicken
            log.writeTo(out);
            System.out.println("Log sent: " + message);

        } catch (IOException e) {
            System.err.println("Could not send log: " + e.getMessage());
        }
    }
}
