package Client.AfisariGUI;

import Client.AfisariGUI.Validari.ValidariLungime;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class VerificareParola extends JFrame {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean parolaCorecta;
    private boolean esteSef;
    private JButton buttonVerificare;
    private JTextField textFieldUtilizator;
    private JPasswordField passwordField;
    private JPanel panel;

    private String calculator;

    private String departament;

    void deconectareSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() {
                try {
                    writer.println("exit");
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };
        swingWorker.execute();
        try{swingWorker.get();} catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean esteSefSwing(String user) throws ExecutionException, InterruptedException {
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("verificareSef");
                writer.println(user);
                Integer comanda = Integer.parseInt(reader.readLine());
                if(comanda==1){esteSef=true;}
                return esteSef;
            }
        };
        swingWorker.execute();
        return (boolean) swingWorker.get();
    }



    private boolean VerificareBDSwing(String user,String parola) throws ExecutionException, InterruptedException {
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("verificareParola");
                writer.println(user);
                writer.println(parola);
                parolaCorecta = Boolean.parseBoolean(reader.readLine());
                publish(parolaCorecta);
                return parolaCorecta;
            }

        };
        swingWorker.execute();
        return (boolean) swingWorker.get();
    }

    private void SelectUtilizator(String calculator_o){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("selectUtilizator");
                writer.println(calculator_o);
                calculator=reader.readLine();
                publish(calculator);
                return null;
            }


        };

        swingWorker.execute();
        try {
            swingWorker.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    private void SelectDepartament(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("selectDepartament");
                writer.println(calculator);
                departament=reader.readLine();
                publish(departament);
                return null;
            }
        };
        swingWorker.execute();
        try {
            swingWorker.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

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


    VerificareParola() {
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        conectareSwing();
        textFieldUtilizator.setInputVerifier(new ValidariLungime());
        passwordField.setInputVerifier(new ValidariLungime());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        buttonVerificare.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ("admin".equalsIgnoreCase(textFieldUtilizator.getText()) && "admin111".equalsIgnoreCase(String.valueOf(passwordField.getPassword()))) {
                        deconectareSwing();
                        dispose();
                        new MeniuAdmin(textFieldUtilizator.getText());
                    } else if (VerificareBDSwing(textFieldUtilizator.getText(), String.valueOf(passwordField.getPassword()))) {

                        if (esteSefSwing(textFieldUtilizator.getText())) {
                            SelectUtilizator(textFieldUtilizator.getText());
                            SelectDepartament();
                            dispose();
                            new DefaultPage(1, calculator, departament);
                            deconectareSwing();
                        } else {
                            dispose();
                            SelectUtilizator(textFieldUtilizator.getText());
                            SelectDepartament();
                            new DefaultPage(0, calculator, departament);
                            deconectareSwing();
                        }
                    } else {
                        JOptionPane.showConfirmDialog(null, "Parola sau username gresite, va rugam reincercati", "Eroare", JOptionPane.DEFAULT_OPTION);
                    }
                }
                catch (ExecutionException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args){

        new VerificareParola();
    }
}
