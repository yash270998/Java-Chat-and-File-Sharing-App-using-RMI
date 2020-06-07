import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Insets;
import java.awt.ScrollPane;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;


import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class clientGUI extends TimerTask{
	 private DefaultListModel<String> listModel;
	private JFrame frame;
	private JTextField textField;
	private JButton PMBtn,StartBtn,SendBtn;
	private  JList list = new JList();
	private JPanel userPanel = new JPanel();
	protected JPanel clientPanel = new JPanel();
	private String name,message;
	protected JTextArea msgArea = new JTextArea();
	private String ip = "172.19.96.186";
	private JList<String> filelist1 = new JList();
	/**
	 * Launch the application.
	 */
	private ChatClient client;
	  private ChatServerInt server;
	  private JTextField nameTF;
	  private JTextField inputfileTF;
	  public void doConnect(){
		    if (StartBtn.getText().equals("Start")){
		    	if (nameTF.getText().length()<2){JOptionPane.showMessageDialog(frame, "You need to type a name."); return;}
		    	if (ip.length()<2){JOptionPane.showMessageDialog(frame, "You need to type an IP."); return;}	    	
		    	try{
					client=new ChatClient(nameTF.getText());
		    		client.setGUI(this);
					server=(ChatServerInt)Naming.lookup("rmi://"+ip+"/chatapp");
					server.login(client);
					updateUsers(server.getConnected());
					Timer t = new Timer( );
					t.scheduleAtFixedRate(new TimerTask() {

					    @Override
					    public void run() {
					    	try {
								updateUsers(server.getConnected());
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					    }
					}, 1000,5000);
				    StartBtn.setText("Disconnect");			    
		    	}catch(Exception e){e.printStackTrace();JOptionPane.showMessageDialog(frame, "ERROR, we wouldn't connect....");}		  
		      }else{
		    	  	updateUsers(null);
		    	  	StartBtn.setText("Start");
		    	  	//Better to implement Logout ....
			}
		  }  
	  
	  public void sendText(){
	    if (StartBtn.getText().equals("Start")){
	    	JOptionPane.showMessageDialog(frame, "You need to connect first."); return;	
	    }
	      String st=textField.getText();
	      st="["+nameTF.getText()+"] "+st;
	      textField.setText("");
	      //Remove if you are going to implement for remote invocation
	      try{
	    	  	server.publish(st);
	    	  //	updateUsers(server.getConnected());
	  	  	}catch(Exception e){e.printStackTrace();}
	  }
	 
	  public void writeMsg(String st){  msgArea.setText(msgArea.getText()+"\n"+st);  }
	 
	  public void updateUsers(Vector v){
	      DefaultListModel listModel = new DefaultListModel();
	      if(v!=null) for (int i=0;i<v.size();i++){
	    	  try{  String tmp=((ChatClientInt)v.get(i)).getName();
	    	  		listModel.addElement(tmp);
	    	  }catch(Exception e){e.printStackTrace();}
	      }
	      System.out.println(listModel);
	      list.setModel(listModel);
	      
	  }
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					clientGUI window = new clientGUI();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public clientGUI() {
		initialize();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 604, 560);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
	//	JPanel userPanel = new JPanel();
		userPanel.setBounds(12, 46, 109, 259);
		
	//	JPanel clientPanel = new JPanel();
		userPanel.add(clientPanel);

		list.setFixedCellHeight(20);
       //System.out.println(clientPanel.getWidth());
		 list.setFixedCellWidth(userPanel.getWidth());
		clientPanel.add(list);
		frame.getContentPane().add(userPanel);
		
		JPanel btnPanel = new JPanel();
		btnPanel.setBounds(12, 318, 109, 68);
		frame.getContentPane().add(btnPanel);
		SendBtn = new JButton("Send");
		StartBtn = new JButton("Start");
		StartBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				doConnect();
				
			}
		});
		btnPanel.add(StartBtn);
		
		
	
		SendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendText();
			}
		});
		btnPanel.add(SendBtn);
		
		
		msgArea.setBounds(133, 46, 414, 344);
		frame.getContentPane().add(msgArea);
		msgArea.setColumns(48);
		msgArea.setMargin(new Insets(10, 15, 10, 15));
		msgArea.setRows(18);
		
		
		textField = new JTextField();
		textField.setBounds(133, 403, 413, 22);
		frame.getContentPane().add(textField);
		textField.setColumns(37);
		
		JLabel lblCurrentUsers = new JLabel("Current Users");
		lblCurrentUsers.setBounds(12, 24, 95, 16);
		frame.getContentPane().add(lblCurrentUsers);
		
		nameTF = new JTextField();
		nameTF.setBounds(431, 21, 116, 22);
		frame.getContentPane().add(nameTF);
		nameTF.setColumns(10);
		
		JLabel lblUsername = new JLabel("UserName:");
		lblUsername.setBounds(355, 24, 64, 16);
		frame.getContentPane().add(lblUsername);
		
		JButton uploadBtn = new JButton("Upload");
		uploadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
				fd.setVisible(true);
				String clientpath = fd.getDirectory()+fd.getFile();
				
				System.out.println(clientpath);
				//String clientpath = "S:/Eclipse workspace/chatmark2/serverfiles";
				String serverpath = "S:/serverfiles/"+fd.getFile();
				
				File clientpathfile = new File(clientpath);
				
				byte [] mydata=new byte[(int) clientpathfile.length()];
				FileInputStream in;
				try {
					in = new FileInputStream(clientpathfile);
					in.read(mydata, 0, mydata.length);	
					server.uploadFileToServer(mydata, serverpath, (int) clientpathfile.length());
					 System.out.println(mydata);
					 JOptionPane.showMessageDialog(null,"Upload Complete");
					 in.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
					System.out.println("uploading to server...");	
					
					
				// in.read(mydata, 0, mydata.length);					 
				 
				
			}
		});
		uploadBtn.setBounds(133, 475, 97, 25);
		frame.getContentPane().add(uploadBtn);
		JPanel panel = new JPanel();
		panel.setBounds(12, 399, 109, 101);
		frame.getContentPane().add(panel);
		
		
		panel.add(filelist1);
		
		JLabel lblNewLabel = new JLabel("Input Filename");
		lblNewLabel.setBounds(283, 427, 97, 16);
		frame.getContentPane().add(lblNewLabel);
		
		inputfileTF = new JTextField();
		inputfileTF.setBounds(283, 452, 97, 22);
		frame.getContentPane().add(inputfileTF);
		inputfileTF.setColumns(10);
		JButton downloadBtn = new JButton("Download");
		
		downloadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = inputfileTF.getText();
				
				String serverpath = "S:/serverfiles/"+filename;
				String clientpath= "S:/clientfiles/"+filename;

				byte[] mydata;
				try {
					mydata = server.downloadFileFromServer(serverpath);
					File clientpathfile = new File(clientpath);
					FileOutputStream out=new FileOutputStream(clientpathfile);				
		    		out.write(mydata);
					out.flush();
			    	out.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("downloading...");
				JOptionPane.showMessageDialog(null,"download complete");
				
			}
		});
		downloadBtn.setBounds(283, 475, 97, 25);
		frame.getContentPane().add(downloadBtn);
		
		JButton listBtn = new JButton("List");
		listBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = "S:/serverfiles";
					String[] filelist;
			//		filelist = new JList<>(model);
				DefaultListModel<String> model = new DefaultListModel<>();
			
				try {
					filelist = server.listFiles(path);
					for(int i =0;i < filelist.length;i++) {
						model.addElement(filelist[i]);
					}
					filelist1.setModel(model);
						
						for (String i: filelist)
						{
							System.out.println(i);
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
			}
		});
		listBtn.setBounds(437, 475, 97, 25);
		frame.getContentPane().add(listBtn);
		
		JLabel lblNewLabel_1 = new JLabel("Show Server Files");
		lblNewLabel_1.setBounds(431, 427, 116, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		
		//msgArea.append(str);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

