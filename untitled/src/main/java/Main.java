import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) {
        DataBaseImpl db = new DataBaseImpl();

        try {
            Remote stub = UnicastRemoteObject.exportObject(db, 0);

            writeStubToFile("database.stub", stub);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }

    private static void writeStubToFile ( String fileName , Remote stub )
            throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream ( fileName );
        ObjectOutputStream out = new ObjectOutputStream ( fos );
        out . writeObject ( stub );
        out . close ();
    }
}
