package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerConnection implements Runnable {
	private Socket server;
	private BufferedReader reader;
	public ServerConnection(Socket server)throws IOException {
		this.server = server;
		this.reader = new BufferedReader(new InputStreamReader(server.getInputStream()));
	}
	@Override
	public void run() {
		try {
		while(true) {
			String serverResponse=reader.readLine();
			if(serverResponse==null) break;
			System.out.println(serverResponse);
		}
		}catch(IOException e) {e.printStackTrace();}
		
	}
	
	
}
