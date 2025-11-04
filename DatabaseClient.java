import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import database.Database;

public class DatabaseClient {
    private final String host;
    private final int port;

    public DatabaseClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Database.RPC_Response send(Database.RPC_Request request)
            throws IOException {
        try (Socket socket = new Socket(host, port)) {
            OutputStream out = socket.getOutputStream();
            request.writeTo(out);
            socket.shutdownOutput(); // signal: request done

            InputStream in = socket.getInputStream();
            return Database.RPC_Response.parseFrom(in);
        }
    }

    public void addRecord(int index, String record) {
        Database.RPC_Request request =
                Database.RPC_Request.newBuilder()
                        .setOperation(Database.RPC_Request.Operation.ADD_RECORD)
                        .setAddRecord(
                                Database.AddRecordRequest.newBuilder()
                                        .setIndex(index)
                                        .setRecord(record)
                                        .build())
                        .build();

        try {
            Database.RPC_Response resp = send(request);
            System.out.println(resp.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getRecord(int index) {
        Database.RPC_Request request =
                Database.RPC_Request.newBuilder()
                        .setOperation(Database.RPC_Request.Operation.GET_RECORD)
                        .setGetRecord(
                                Database.GetRecordRequest.newBuilder()
                                        .setIndex(index)
                                        .build())
                        .build();

        try {
            Database.RPC_Response resp = send(request);
            if (resp.getSuccess() && resp.hasGetRecordResponse()) {
                System.out.println(index + " -> " + resp.getGetRecordResponse().getRecord());
            } else {
                System.out.println("Record not found for index " + index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSize() {
        Database.RPC_Request request =
                Database.RPC_Request.newBuilder()
                        .setOperation(Database.RPC_Request.Operation.GET_SIZE)
                        .build();

        try {
            Database.RPC_Response resp = send(request);
            if (resp.getSuccess()) {
                System.out.println("Database size: " + resp.getGetSizeResponse().getSize());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
