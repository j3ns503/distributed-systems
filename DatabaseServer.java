import java.io.*;
import java.net.*;
import java.util.*;
import database.Database;

public class DatabaseServer {
    private final int port;
    private final Map<Integer, String> database = new HashMap<>();

    public DatabaseServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("DatabaseServer listening on port " + port);

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
            // Request lesen
            Database.RPC_Request request =
                    Database.RPC_Request.parseFrom(client.getInputStream());

            Database.RPC_Response.Builder responseBuilder =
                    Database.RPC_Response.newBuilder();

            switch (request.getOperation()) {
                case GET_RECORD:
                    int index = request.getGetRecord().getIndex();
                    String record = database.get(index);
                    if (record != null) {
                        responseBuilder
                                .setSuccess(true)
                                .setGetRecordResponse(
                                        Database.GetRecordResponse.newBuilder()
                                                .setRecord(record)
                                                .build());
                    } else {
                        responseBuilder
                                .setSuccess(false)
                                .setMessage("Record not found for index " + index);
                    }
                    break;

                case ADD_RECORD:
                    int addIndex = request.getAddRecord().getIndex();
                    String value = request.getAddRecord().getRecord();
                    database.put(addIndex, value);
                    responseBuilder
                            .setSuccess(true)
                            .setMessage("Added record " + addIndex + " -> " + value);
                    break;

                case GET_SIZE:
                    responseBuilder
                            .setSuccess(true)
                            .setGetSizeResponse(
                                    Database.GetSizeResponse.newBuilder()
                                            .setSize(database.size())
                                            .build());
                    break;

                default:
                    responseBuilder
                            .setSuccess(false)
                            .setMessage("Unknown operation");
            }

            // Antwort zurückschicken
            Database.RPC_Response response = responseBuilder.build();
            response.writeTo(client.getOutputStream());

            System.out.println("Processed " + request.getOperation());

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try { client.close(); } catch (IOException ignored) {}
        }
    }
}
