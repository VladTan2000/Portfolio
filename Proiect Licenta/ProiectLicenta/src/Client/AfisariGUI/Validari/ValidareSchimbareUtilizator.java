package Client.AfisariGUI.Validari;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ValidareSchimbareUtilizator extends InputVerifier {
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    String utilizatorVechi;
    private boolean ok;

    public void setUtilizatorVechi(String utilizatorVechi) {
        this.utilizatorVechi = utilizatorVechi;
    }

    @Override
    public boolean verify(JComponent input) {
        JTextField jTextField=(JTextField)input;
        if(utilizatorVechi.equals(jTextField.getText())){
            return true;
        }
        if(jTextField.getText().length()<3){
            JOptionPane.showMessageDialog(null,"Eroare introduceti un username cu cel putin 4 caractere","Eroare",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        SwingWorker<Void,Void> swingWorker= new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {

                int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
                String host = ResourceBundle.getBundle("settings").getString("host");
                socket = new Socket(host, port);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("GUI");
                writer.println("updateUser");
                writer.println(jTextField.getText());
                String oks = reader.readLine();
                ok = "1".equals(oks);
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

        return true;}
}
