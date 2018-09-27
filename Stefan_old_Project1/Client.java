/**
 * A runnable
 * 
 * @author Stefan van Deventer and Simone van Zyl
 * @version 0.0.2
 * @since 2018-09-26
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.net.InetAddress;
import java.util.Map;
import javax.swing.JOptionPane;

public class Client implements Runnable {
	public static Socket sock;
	public static ObjectInputStream input;
	public static ObjectOutputStream out;

	public Client(Socket x) {
		this.sock = x;
	}

	public void run() {
		try {
			try {
				input = new ObjectInputStream(sock.getInputStream());
				out = new ObjectOutputStream(sock.getOutputStream());
				out.flush();
				receive();

			} finally {
				sock.close();
			}
		} catch (Exception e) {
			System.out.println("No, Groot! You can't! You'll die! " + 
					"Why are you doing this? Why?");

		}
	}

	/**
	 * The receive method has an infinite loop which checks if there is a new message to receive at all times.
	 * This is run by the clients thread so to make it run permanently even when other operations/actions
	 * happen to the client. It also checks the message to see what type of message it is i.e. if it is a server message
	 * or if it is a chat message.
	 */
	public static void receive() {
		while (true) {
			try {
			String msg = input.readUTF();
			if (!msg.substring(0,3).equals("#/@")) {
				Client_GUI.public_Chat_TArea.append(msg + "\n");
			} else {
				populateOnline(msg);
			}
			} catch (IOException e) {
				System.out.println("OMG you killed Kenny, You bastards.");
			}
		}
	}


	/**
	 * The send method is called whenever a client wants to send a message. It is called from outside the clients thread
	 * so that the client can receive a message even while sending its own message.
	 */
	public static void send(String msg) {
		try {
		if (sock.isClosed()) {
			Client_GUI.public_Chat_TArea.append("Server not responding.");
		}

		out.writeUTF(Client_GUI.username + ": " + msg);
		out.flush();
		Client_GUI.my_text_field.setText("");
		} catch(IOException e) {
			System.out.println("Stan: Oh my god. Jay Leno's chin killed Kenny.\n" + 
					"Kyle: You bastard.\n" + 
					"Jay Leno: Ah, who cares? He dies every episode.");
		}
	}

	/**
	 * The disconnect method disconnects the client and closes its socket. The message is interpreted by the server to 
	 * notify all other clients that this client has disconnected.
	 */
	public static void Disconnect() throws IOException {
		out.writeUTF(Client_GUI.username + ": has disconnected.");
		out.flush();
		out.close();
		sock.close();
		JOptionPane.showMessageDialog(null, "You have disconnected.");
		System.exit(0);
	}

	/**
	 * This method populates the online users list if the server sent a message requiring the client to do so.
	 */
	public static void populateOnline(String input) {
		String temp = input.substring(3);
		temp = temp.replace("[", "");
		temp = temp.replace("]", "");
		String[] onlineArr = temp.split(", ");
		Client_GUI.List_online.setListData(onlineArr);
	}

}
