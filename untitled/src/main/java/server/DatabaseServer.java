package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import database.DatabaseServiceGrpc;
import database.Database.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseServer {

    private final Map<Integer, String> database = new ConcurrentHashMap<>();
    private Server server;

    public void start() throws Exception {
        server = ServerBuilder.forPort(5001)
                .addService(new DatabaseServiceImpl())
                .build()
                .start();

        System.out.println("Server gestartet auf Port 5001");
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
            System.out.println("Server gestoppt.");
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


    private class DatabaseServiceImpl extends DatabaseServiceGrpc.DatabaseServiceImplBase {

        @Override
        public void addRecord(AddRecordRequest request,
                              StreamObserver<AddRecordResponse> responseObserver) {

            database.put(request.getIndex(), request.getRecord());

            AddRecordResponse response = AddRecordResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getRecord(GetRecordRequest request,
                              StreamObserver<GetRecordResponse> responseObserver) {

            String result = database.getOrDefault(request.getIndex(), "");

            GetRecordResponse response = GetRecordResponse.newBuilder()
                    .setRecord(result)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void getSize(GetSizeRequest request,
                            StreamObserver<GetSizeResponse> responseObserver) {

            int size = database.size();

            GetSizeResponse response = GetSizeResponse.newBuilder()
                    .setSize(size)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
