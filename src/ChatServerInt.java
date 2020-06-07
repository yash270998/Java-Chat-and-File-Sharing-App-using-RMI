import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ChatServerInt extends Remote{
	public boolean login (ChatClientInt a)throws RemoteException ;
	public void publish (String s)throws RemoteException ;
	public Vector getConnected() throws RemoteException ;
	public void uploadFileToServer(byte[] mybyte, String serverpath, int length) throws RemoteException;
	public byte[] downloadFileFromServer(String servername) throws RemoteException;
	public String[] listFiles(String serverpath) throws RemoteException;
}
