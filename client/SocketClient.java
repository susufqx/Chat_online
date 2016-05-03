package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;   
import java.net.Socket;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.swing.*;
import cryptographie.RSA;

 /**
  * main class et main fonction pour realiser cette programmation 
  */
public class SocketClient   extends WindowAdapter  {  
	grapheError				ge			=		new grapheError();
	static int				a			=		0;
    static String      		clientNom   =   	null;
    static String			clientPwd	=		null;
    //int             		connection  =   	0;
    String          		tempChar    =   	null;
    //GrapheUser          	gu          =  		new GrapheUser();
	//************************ socket et i/o stream ***************************
    Socket          		socket      =   	null;  
    ObjectOutputStream 		os		 	= 		null;
    ObjectInputStream  		is 		 	= 		null;; 
    //************************ information de connection **********************
    String          		serverIP    =   	"127.0.0.1";   // IP de serveur
    int             		port        =   	8080;          // prote de serveur
    //************************** parametres de RSA ****************************
    RSA                 	rsa         =   	null;
    private   RSAPublicKey 		publicKey;
    private   RSAPrivateKey	 	privateKey;
    private   RSAPublicKey      getKey;
    //************************** parametres de chat ***************************
	public JFrame      frame1      =     new JFrame 	();
	public JPanel      panel1      =     new JPanel 	();
	public JPanel      panel2      =     new JPanel     ();
	public JPanel      panel3      =     new JPanel     ();
	public JPanel      panelD      =     new JPanel		();
	public JTextArea   chatText    =     new JTextArea(20, 32);
	public JTextArea   chatEnter   =     new JTextArea(6, 22);
	public JButton     send        =     new JButton 	("Envoyer");
	public JButton     clear       =     new JButton    ("Liquider");
	public JScrollPane jsp1        =     new JScrollPane (chatText);
	public JScrollPane jsp2        =     new JScrollPane (chatEnter);
	
	@Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    
    public SocketClient (String nom, String pwd) {
    	clientNom 	=	nom;
    	clientPwd	=	pwd;
    	String		juger	=	null;
    	String		msg		=	null;
    	frame1	.setLayout(new BorderLayout(5,5));
    	frame1	.add("North",panel1);
    	frame1  .add("South",panelD);
    	panelD	.add("West", panel2);
    	panelD  .add("East", panel3);
    	panel3  .setLayout(new GridLayout(3, 1, 5, 5));
    	panel1	.add(jsp1);
    	panel2	.add(jsp2);
    	panel3	.add(send);
    	panel3	.add(clear);
    	chatText.setEditable(false);
    	chatText.setLineWrap(true);
    	chatEnter.setLineWrap(true);
    	
    	frame1  .addWindowListener(this);
        frame1  .setSize (400, 500);
        frame1  .setResizable(false);
    	frame1	.setVisible(false);
    	send	.addActionListener(new buttonListener());
    	clear   .addActionListener(new buttonListener());
    	ge.b	.addActionListener(new buttonListener());
    	try {  
    			rsa     =   new RSA();
    			System.out.println(nom+pwd);
    			System.out.println("+++++++++++");
				socket     		=   new Socket(serverIP, port);  
				os = new ObjectOutputStream(socket.getOutputStream());
				is = new ObjectInputStream (socket.getInputStream());
				
				msg		= clientNom + "&&" + clientPwd;
				
				os.writeUTF(msg);
				os.flush();
				juger	=	is.readUTF();
				System.out.println(juger);
				
				if (juger.equals("true")) {
					rsa.getKey();
					publicKey  =  rsa.getPubKey();
					privateKey =  rsa.getPriKey();
					
					System.out.println(publicKey);
					os.writeObject(publicKey);
					os.flush();
					getKey = (RSAPublicKey) is.readObject();
					client.gu.frame1.setVisible(false);
					
					frame1  .setTitle(clientNom);
					frame1  .setVisible(true);
					
				} else {
					ge.viewError("Nom ou mot de passe est incorrect.");
					frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					client.t = false;
					a		 = 1;
				}
				int temp;
				while (true) {
					byte[]  resultBytes   =     new byte[256];
					temp = is.read(resultBytes);
					
			        byte[]  decBytes      =     rsa.decrypt(privateKey,resultBytes);   
		            String  dec           =     new String(decBytes);
		            
    				if (chatText.getText().equals("")) {
    					chatText.setText(chatText.getText()+"serveur:\n"+dec);
    					chatEnter.setText("");
    				}else {
    					chatText.setText(chatText.getText()+"\n"+"serveur:\n"+dec);
    					chatEnter.setText("");
    				}	
				}
				
        	} catch (Exception e) {  
        		e.printStackTrace();
        	} 
    }

    public class buttonListener  implements ActionListener {
    	public void actionPerformed(ActionEvent evt) {
    		String s  =  evt.getActionCommand();
    		if (s.equals("Envoyer") && chatEnter.getText() != null) {
    			try {
    				String input = chatEnter.getText().toString();
    				tempChar  =   input;
    				if (chatText.getText().equals("")) {
    					chatText.setText(chatText.getText()+clientNom+"：\n"+input);
    					chatEnter.setText("");
    				}else {
    					chatText.setText(chatText.getText()+"\n"+clientNom+"：\n"+input);
    					chatEnter.setText("");
    				}	
    				
    				String  newtemp         =  clientNom+":\n"+tempChar;
    				byte[]  srcBytes        =   newtemp.getBytes();   
		            byte[]  resultBytes     =   rsa.encrypt (getKey, srcBytes);  
    				os.			write(resultBytes);
    				os.			flush();
    			} catch (IOException e) {
					e.printStackTrace();
    			}
    		}
    		if (s.equals("Liquider")) {
    			chatText.setText("");
    		}
    		if (s.equals("Valider")) {
    			ge.f.setVisible(false);
    			client.t = false;
    			//frame1.
    			//client a = new client ();
				//a.last(a);
    		}
    	}
    }

}  

class grapheError {
    /**
     *  afficher les erreurs  
     */
	JFrame   f    =    new JFrame ("Error");
	JPanel   p    =    new JPanel ();
	JLabel   l    =    new JLabel ();
	JButton  b    =    new JButton ("Valider");
    public void viewError (String error) {

    	//b .addActionListener(new buttonListener());
    	l . setText(error);
    	f . add (p);
    	p . add ("North",l);
    	p . add ("South",b);
    	f . setSize (250, 150);
    	f . setResizable(false);
    	f . setVisible (true);
    }
} 

