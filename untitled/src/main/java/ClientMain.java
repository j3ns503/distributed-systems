package de.fhwedel.verteilteSysteme;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;

public class ClientMain {

    public static void main(String[] args) {

        try {
            // 1. Stub aus Datei laden
            DataBase db = readStubFromFile("database.stub");

            // 2. Remote-Methoden testen
            System.out.println("Initial size: " + db.getSize());

            db.addRecord(0, "Record A");
            db.addRecord(1, "Record B");

            System.out.println("Size after adds: " + db.getSize());
            System.out.println("Record 0: " + db.getRecord(0));
            System.out.println("Record 1: " + db.getRecord(1));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DataBase readStubFromFile(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(fis);

        DataBase remoteObj = (DataBase) in.readObject();

        in.close();
        return remoteObj;
    }
}
