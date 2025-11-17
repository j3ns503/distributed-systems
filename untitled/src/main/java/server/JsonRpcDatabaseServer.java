package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.json.*;   // Java JSON API

public class JsonRpcDatabaseServer {

    private final Map<Integer, String> database = new HashMap<>();

    public void start(int port) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("JSON-RPC Server gestartet auf Port " + port);

        while (true) {
            Socket client = serverSocket.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }

    private void handleClient(Socket client) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))
        ) {
            String line;

            while ((line = reader.readLine()) != null) {

                JsonObject request = Json.createReader(new StringReader(line)).readObject();

                String method = request.getString("method");
                JsonObject params = request.getJsonObject("params");
                int id = request.getInt("id");

                JsonObject response;

                switch (method) {
                    case "addRecord":
                        int addKey = params.getInt("key");
                        String addVal = params.getString("value");
                        database.put(addKey, addVal);

                        response = jsonRpcResult(
                                Json.createObjectBuilder().add("success", true).build(),
                                id
                        );
                        break;

                    case "getRecord":
                        int getKey = params.getInt("key");
                        String value = database.getOrDefault(getKey, "");

                        response = jsonRpcResult(
                                Json.createObjectBuilder().add("value", value).build(),
                                id
                        );
                        break;

                    case "getSize":
                        int size = database.size();
                        response = jsonRpcResult(
                                Json.createObjectBuilder().add("size", size).build(),
                                id
                        );
                        break;

                    default:
                        response = jsonRpcError("Unknown method: " + method, id);
                        break;
                }

                writer.write(response.toString());
                writer.newLine();
                writer.flush();
            }

        } catch (Exception e) {
            System.out.println("Client getrennt: " + e.getMessage());
        }
    }

    private JsonObject jsonRpcResult(JsonObject result, int id) {
        return Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("result", result)
                .add("id", id)
                .build();
    }

    private JsonObject jsonRpcError(String message, int id) {
        return Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("error",
                        Json.createObjectBuilder()
                                .add("code", -32601)
                                .add("message", message)
                )
                .add("id", id)
                .build();
    }
}
