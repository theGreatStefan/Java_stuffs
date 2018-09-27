/**
 * The server to interact with Clients
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

public class Chat_server implements Runnable {
	Socket sock;
	private ObjectInputStream INPUT;
	private ObjectOutputStream OUT;
	String MSG = "";

	public Chat_server(Socket x) {
		this.sock = x;
	}

	public void run() {
		try {
			INPUT = new ObjectInputStream(sock.getInputStream());
			OUT = new ObjectOutputStream(sock.getOutputStream());

			OUT.writeUTF("Server: WELCOME TO THE CHAT!!!"+
					"\nTo send a message type in the message box below and click send."+
					"\nAnd as always, don`t be rude!\n"+"   ");
			OUT.flush();

			while (true) {

				MSG = INPUT.readUTF();
				System.out.println("Chat Server received: " + MSG);
				//TODO process the MSG
			}
			//String special = MSG.substring(MSG.indexOf(":")+2,MSG.indexOf(":")+3);

			//System.out.println("Client said: "+ MSG);

			/* Whisper to another user by using the /whisper command */
			/*if (special.equals("/")) {
			  if (MSG.contains("whisper")) {
			  String fromUser = MSG.substring(0,MSG.indexOf(":"));
			  String toUser = MSG.substring(MSG.indexOf(":")+11,MSG.indexOf("-"));
			  if (!ProjectServer.CurrUsers.contains(toUser)) {
			  OUT.println(toUser+ " is not in the chat.");
			  OUT.flush();
			  } else {
			  int i = ProjectServer.CurrUsers.indexOf(toUser);
			  int k = ProjectServer.CurrUsers.indexOf(fromUser);
			  String newMSG = fromUser + " whispered: " + MSG.substring(MSG.indexOf("-")+1);
			  Socket temp1 = (Socket) ProjectServer.Connections.get(i);
			  Socket temp2 = (Socket) ProjectServer.Connections.get(k);
			  PrintWriter temp1_out = new PrintWriter(temp1.getOutputStream());
			  PrintWriter temp2_out = new PrintWriter(temp2.getOutputStream());
			  temp1_out.println(newMSG);
			  temp1_out.flush();
			  temp2_out.println(newMSG);
			  temp2_out.flush();
			  System.out.println("Whisper sent to client: " + i);
			  System.out.println("Whisper sent to client: " + k);

			  }
			  } else {
			  OUT.println("That is not a valid command.");
			  OUT.flush();
			  }
			  } else {
			  if (MSG.contains("has disconnected")) {
			  String user = MSG.substring(0,MSG.indexOf(":"));
			  ProjectServer.RemoveUsername(user);
			  sock.close();
			  MSG = user + " has disconnected.";
			  System.out.println(MSG);
			  }

			  for (int i=1; i <= ProjectServer.Connections.size(); i++) {
			  Socket temp = (Socket) ProjectServer.Connections.get(i-1);
			  PrintWriter temp_out = new PrintWriter(temp.getOutputStream());
			  temp_out.println(MSG);
			  temp_out.flush();
			  }

			  }*/



		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try  {
				sock.close();
			}
			catch (IOException e) {
				System.out.println("Woops! Chat Server could not close a socket.");
			}
		}

	}
}
