package Client.AfisariGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

public class ListaAplicatii extends JFrame {

    private final String user;
    private JPanel panel1;
    private JScrollPane scrollList;
    private JButton dezinstaleazaButton;
    private JTable tableAplicatii;
    private JButton refreshButton;
    private DefaultTableModel tableModel;

    private Socket socket;

    private BufferedReader reader;
    private PrintWriter writer;

    ListaAplicatii(String user,int a) {


        this.user = user;
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
        refreshButton.addActionListener(e->{
            populareSwing();
        });
        if(a>0){
            dezinstaleazaButton.setVisible(true);
            dezinstaleazaButton.addActionListener(e -> {
                dezinstareSwing();
            });
        return;}
        dezinstaleazaButton.setVisible(false);


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
    private void populareSwing() {
        SwingWorker populare = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                    refreshButton.setEnabled(false);
                    dezinstaleazaButton.setEnabled(false);
                    tableModel=new DefaultTableModel();
                    String[] coloane = {"Nume", "Versiunea", "Dezvoltator", "Locatie Instalare"};
                    tableModel.setColumnIdentifiers(coloane);
                    writer.println("listaAplicatii");
                    writer.println(user);
                    String comanda = reader.readLine();
                    if("Eroare".equals(comanda)){
                        JOptionPane.showMessageDialog(null,"A aparut o eroare la conectarea cu utilizatorul, va rugam incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                        return null;
                    }
                    int size = Integer.parseInt(comanda);
                    String[][] data = new String[size / 4][4];
                    for (int i = 0; i < size / 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            comanda = reader.readLine();
                            comanda = comanda.substring(17);
                            data[i][j] = comanda;
                        }
                        tableModel.addRow(data[i]);


                }
                publish(tableModel);
                return null;
            }

            @Override
            protected void process(List chunks) {
                tableAplicatii.setModel(tableModel);
                refreshButton.setEnabled(true);
                dezinstaleazaButton.setEnabled(true);
            }
        };
        populare.execute();

    }

    private void dezinstareSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                int row=tableAplicatii.getSelectedRow();
                if(row==-1){
                    JOptionPane.showConfirmDialog(null, "Va rugam sa selectati un rand", "Eroare", JOptionPane.DEFAULT_OPTION);
                }
                else{
                    dezinstaleazaButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                    String app=(String) tableAplicatii.getValueAt(row,0);
                    writer.println("dezinstaleazaAplicatie");
                    writer.println(user);
                    writer.println(app);
                    if("Eroare".equals(reader.readLine())){
                        JOptionPane.showMessageDialog(null,"A aparut o eroare cu conectarea la utilizator, va rog incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                        return null;
                    }
                    dezinstaleazaButton.setEnabled(true);
                    refreshButton.setEnabled(true);
                }
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


}

