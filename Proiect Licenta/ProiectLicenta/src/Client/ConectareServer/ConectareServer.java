package Client.ConectareServer;

import Server.ComenziCMD.ComenziInfo.ComenziInfo;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ConectareServer {

    public static void main(String[] args) {
        int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
        String host = ResourceBundle.getBundle("settings").getString("host");
        while (true) {
            try (Socket socket = new Socket(host, port);
                 PrintWriter printwritter = new PrintWriter(socket.getOutputStream(), true)) {
                    System.out.println("Conectare Server");
                    printwritter.println("App");
                    printwritter.println(new ComenziInfo().getComandaNumeUser());
                    TimeUnit.MINUTES.sleep(5);

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
    }

}