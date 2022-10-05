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

public class DefaultPage extends JFrame {

    private JPanel panel;
    private JRadioButton radioButtonInfo;
    private JRadioButton radioButtonListaProcese;
    private JRadioButton radioButtonAplicat;
    private JRadioButton radioButtonScreenshot;
    private JButton executeButton;
    private JRadioButton radioButtonIstoricChrome;
    private JTable tableListaAngajati;
    private JScrollPane scrollList;
    private JButton reactualizareButton;
    private DefaultTableModel tableModel;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private String user;

    private String departament;


    private void conectare(){
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

    void populareSefSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                tableModel = new DefaultTableModel();
                String[] columns = {"ID Angajat", "Nume Angajat", "Prenume Angajat", "Email", "Telefon", "Utilizator","Calculator" ,"Deartament"};
                tableModel.setColumnIdentifiers(columns);
                writer.println("whoamISef");
                writer.println(departament);
                String comanda = reader.readLine();
                int size = Integer.parseInt(comanda);
                if (size == 0) {
                    JOptionPane.showMessageDialog(null, "A aparut o eroare cu conectarea aplicatiei de luarea datelor dumneavoastra la server, va rugam reincercati", "Eroare", JOptionPane.DEFAULT_OPTION);
                    System.exit(22);
                }
                String[][] date = new String[size][8];
                for (int i = 0; i < size; i++) {
                    comanda = reader.readLine();
                    String[] splits = comanda.split("~");
                    System.arraycopy(splits,0,date[i],0,splits.length);
                    tableModel.addRow(date[i]);
                }
                publish(tableModel);
                return null;
            }

            @Override
            protected void process(List chunks) {
                tableListaAngajati.setModel(tableModel);
            }
        };
        swingWorker.execute();
    }

    void populareAngSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                tableModel=new DefaultTableModel();
                String[] columns = {"ID Angajat", "Nume Angajat", "Prenume Angajat", "Email", "Telefon", "Utilizator","Calculator", "Deartament"};
                tableModel.setColumnIdentifiers(columns);
                writer.println("whoamIAngj");
                writer.println(user);
                String[][] date = new String[1][8];
                String comanda = reader.readLine();
                if ("Eroare".equalsIgnoreCase(comanda)) {
                    JOptionPane.showMessageDialog(null, "A aparut o eroare cu conectarea aplicatiei de luarea datelor dumneavoastra la server, va rugam reincercati", "Eroare", JOptionPane.ERROR_MESSAGE);
                    System.exit(22);
                } else {
                    String[] splits = comanda.split("~");
                    System.arraycopy(splits,0,date[0],0,splits.length);
                    tableModel.addRow(date[0]);

                }
                publish(tableModel);
                return null;
            }

            @Override
            protected void process(List chunks) {
                tableListaAngajati.setModel(tableModel);
            }
        };

        swingWorker.execute();

    }

    void populareAdminSwing() {
        SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                tableModel = new DefaultTableModel();
                String[] columns = {"ID Angajat", "Nume Angajat", "Prenume Angajat", "Email", "Telefon", "Utilizator","Calculator" ,"Deartament"};
                tableModel.setColumnIdentifiers(columns);
                writer.println("whoamI");
                String comanda = reader.readLine();
                int size = Integer.parseInt(comanda);
                String[][] date = new String[size][8];
                for (int i = 0; i < size; i++) {
                    comanda = reader.readLine();
                    String[] splits = comanda.split("~");
                    System.arraycopy(splits,0,date[i],0,splits.length);
                    tableModel.addRow(date[i]);

                }
                publish(tableModel);
                return null;
            }

            @Override
            protected void process(List chunks) {
                tableListaAngajati.setModel(tableModel);
            }
        };
        swingWorker.execute();
        try {
            swingWorker.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    void populareSwing(int a){
        switch (a){
            case 1:populareSefSwing();break;
            case 2:populareAdminSwing();break;
            default:populareAngSwing();break;
        }
    }

    DefaultPage(int a,String user,String departament) {
        this.user=user;
        this.departament=departament;
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        conectare();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        this.pack();
        this.setVisible(true);
        populareSwing(a);
        if(a==0){reactualizareButton.setVisible(false);}
        else {
            reactualizareButton.addActionListener(e -> {

                populareSwing(a);
            });
        }
        executeButton.addActionListener(e -> {
            int row = tableListaAngajati.getSelectedRow();
            if(row==-1){
                JOptionPane.showMessageDialog(null, "Va rugam sa selectati un rand", "Eroare", JOptionPane.ERROR_MESSAGE);
                return;
            }
                String user_s = tableListaAngajati.getValueAt(row, 6).toString();

                if (radioButtonAplicat.isSelected()) {
                    new ListaAplicatii(user_s,a);
                } else if (radioButtonListaProcese.isSelected()) {
                    new ListaProcese(user_s,a);
                } else if (radioButtonInfo.isSelected()) {
                    new InfoClient(user_s,a);
                } else if (radioButtonScreenshot.isSelected()) {
                    new ScreenShot(user_s);
                } else if (radioButtonIstoricChrome.isSelected()) {
                    new HistoryPage(user_s);
                }
        });


    }
}
