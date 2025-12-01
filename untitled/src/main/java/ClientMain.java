import de.fhwedel.verteilteSysteme.DBResult;
import de.fhwedel.verteilteSysteme.DataBase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;

public class ClientMain {

    public static void main(String[] args) {

        try {
            // 1. Stub aus Datei laden
            DataBase db = readStubFromFile("database.stub");

            db.addRecord(4101, "Appen");
            db.addRecord(4102, "Ahrensburg");
            db.addRecord(4103, "Wedel");
            db.addRecord(4104, "Aumühle");
            db.addRecord(4105, "Seevetal");
            db.addRecord(4106, "Quickborn");

            System.out.println("Record 4103: " + db.getRecord(4103));
            System.out.println("Record 4107: " + db.getRecord(4107));

            System.out.println(db.getSize());

            System.out.println(db.getRecordObj(4103).getKey() + " " + db.getRecordObj(4103).getValue());
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
