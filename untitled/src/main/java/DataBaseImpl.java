import de.fhwedel.verteilteSysteme.DBResult;
import de.fhwedel.verteilteSysteme.DataBase;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class DataBaseImpl implements DataBase {

    private final Map<Integer, String> records = new HashMap<>();

    @Override
    public synchronized String getRecord(int index) throws RemoteException {
        return records.get(index);
    }

    @Override
    public synchronized void addRecord(int index, String record) throws RemoteException {
        if (index < 0) {
            return;
        }
        records.put(index, record);
    }

    @Override
    public synchronized int getSize() throws RemoteException {
        return records.size();
    }

    @Override
    public synchronized DBResult getRecordObj(int index) throws RemoteException {
        String value = records.get(index);
        if (value == null) {
            return null;
        }
        return new DBResult(index, value);
    }
}
