/**
 * The GUI that allows clients to interact
 * 
 * @author Stefan van Deventer and Simone van Zyl
 * @version 0.0.2
 * @since 2018-09-26
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.awt.event.*;
import javax.swing.JOptionPane;

public class Client_GUI extends javax.swing.JFrame {

	private static Client client;
	public static String username = "Anonymous";
	public static String hostname;

	// Variables declaration - for GUI form
	public static javax.swing.JList<String> List_online;
	private javax.swing.JButton btn_connect;
	private javax.swing.JButton btn_disconnect;
	private javax.swing.JButton btn_help;
	private javax.swing.JButton btn_send;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JLabel label_name;
	public static javax.swing.JTextField my_text_field;
	public static javax.swing.JTextArea public_Chat_TArea;
	// End of variables declaration - for GUI form

	/**
	 * Creates new form Client_GUI
	 */
	public Client_GUI() {
		initComponents();
	}

	/**
	 * Connection method to connect the new client to the server.
	 * The server validates the nickname chosen and sends a response here where if the nickname failed the
	 * user has to change the nickname.
	 * A new thread is started for each client.
	 */
	public void Connect() {
		try {
			System.out.println("Connecting...");
			final int port = 8000;
			Socket sock = new Socket(hostname, port);
			System.out.println("Connected to host");
			client = new Client(sock);

			ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(sock.getInputStream());
			output.writeUTF(username);
			output.flush();

			boolean exit=false;
			/*Check if the nickname is valid i.e. not in use at the moment*/
			while (!exit) {
				String nickname = input.readUTF();
				if (nickname.equals("Nickname failed")) {
					sock.close();
					output.close();
					input.close();
					sock = new Socket(hostname, port);
					client = new Client(sock);
					output = new ObjectOutputStream(sock.getOutputStream());
					input = new ObjectInputStream(sock.getInputStream());

					username = JOptionPane.showInputDialog("Nickname already in use, please try another one: ");
					output.writeUTF(username);
					output.flush();
				} else if (nickname.equals("Nickname accepted")) {
					exit = true;

					label_name.setText(username);
					btn_send.setEnabled(true);
					btn_disconnect.setEnabled(true);
					btn_connect.setEnabled(false);
				}
			}
			my_text_field.setEditable(true);

			Thread x = new Thread(client);
			x.start();
		} catch (UnknownHostException err) {
			System.out.println("Error hostname");
		} catch (Exception e) {
			System.out.println("Error: "+e);
			e.printStackTrace();
		}

	}

	/**
	 * Checks that the client is trying to connect to a valid server before acctually trying to connect
	 * to it. Done to avoid problems with the connection.
	 */
	public boolean checkConnectionToHost() {
		try (Socket s = new Socket(hostname, 8000)) {
			return true;
		} catch (IOException e) {
			System.out.println("Host problem");
			return false;
		}
	}

	private void btn_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_connectActionPerformed
		if (checkConnectionToHost()) {
			username = JOptionPane.showInputDialog("Nickname: ");
			if (!username.equals("")) {
				Connect();
			} else {
				JOptionPane.showMessageDialog(null, "Please enter a Nickname");
			}
		}
	}//GEN-LAST:event_btn_connectActionPerformed

	private void btn_disconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_disconnectActionPerformed
		try{
			client.Disconnect();
			username = "Anonymous";
			label_name.setText("Anonymous");
			btn_send.setEnabled(false);
			btn_disconnect.setEnabled(false);
			btn_connect.setEnabled(true);
		} catch (Exception e) {
			System.out.println("Error: "+e);

		}
	}//GEN-LAST:event_btn_disconnectActionPerformed

	private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
		if (!my_text_field.getText().equals("")) {
			client.send(my_text_field.getText());
			my_text_field.requestFocus();
		}
	}//GEN-LAST:event_btn_sendActionPerformed

	private void btn_helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_helpActionPerformed
		JOptionPane.showMessageDialog(null, "How to use the chatroom:\n"+
				"1. To connect to the Chat, click the 'Connect' button and choose a nickname.\n"+
				"2. The big white message box is the Chat. All messages sent to you will be displayed there.\n"+
				"3. The long verticle box on the right is the 'online users' list. It shows who is currently in the Chat\n"+
				"4. The horisontal box below is your message box. Type a message and send it by clicking the 'send' button\n"+
				"    or by pressing the enter key.\n"+
				"5. If you whish to speak privately to one of the online users, use the command '/whisper <username>-<message>'\n"+
				"    in the message box. Only the person you whisper to will receive the message.\n"+
				"6. To leave the Chat click on the 'disconnect' button");

	}//GEN-LAST:event_btn_helpActionPerformed    

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		hostname = args[0];
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Client_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Client_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Client_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Client_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Client_GUI().setVisible(true);
			}
		});
	}

	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        public_Chat_TArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        label_name = new javax.swing.JLabel();
        btn_connect = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        List_online = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        btn_disconnect = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        my_text_field = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        btn_help = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        public_Chat_TArea.setEditable(false);
        public_Chat_TArea.setColumns(20);
        public_Chat_TArea.setLineWrap(true);
        public_Chat_TArea.setRows(5);
        jScrollPane1.setViewportView(public_Chat_TArea);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        jLabel1.setText("Chat");

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        jLabel2.setText("Signed in as: ");

        label_name.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        label_name.setText("Anonamous");

        btn_connect.setBackground(new java.awt.Color(11, 0, 255));
        btn_connect.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        btn_connect.setText("CONNECT");
        btn_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_connectActionPerformed(evt);
            }
        });

        List_online.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        jScrollPane2.setViewportView(List_online);

        jLabel3.setText("Online users:");

        btn_disconnect.setBackground(new java.awt.Color(11, 0, 255));
        btn_disconnect.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        btn_disconnect.setText("DISCONNECT");
        btn_disconnect.setEnabled(false);
        btn_disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_disconnectActionPerformed(evt);
            }
        });

        jLabel4.setText("Send a message:");

        my_text_field.setEditable(false);

        btn_send.setBackground(new java.awt.Color(11, 0, 255));
        btn_send.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        btn_send.setText("Send");
        btn_send.setEnabled(false);
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

		my_text_field.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!my_text_field.getText().equals("")) {
            			client.send(my_text_field.getText());
      			      	my_text_field.requestFocus();
       				 }
				}
			}

			@Override
			public void keyReleased(KeyEvent e){

			}
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!my_text_field.getText().equals("")) {
            			client.send(my_text_field.getText());
      			      	my_text_field.requestFocus();
       				 }
				}
			}
		
		});

        btn_help.setBackground(new java.awt.Color(11, 0, 255));
        btn_help.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        btn_help.setText("Help");
	btn_help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_helpActionPerformed(evt);
            }
        });


        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(my_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(75, 75, 75))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_help, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(236, 236, 236)
                        .addComponent(btn_connect, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_disconnect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(label_name)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(4, 4, 4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(label_name))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_connect)
                            .addComponent(btn_disconnect))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(my_text_field, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_help))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

}
