package client;

import java.awt.event.*;
import javax.swing.*;

public class client implements ActionListener{
	static	 client       		c 				= 		new client();
	static	 boolean            t            	=	    false;
	static 	 GrapheUser      	gu           	=   	new GrapheUser ();
    static 	 String          	clientNom    	=    	null;
    static   String				clientPwd	    =		null;
    int             			connection  	=   	0;
    /**
     *  on considerer les clients et les mots de passe  sont :
     *    client               mot de passe
     *    davide                eisti0001
     *    lucas                 eisti0002
     *    bernard               eisti0003
     *    paul                  eisti0004
     *    李睿                   lirui
     *    赵方圆					fangyuan
     *    黄思哲					huangsizhe
     *    郝老师					haozhaojun
     **/   
   //String   [] user    =   {"davide", "lucas", "bernard", "paul", "李睿", "赵方圆", "黄思喆", "郝老师"};
   //String   [] pwd     =   {"eisti0001", "eisti0002", "eisti0003", "eisti0004", "lirui", "fangyuan", "huangsizhe", "haozhaojun"};

   @Override
   public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	String s  =  e.getActionCommand();
	//int p = juger();
	if (s.equals("Login")) {
		clientNom	=  gu.user.getText();
		clientPwd	=  gu.pwd.getText();
		t = true;
		//gu.frame1	.setVisible(false);
		connection	=	1;
		//new SocketClient(clientNom, clientPwd);
		//c.auth();
		//c.last();
		if (gomain == 1) {
			c.last();
		}
	}
	if (s.equals("Annuler")) {
		gu.user	.setText("");
		gu.pwd	.setText("");
	}
}
   /**
    *  afficher les erreurs  
    */
	public static void viewError (String error) {
		JFrame   f    =    new JFrame ("Error");
		JPanel   p    =    new JPanel ();
		JLabel   l    =    new JLabel (error);
		f . add  (p);
		p . add	 (l);
		f . setSize (250, 50);
		f . setResizable(false);
		f . setVisible (true);
	}
	
	public void run () {
	    gu  . login    .addActionListener(this);
	    gu  . annuler  .addActionListener(this);
		gu  . faireGui();
	}
	
	public void last () {
		//a.run();
		while (true) {
			while (SocketClient.a == 0) {
				do {
					System.out.println(t);
				}while (t == false);
				t = false; 
				gomain = 1;
				new SocketClient(clientNom, clientPwd);
				SocketClient.a = 0;
			}
		}
	}
	int gomain = 0;
	public static void main (String [] args) {
		c.run();
		//new SocketClient(clientNom, clientPwd);
		c.last();
	}
}

/**
 * pour affihcer le GUI de login 
 */
class GrapheUser extends WindowAdapter{
	// ******************** parametres de gui de login ************************
    public  JFrame      frame1     =       new JFrame ("Login de Client");
    public  JPanel      panel1     =       new JPanel ();
    public  JLabel      userName   =       new JLabel ("     Nom de Client");
    public  JTextField  user       =       new JTextField (25);
    public  JLabel      pwdChar    =       new JLabel ("Votre mot de passe");
    public  JTextField  pwd        =       new JTextField (25);
    public  JButton     login      =       new JButton ("Login");
    public  JButton     annuler    =       new JButton ("Annuler");
    //public  static int   n  =  0;
  
    public void windowClosing(WindowEvent e) {
       System.exit(0);
    }
    public void faireGui () {
        frame1   .add  (panel1);
        panel1   .add  (userName);
        panel1   .add  (user);
        panel1   .add  (pwdChar);
        panel1   .add  (pwd);
        panel1   .add  (login);
        panel1   .add  (annuler);
        frame1   .addWindowListener(this);
        
        frame1   .setSize (500, 150);
        frame1   .setResizable(false);  
        frame1   .setVisible (true);
    }    
}