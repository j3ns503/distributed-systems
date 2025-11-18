package client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import database.DatabaseServiceGrpc;
import database.Database.*;

public class DatabaseClient {

    public void run() {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 5001)
                .usePlaintext()
                .build();

        DatabaseServiceGrpc.DatabaseServiceBlockingStub stub =
                DatabaseServiceGrpc.newBlockingStub(channel);

        add(stub, 4101, "Appen");
        add(stub, 4102, "Ahrensburg");
        add(stub, 4103, "Wedel");
        add(stub, 4104, "Aumühle");
        add(stub, 4105, "Seevetal");
        add(stub, 4106, "Quickborn");

        read(stub, 4103);
        read(stub, 4107);

        getSize(stub);

        channel.shutdown();
    }

    private void add(DatabaseServiceGrpc.DatabaseServiceBlockingStub stub,
                     int index, String record) {

        AddRecordRequest req = AddRecordRequest.newBuilder()
                .setIndex(index)
                .setRecord(record)
                .build();

        AddRecordResponse res = stub.addRecord(req);

        System.out.println("[ADD] " + index + " -> " + res.getSuccess());
    }

    private void read(DatabaseServiceGrpc.DatabaseServiceBlockingStub stub,
                      int index) {

        GetRecordRequest req = GetRecordRequest.newBuilder()
                .setIndex(index)
                .build();

        GetRecordResponse res = stub.getRecord(req);

        String out = res.getRecord().isEmpty() ? "<not found>" : res.getRecord();
        System.out.println("[GET] " + index + " -> " + out);
    }

    private void getSize(DatabaseServiceGrpc.DatabaseServiceBlockingStub stub) {

        GetSizeResponse res =
                stub.getSize(GetSizeRequest.newBuilder().build());

        System.out.println("[SIZE] " + res.getSize());
    }
}
