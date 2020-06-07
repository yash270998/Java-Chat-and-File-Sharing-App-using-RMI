import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
public class ChatServer extends UnicastRemoteObject implements ChatServerInt{
	private Vector v=new Vector();
	public ChatServer() throws RemoteException{}
	
	public boolean login(ChatClientInt a) throws RemoteException{	
		System.out.println(a.getName() + "  got connected....");	
		a.tell("You have Connected successfully.");
		publish(a.getName()+ " has just connected.");
		v.add(a);
		return true;		
	}
	
	public void publish(String s) throws RemoteException{
	    System.out.println(s);
		for(int i=0;i<v.size();i++){
		    try{
		    	ChatClientInt tmp=(ChatClientInt)v.get(i);
			tmp.tell(s);
		    }catch(Exception e){
		    	//problem with the client not connected.
		    	//Better to remove it
		    }
		}
	}
 
	public Vector getConnected() throws RemoteException{
		return v;
	}

	@Override
	public void uploadFileToServer(byte[] mydata, String serverpath, int length) throws RemoteException {
		// TODO Auto-generated method stub
		try {
    		File serverpathfile = new File(serverpath);
    		FileOutputStream out=new FileOutputStream(serverpathfile);
    		byte [] data=mydata;
			
    		out.write(data);
			out.flush();
	    	out.close();
	    	System.out.println("Done writing data...");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	@Override
	public byte[] downloadFileFromServer(String serverpath) throws RemoteException {
		byte [] mydata;	
		
		File serverpathfile = new File(serverpath);			
		mydata=new byte[(int) serverpathfile.length()];
		FileInputStream in;
		try {
			in = new FileInputStream(serverpathfile);
			try {
				in.read(mydata, 0, mydata.length);
			} catch (IOException e) {
				
				e.printStackTrace();
			}						
			try {
				in.close();
			} catch (IOException e) {
			
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		return mydata;
	}

	@Override
	public String[] listFiles(String serverpath) throws RemoteException {
		File serverpathdir = new File(serverpath);
		return serverpathdir.list();
	}
}
