
public class Main {
    public static void main(String[] args) {

        int serverPort = 5000;
        String logFile = "logs.txt";
        new Thread(() -> new LogServer(serverPort, logFile).start()).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

        int clientPort = 5000;
        SocketLogClient client = new SocketLogClient("127.0.0.1", clientPort);
        client.sendLog("INFO", "Hello LogServer!");

    }
}