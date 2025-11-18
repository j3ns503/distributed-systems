package org.example;

import client.DatabaseClient;
import server.DatabaseServer;

public class Main {

    public static void main(String[] args) throws Exception {

        DatabaseServer server = new DatabaseServer();

        System.out.println("Starte Server...");

        new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(500);

        System.out.println("Starte Client...");
        DatabaseClient client = new DatabaseClient();
        client.run();

        server.stop();
    }
}