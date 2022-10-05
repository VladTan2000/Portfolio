package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket;
	private static List<ClientHandler> clients=new ArrayList<>();
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		ExecutorService executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
		System.out.println("Server Started");
		while(!serverSocket.isClosed()) {
			Socket client=serverSocket.accept();
			ClientHandler clientHanlder=new ClientHandler(client,clients);
			clients.add(clientHanlder);
			executorService.execute(clientHanlder);
			System.out.println("New User Connected");
			}
		}

	@Override
	public void close() throws Exception {
		serverSocket.close();
	}

}
