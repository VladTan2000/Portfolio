package Client.AfisariGUI;

import Client.AfisariGUI.Validari.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class UpdateTable extends JFrame {
    private JTextField textFieldIDAngajat;
    private JTextField textFieldNumeAngajat;
    private JTextField textFieldPrenumeAngajat;
    private JTextField textFieldEmail;
    private JTextField textFieldTelefon;
    private JRadioButton radioButtonNu;
    private JRadioButton radioButtonDa;
    private JComboBox comboBoxDepartamente;
    private JTextField textFieldUtilizator;
    private JTextField textFieldParola;
    private JTextField textFieldCalculator;
    private JButton buttonInsert;
    private JPanel panel;
    private JTextField textFieldSSH_Utilizator;
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private String id;

    private String ultimul_id;


    private void conectareSwing(){

        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground()  {
                try {
                    int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
                    String host = ResourceBundle.getBundle("settings").getString("host");
                    socket = new Socket(host,port);
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer.println("GUI");
                    writer.println("ultimulID");
                    ultimul_id = reader.readLine();


                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "A aparut o eroare le server, va rugam incercati mai tarziu", "Eroare", JOptionPane.ERROR_MESSAGE);
                    dispose();
                }
                return null;
            }
        };

        swingWorker.execute();
        try{
        swingWorker.get();} catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void inserareSwing() {
        SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("insarareTable");
                writer.println(textFieldIDAngajat.getText());
                writer.println(textFieldNumeAngajat.getText());
                writer.println(textFieldPrenumeAngajat.getText());
                writer.println(textFieldEmail.getText());
                writer.println(textFieldTelefon.getText());
                writer.println(comboBoxDepartamente.getSelectedIndex()+1);
                if (radioButtonDa.isSelected()) {
                    writer.println("1");
                } else {
                    writer.println("0");
                }
                writer.println(textFieldUtilizator.getText());
                writer.println(textFieldParola.getText());
                writer.println(textFieldCalculator.getText());
                writer.println(textFieldSSH_Utilizator.getText());
                JOptionPane.showConfirmDialog(null, "Introducere cu Succes", "Succes", JOptionPane.DEFAULT_OPTION);
                dispose();

                return null;
            }


        };
        swingWorker.execute();
    }

    private void updateSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("updateAngj");
                writer.println(textFieldNumeAngajat.getText());
                writer.println(textFieldPrenumeAngajat.getText());
                writer.println(textFieldEmail.getText());
                writer.println(textFieldTelefon.getText());
                writer.println(comboBoxDepartamente.getSelectedIndex()+1);
                if(radioButtonDa.isSelected()){writer.println("1");}
                else{writer.println("0");}
                writer.println(textFieldUtilizator.getText());
                writer.println(textFieldParola.getText());
                writer.println(textFieldCalculator.getText());
                writer.println(textFieldSSH_Utilizator.getText());
                writer.println(textFieldIDAngajat.getText());
                JOptionPane.showConfirmDialog(null,"Update cu succes","Succes",JOptionPane.DEFAULT_OPTION);

                return null;
            }
        };
        swingWorker.execute();
    }

    private void populareSwing(int id){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("selectAngj");
                writer.println(id);
                String result=reader.readLine();
                String[] results=result.split("~");
                textFieldIDAngajat.setText(results[0]);
                textFieldIDAngajat.setEditable(false);
                textFieldNumeAngajat.setText(results[1]);
                textFieldPrenumeAngajat.setText(results[2]);
                textFieldEmail.setText(results[3]);
                textFieldTelefon.setText(results[4]);
                comboBoxDepartamente.setSelectedIndex(Integer.parseInt(results[5])-1);
                if(Integer.parseInt(results[6])==0){radioButtonNu.setSelected(true);}
                else{radioButtonDa.setSelected(true);}
                textFieldUtilizator.setText(results[7]);
                textFieldParola.setText(results[8]);
                textFieldCalculator.setText(results[9]);
                textFieldSSH_Utilizator.setText(results[10]);
                publish(results);
                return null;
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

    UpdateTable(boolean a, int id,String user){
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conectareSwing();
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        comboBoxDepartamente.setInputVerifier(new ValidareDepartamente());
        textFieldNumeAngajat.setInputVerifier(new ValidariLungime());
        textFieldPrenumeAngajat.setInputVerifier(new ValidariLungime());
        textFieldParola.setInputVerifier(new ValidariLungime());
        textFieldEmail.setInputVerifier(new ValidariEmail());
        textFieldTelefon.setInputVerifier(new ValidariTelefon());
        textFieldCalculator.setInputVerifier(new ValidariLungime());
        textFieldSSH_Utilizator.setInputVerifier(new ValidariLungime());
        if(a) {
            int id_i = Integer.parseInt(this.ultimul_id) + 1;
            textFieldIDAngajat.setText(String.valueOf(id_i));
            textFieldIDAngajat.setEditable(false);
            textFieldUtilizator.setInputVerifier(new ValidariUnicitateUtilizator());
            buttonInsert.addActionListener(e -> {
                inserareSwing();
            });
            return;
        }
            populareSwing(id);
            String utilizatorVechi=textFieldUtilizator.getText();
            ValidareSchimbareUtilizator validareSchimbareUtilizator=new ValidareSchimbareUtilizator();
            validareSchimbareUtilizator.setUtilizatorVechi(utilizatorVechi);
            textFieldUtilizator.setInputVerifier(validareSchimbareUtilizator);
            buttonInsert.setText("Update");
            buttonInsert.addActionListener(e->updateSwing());
    }
}
