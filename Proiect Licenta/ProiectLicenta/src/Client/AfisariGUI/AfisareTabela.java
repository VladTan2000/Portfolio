package Client.AfisariGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

public class AfisareTabela extends JFrame {
    private JTable tableAngajati;
    private JButton refreshButton;
    private JPanel panel;
    private JScrollPane scrollList;
    private JButton updateButton;
    private JButton deleteButton;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private DefaultTableModel tableModel;
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

    private void populareSwing(){
        SwingWorker workSef = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception  {
                    deleteButton.setEnabled(false);
                    updateButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                    tableModel=new DefaultTableModel();
                    String[] columns = {"idAngajat", "numeAngajat", "prenumeAngajat", "email", "telefon", "departament", "esteSef","utilizator","parola","calculator","ssh_calculator"};
                    tableModel.setColumnIdentifiers(columns);
                    writer.println("afisareTable");
                    String comanda = reader.readLine();
                    int size = Integer.parseInt(comanda);
                    if(size==0){
                        JOptionPane.showConfirmDialog(null, "A aparut o eroare cu conectarea aplicatiei la baza de date, va rugam reincercati", "Eroare", JOptionPane.DEFAULT_OPTION);
                        System.exit(22);
                    }
                    String[][] date = new String[size][11];
                    for (int i = 0; i < size; i++) {
                        comanda = reader.readLine();
                        String[] splits = comanda.split("~");
                        System.arraycopy(splits, 0, date[i], 0, 11);
                        tableModel.addRow(date[i]);
                    }

                    publish(tableModel);

                return null;
            }

            @Override
            protected void process(List chunks) {
                tableAngajati.setModel(tableModel);
                deleteButton.setEnabled(true);
                updateButton.setEnabled(true);
                refreshButton.setEnabled(true);
            }

        };

        workSef.execute();
    }

    private void deleteSwing(int id){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                writer.println("deleteAngj");
                writer.println(id);
                JOptionPane.showMessageDialog(null,"Angajat sters cu succes","Succes",JOptionPane.DEFAULT_OPTION);
                return null;
            }
        };
        swingWorker.execute();
    }

    AfisareTabela(String a){
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        conectareSwing();
        this.pack();
        this.setVisible(true);
        populareSwing();
        refreshButton.addActionListener(e->populareSwing());
        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row=tableAngajati.getSelectedRow();
                int column=0;
                if(row==-1){
                    JOptionPane.showMessageDialog(null,"Va rog selectati un rand","Eroare",JOptionPane.ERROR_MESSAGE);
                }else {
                    int id = Integer.parseInt(tableAngajati.getValueAt(row, column).toString());
                    deleteSwing(id);
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });

        updateButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row=tableAngajati.getSelectedRow();
                int column=0;
                if(row==-1){
                    JOptionPane.showMessageDialog(null,"Va rog selectati un rand","Eroare",JOptionPane.ERROR_MESSAGE);
                }else {
                    int id = Integer.parseInt(tableAngajati.getValueAt(row, column).toString());
                    new UpdateTable(false,id,a);
                }
            }
        });


    }
}
