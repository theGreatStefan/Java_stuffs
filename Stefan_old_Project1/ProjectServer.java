/**
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

public class ProjectServer extends Thread {

	/* CONNECTIONS<username,Socket> -- for the Servers communication
	 * to clients ONLY*/
	public static HashMap<String, Socket> CONNECTIONS = new HashMap<String, Socket>();
	/* USERS <usernames, IP addresses> can be requested by other clients */
	public static HashMap<String, String> USERS = new HashMap<String, String>();
	public static boolean exit = false;
	
	private static final String TEMPKEY = "" + Math.random();

	/*public void run() {
		try {
			ObjectInputStream INPUT = new ObjectInputStream((System.in));
			String str = INPUT.readUTF();
			if (str.equals("exit")) {
				exit = true;
				Thread.sleep(500); // XXX
				Socket exitSock = new Socket("127.0.0.1", 8000);
				System.out.println("exit time");
			}
		} catch (IOException e) {
			System.out.println("Problem with input");
		} catch (InterruptedException err) {
			System.out.println("Problem sleeping");
		}

	}*/

	public static void main(String[] args) {
		System.out.println("Starting server...");
		//(new ProjectServer()).start();

		int portNumber = 8000;
		System.out.println("Initialising Sockets");

		try {
			ServerSocket serverSocket = new ServerSocket(portNumber);

			/*Get new clients*/
			while (true) {
				Socket clientSocket = serverSocket.accept();
				if (clientSocket != null) {
					ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

					/*Username already in use*/
					int valid = AddUsername(clientSocket);
					if (valid == 0) {
						CONNECTIONS.remove(clientSocket);
						out.writeUTF("Nickname failed");
						out.flush();
						System.out.println("Nickname failed");
					} else if (valid == 1) {
						out.writeUTF("Nickname accepted");
						out.flush();

						System.out.println("Client connected");
						Chat_server CHAT = new Chat_server(clientSocket);
						Thread x = new Thread(CHAT);
						x.start();
					} else if (valid == -1) {
						CONNECTIONS.remove(clientSocket);
					}
				}

			}
		} catch (IOException e) {
			System.out.println("There is an IOException in main "+e);
		}
		System.exit(0);
	}

	/**
	 * Adds the username to the list of usernames if it is valid or returns -1 if the client is only testing the 
	 * connection.
	 **/
	public static int AddUsername(Socket x) throws IOException {
		ObjectInputStream input = new ObjectInputStream(x.getInputStream());
		
		String username = input.readUTF();
		if (!checkUsername(username)) {
			return 0;
		}

		CONNECTIONS.put(username, x);
		
		ObjectOutputStream out = new ObjectOutputStream(x.getOutputStream());
		
		USERS.put(username, x.getInetAddress().getHostName());
		
		/* TODO later:
		for (int i=1; i <= Connections.size(); i++) {
			Socket temp = (Socket) Connections.get(i-1);
			PrintWriter tempout = new PrintWriter(temp.getOutputStream());
			tempout.println("#/@"+CurrUsers);
			tempout.flush();
			tempout.println(username+" has joined the chat.");
			tempout.flush();
		}*/
		return 1;

	}

	/**
	 * Removes a username from the CurrUsers array list and the corresponding socket from the Connections array list
	 * and then sends an updated list to all other clients to notify them that there was a change in the online user
	 * list.
	 */
	public static void RemoveUsername(String username) throws IOException {
		USERS.remove(username);
		CONNECTIONS.remove(username);
		
		/*
		 * @see
		 * https://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap 
		 */
		for (Map.Entry<String, Socket> entry : CONNECTIONS.entrySet()) {
			Socket temp = (Socket) entry.getValue();
			ObjectOutputStream tempout = new ObjectOutputStream(temp.getOutputStream());
			tempout.writeUTF("#/@"+USERS.toString());
			tempout.flush();
		}
	}

	/**
	 * Checks through the list of usernames already in use and then returns true if the username is available or -1 if not.
	 * Done because the chat room does not support duplicate nicknames.
	 **/
	public static boolean checkUsername(String name) {
		if (USERS.containsKey(name)) {
			return false;
		} else {
			return true;
		}

	}

}
