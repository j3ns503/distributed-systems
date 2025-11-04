
public class Main {
    /// task1
    /*public static void main(String[] args) {
        /// task 1
        int serverPort = 5000;
        String logFile = "logs.txt";

        LogServer server = new LogServer(serverPort, logFile);
        Thread serverThread = new Thread(server::start);
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}

        int clientPort = 5000;
        SocketLogClient client = new SocketLogClient("127.0.0.1", clientPort);
        client.sendLog("INFO", "Hello LogServer!");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}


        System.exit(0);

    }*/

    public static void main(String[] args) throws InterruptedException {
        int port = 6000;
        DatabaseServer server = new DatabaseServer(port);
        new Thread(server::start).start();

        Thread.sleep(1000); // Server starten lassen

        DatabaseClient client = new DatabaseClient("127.0.0.1", port);

        client.addRecord(4101, "Appen");
        client.addRecord(4102, "Ahrensburg");
        client.addRecord(4103, "Wedel");
        client.addRecord(4104, "Aumühle");
        client.addRecord(4105, "Seevetal");
        client.addRecord(4106, "Quickborn");

        client.getRecord(4103);
        client.getRecord(4107); // nicht vorhanden
        client.getSize();
    }
}