import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatClient extends UnicastRemoteObject implements ChatClientInt {
	private String name;
	private clientGUI ui;	
	public ChatClient (String n) throws RemoteException {
		name=n;
		}
	
	public void tell(String st) throws RemoteException{
		System.out.println(st);
		ui.writeMsg(st);
		//ui.msgArea.append(st);
	}
	public String getName() throws RemoteException{
		return name;
	}
	
	public void setGUI(clientGUI t){ 
		ui=t ; 
	} 	
}
