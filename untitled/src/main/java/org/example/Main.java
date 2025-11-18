package org.example;

import client.DatabaseClient;
import server.JsonRpcDatabaseServer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws Exception {

        JsonRpcDatabaseServer server = new JsonRpcDatabaseServer();

        System.out.println("Starte Server...");

        new Thread(() -> {
            try {
                server.start(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(500);

        System.out.println("Starte Client...");
        DatabaseClient client = new DatabaseClient();
        client.run();

    }
}
