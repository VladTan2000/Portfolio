
package server;

import java.util.ResourceBundle;
import java.util.Scanner;

public class ServerProgram {

	public static void main(String[] args) {
		int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
		try (Server server = new Server(port)) {
			System.out.println(String.format("Server is running on port %d. Type 'exit' to stop", port));
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					if ("exit".equalsIgnoreCase(command)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.exit(0);
		}
	}

}
