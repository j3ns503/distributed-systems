package client;

import javax.json.*;
import java.io.*;
import java.net.Socket;

public class DatabaseClient {

    private int requestId = 1;

    public void run() throws Exception {

        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            addRecord(out, in, 4101, "Appen");
            addRecord(out, in, 4102, "Ahrensburg");
            addRecord(out, in, 4103, "Wedel");
            addRecord(out, in, 4104, "Aumühle");
            addRecord(out, in, 4105, "Seevetal");
            addRecord(out, in, 4106, "Quickborn");

            getRecord(out, in, 4103);
            getRecord(out, in, 4107);

            getSize(out, in);
        }
    }

    private void addRecord(BufferedWriter out, BufferedReader in, int key, String value) throws Exception {

        JsonObject request = Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("method", "addRecord")
                .add("params", Json.createObjectBuilder()
                        .add("key", key)
                        .add("value", value))
                .add("id", requestId++)
                .build();

        send(out, request);

        JsonObject response = receive(in);

        // server returns: { "result": { "success": true } }
        boolean success = response
                .getJsonObject("result")
                .getBoolean("success");

        System.out.println("[ADD] " + key + " -> " + success);
    }

    private void getRecord(BufferedWriter out, BufferedReader in, int key) throws Exception {

        JsonObject request = Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("method", "getRecord")
                .add("params", Json.createObjectBuilder()
                        .add("key", key))
                .add("id", requestId++)
                .build();

        send(out, request);

        JsonObject response = receive(in);

        // server returns: { "result": { "value": "..." } }
        String value = response
                .getJsonObject("result")
                .getString("value");

        System.out.println("[GET] " + key + " -> " + (value.isEmpty() ? "<not found>" : value));
    }

    private void getSize(BufferedWriter out, BufferedReader in) throws Exception {

        JsonObject request = Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("method", "getSize")
                .add("params", Json.createObjectBuilder())
                .add("id", requestId++)
                .build();

        send(out, request);

        JsonObject response = receive(in);

        int size = response
                .getJsonObject("result")
                .getInt("size");

        System.out.println("[SIZE] " + size);
    }

    private void send(BufferedWriter out, JsonObject request) throws IOException {
        out.write(request.toString());
        out.newLine();
        out.flush();
    }

    private JsonObject receive(BufferedReader in) throws Exception {
        String line = in.readLine();
        JsonReader reader = Json.createReader(new StringReader(line));
        return reader.readObject();
    }
}
