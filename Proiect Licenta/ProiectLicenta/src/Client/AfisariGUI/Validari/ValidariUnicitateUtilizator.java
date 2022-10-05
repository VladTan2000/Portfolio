package Client.AfisariGUI.Validari;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ValidariUnicitateUtilizator extends InputVerifier {
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    private boolean ok;


    @Override
    public boolean verify(JComponent input) {
        JTextField jTextField=(JTextField)input;
        if(jTextField.getText().length()<3){
            JOptionPane.showMessageDialog(null,"Eroare introduceti un username cu cel putin 4 caractere","Eroare",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
                String host = ResourceBundle.getBundle("settings").getString("host");
                socket = new Socket(host,port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("GUI");
                writer.println("verificareUser");
                writer.println(jTextField.getText());
                String oks=reader.readLine();
                ok= "1".equals(oks);
                writer.println("exit");
                reader.close();
                return null;
            }
        };
        swingWorker.execute();
        try {
            swingWorker.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!ok){
            JOptionPane.showMessageDialog(null,"Eroare va rugam introduceti un username unic","Eroare",JOptionPane.ERROR_MESSAGE);
            return false;
        }

    return ok;}
}
