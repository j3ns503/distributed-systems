import de.fhwedel.verteilteSysteme.DataBase;

import java.rmi.RemoteException;
import java.util.List;

public class DataBaseImpl implements DataBase {
    public List<String> records;

    @Override
    public synchronized String getRecord(int index) throws RemoteException {
        if (index < 0 || index >= records.size()) {
            return null;
        }
        return records.get(index);
    }
    @Override
    public synchronized void addRecord(int index, String record) throws RemoteException {
        if (index >= 0 && index <= records.size()) {
            records.add(index, record);
        }
        else if (index > records.size()) {
            records.add(record);
        }
    }
    @Override
    public synchronized int getSize() throws RemoteException {
        return records.size();
    }
}
