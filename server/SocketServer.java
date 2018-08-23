package server;

import java.io.*;
import java.net.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import cryptographie.RSA;

public class SocketServer extends WindowAdapter{
    //**************************** socket et i/o stream ************************
    private ServerSocket serverSocket = null;
    private ObjectInputStream inputstream = null;
    private ObjectOutputStream outputstream = null;
    //**************************** parametres de RSA ***************************
    private RSA rsa = null;
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private RSAPublicKey getKey;
    //*************************parametres de chat ******************************
    public JFrame frame1 = new JFrame("Chat de Serveur");
    public JFrame frame2 = new JFrame("Serveur");
    public JPanel panel1 = new JPanel();
    public JPanel panel2 = new JPanel();
    public JPanel panel3 = new JPanel();
    public JPanel panel4 = new JPanel();
    public JPanel panelD = new JPanel();
    public JLabel label = new JLabel("Il n'y a pas de Client qui a connecte le serveur");
    public JTextArea chatText = new JTextArea(20, 32);
    public JTextArea chatEnter = new JTextArea(6, 22);
    public JButton send = new JButton("Envoyer");
    public JButton clear = new JButton("Liquider");
    public JScrollPane jsp1 = new JScrollPane(chatText);
    public JScrollPane jsp2 = new JScrollPane(chatEnter);
    //
    /**
    *  on considerer les clients et les mots de passe  sont :
    *    client mot de passe
    *    davide eisti0001
    *    lucas eisti0002
    *    bernard eisti0003
    *    paul eisti0004
    *    李睿 lirui
    *    赵方圆 fangyuan
    *    黄思哲 huangsizhe
    *    郝老师 haozhaojun
    **/
    String [] userN = {"davide&&eisti0001", "lucas&&eisti0002", "bernard&&eisti0003", "paul&&eisti0004", "李睿&&lirui", "赵方圆&&fangyuan", "黄思喆&&huangsizhe", "郝老师&&haozhaojun"};
    // ******************************* debut ***********************************
    public boolean userLogin(String user) {
        boolean juger = false;
        int i;
        for (i=0;i<8;i++) {
            if (user.equals( userN[i] )) {
                juger = true;
            }
        }
        return juger;
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
    /**
     *  constructeur de class SocketServer
     * */
    public SocketServer () throws ClassNotFoundException {
        String juger = null;
        String msg = null;

        frame1.setLayout(new BorderLayout(5,5));
        frame1.add("North",panel1);
        frame1.add("South",panelD);
        panelD.add("West",panel2);
        panelD.add("East",panel4);
        panel4.setLayout(new GridLayout(3, 1, 5, 5));
        panel1.add(jsp1);
        panel2.add(jsp2);
        panel4.add(send);
        panel4.add(clear);
        chatText.setEditable(false);
        chatText.setLineWrap(true);
        chatEnter.setLineWrap(true);
        frame2.add(panel3);
        panel3.add("CENTER", label);
        frame2.setSize (350, 100);
        frame1.addWindowListener(this);
        frame1.setSize (400, 500);
        frame1.setResizable(false);
        frame2.setResizable(false);
        frame1.setVisible(false);
        frame2.setVisible(true);

        Socket socket = null;
        int port = 8080;

        rsa = new RSA();

        try{
            serverSocket = new ServerSocket(port);
            System.out.println("server start:");
            int iiii = 0;   // pour juger si on peut obtenir le cle publique par client
            int ni = 0;
            //int pi   = 0;
            rsa.getKey();
            publicKey = rsa.getPubKey();  // obtenir le cle publique qui on peut envoyer a client
            privateKey = rsa.getPriKey();  // obtenir le cle privee

            while(true){
                socket = serverSocket.accept();
                inputstream = new ObjectInputStream (socket.getInputStream());
                outputstream = new ObjectOutputStream(socket.getOutputStream());

                while (ni == 0) {
                    System.out.println("进入循环");
                    msg = inputstream.readUTF().toString();
                    boolean t = userLogin(msg);
                    if (t == true) {
                        juger = "true";
                    } else {
                        juger = "false";
                    }
                    outputstream.writeUTF(juger);
                    outputstream.flush();
                    ni = 1;
                }
                System.out.println("结束循环"+msg+iiii+juger);
                while (iiii == 0) {
                    System.out.println("得到秘钥了吗？");
                    getKey = (RSAPublicKey) inputstream.readObject();
                    outputstream.writeObject(publicKey);
                    System.out.println(getKey);
                    iiii = 1;
                }
                System.out.println("秘钥是"+getKey);
                frame2.setVisible(false);
                frame1.setVisible(true);
                Thread t = new Thread(new ClientHandler(socket));

                t.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable{
        Socket socket = null;

        public ClientHandler(Socket socket) {
            send.addActionListener(new buttonListener());
            clear.addActionListener(new buttonListener());
            this.socket = socket;
        }

        public class buttonListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent evt) {
                String s = evt.getActionCommand();
                if (s .equals("Envoyer") && chatEnter.getText() != null) {
                    try {
                        String str = chatEnter.getText().toString();
                        if (chatText.getText().equals("")) {
                            chatText.setText("Serveur：\n"+chatEnter.getText());
                            chatEnter.setText("");
                        } else {
                            chatText.setText(chatText.getText() + "\nServeur：\n" + chatEnter.getText());
                            chatEnter.setText("");
                        }
                        byte [] srcBytes = str.getBytes();
                        byte [] resultBytes = rsa.encrypt (getKey, srcBytes);
                        outputstream.write(resultBytes);
                        outputstream.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (s.equals("Liquider")) {
                    chatText.setText("");
                }
            }
        }
        public void run() {
            try {
                byte [] resultBytes = new byte[256];
                int temp;
                while ((temp = inputstream.read(resultBytes)) == 256) {
                    byte[] decBytes = rsa.decrypt(privateKey, resultBytes);
                    String dec = new String(decBytes);
                    if (chatText.getText().equals("")) {
                        chatText.setText(chatText.getText() + dec);
                    } else {
                        chatText.setText(chatText.getText() + "\n" + dec);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
  }
    public static void main(String[] args) throws ClassNotFoundException{
        new SocketServer();
  }
}
