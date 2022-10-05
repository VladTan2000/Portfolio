package Client.AfisariGUI;

import Client.AfisariGUI.Validari.ValidariEmail;
import Client.AfisariGUI.Validari.ValidariLungime;
import Client.AfisariGUI.Validari.ValidariTelefon;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class InfoClient extends JFrame{
    private JTextField textFieldLocatie;
    private JTextField textFieldIPExtern;
    private JPanel panel;
    private JTextField textFieldID;
    private JTextField textFieldNume;
    private JTextField textFieldPrenume;
    private JTextField textFieldEmail;
    private JTextField textFieldTelefon;
    private JTextField textFieldSSH;
    private JTextField textFieldCalculator;
    private JButton updateButton;
    private JButton refreshButton;
    private JTextField textFieldDepartament;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private String user;

    private static String[] lista;
    private StringWriter writer1;

    private void conectareSwing(){
        SwingWorker<Void,String> conectare=new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
                    String host = ResourceBundle.getBundle("settings").getString("host");
                    socket = new Socket(host,port);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    writer.println("GUI");

                } catch (IOException e) {
                    publish("Canceled");

                }


                return null;}

            @Override
            protected void process(List<String> chunks) {
                JOptionPane.showMessageDialog(null, "A aparut o eroare le server, va rugam incercati mai tarziu", "Eroare", JOptionPane.ERROR_MESSAGE);
                dispose();
            }
        };
        conectare.execute();
        try {
            conectare.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void updateSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                updateButton.setEnabled(false);
                refreshButton.setEnabled(false);
                writer.println("updateIntroducereDate");
                writer.println(textFieldNume.getText());
                writer.println(textFieldPrenume.getText());
                writer.println(textFieldEmail.getText());
                writer.println(textFieldTelefon.getText());
                writer.println(textFieldID.getText());
                JOptionPane.showConfirmDialog(null, "Inserare cu Success", "Succes", JOptionPane.DEFAULT_OPTION);
                updateButton.setEnabled(true);
                refreshButton.setEnabled(true);

                return null;
            }
        };
        swingWorker.execute();
    }
    private void populareSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() {
                try {
                    updateButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                    writer1 = new StringWriter();
                    writer.println("locatie");
                    writer.println(user);
                    String raspuns=reader.readLine();
                    if("Eroare".equals(raspuns)){
                        JOptionPane.showMessageDialog(null,"A aparut o prblema cu conectarea la utilizator, va rugam incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                        return null;
                    }
                    writer1.append(raspuns);
                    writer1.append("~");
                    writer.println("ipExtern");
                    writer.println(user);
                    raspuns=reader.readLine();
                    if("Eroare".equals(raspuns)){
                        JOptionPane.showMessageDialog(null,"A aparut o prblema cu conectarea la utilizator, va rugam incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                        return null;
                    }
                    writer1.append(raspuns);
                    writer1.append("~");
                    writer.println("updateCerereDate");
                    writer.println(user);
                    writer1.append(reader.readLine());


                    publish(writer1);
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void process(List chunks) {
                String[] comanda=writer1.toString().split("~");
                textFieldLocatie.setText(comanda[0]);
                textFieldLocatie.setEditable(false);
                textFieldIPExtern.setText(comanda[1]);
                textFieldIPExtern.setEditable(false);
                textFieldID.setText(comanda[2]);
                textFieldID.setEditable(false);
                textFieldNume.setText(comanda[3]);
                textFieldPrenume.setText(comanda[4]);
                textFieldEmail.setText(comanda[5]);
                textFieldTelefon.setText(comanda[6]);
                textFieldSSH.setText(comanda[7]);
                textFieldSSH.setEditable(false);
                textFieldCalculator.setText(comanda[8]);
                textFieldCalculator.setEditable(false);
                textFieldDepartament.setText(comanda[9]);
                textFieldDepartament.setEditable(false);
                updateButton.setEnabled(true);
                refreshButton.setEnabled(true);

            }
        };
        swingWorker.execute();
    }

    void deconectareSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("exit");
                reader.close();
                return null;
            }
        };
        swingWorker.execute();
        try{swingWorker.get();} catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    InfoClient(String user,int a) {
        this.user=user;
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textFieldEmail.setInputVerifier(new ValidariEmail());
        textFieldNume.setInputVerifier(new ValidariLungime());
        textFieldPrenume.setInputVerifier(new ValidariLungime());
        textFieldTelefon.setInputVerifier(new ValidariTelefon());
        conectareSwing();
        this.pack();
        this.setVisible(true);
        populareSwing();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        refreshButton.addActionListener(e-> {
            populareSwing();
            });
        if(a>0){updateButton.setVisible(true);
            updateButton.addActionListener(e -> {
                updateSwing();
            });
            return;
        }
        updateButton.setVisible(false);





    }
}
