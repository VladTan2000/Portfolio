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

public class HistoryPage extends JFrame {
    private JPanel panel;
    private JScrollPane scrollList;
    private JTable tableHistory;
    private JButton refreshButton;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private final String user;

    private DefaultTableModel tableModel;


    HistoryPage(String user){

        this.user=user;
        this.setContentPane(this.panel);
        conectare();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.pack();
        this.setVisible(true);
        populareSwing();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        refreshButton.addActionListener(e -> populareSwing());


    }



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
    private void populareSwing(){
        SwingWorker populare=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                refreshButton.setEnabled(false);
                    tableModel=new DefaultTableModel();
                    String[] coloane={"URL","Titlu"};
                    tableModel.setColumnIdentifiers(coloane);
                    writer.println("history");
                    writer.println(user);
                    String comanda=reader.readLine();
                    if("Eroare".equals(comanda)){
                        JOptionPane.showMessageDialog(null,"A aparut o eroare cu conectarea cu utilizatorul, va rugam incercati mai tarziu","Eraore",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                        return null;
                    }
                    int size = Integer.parseInt(comanda);
                    String[][] data = new String[size][2];
                    for (int i = 0; i < size; i++) {
                        comanda = reader.readLine();
                        String[] splits=comanda.split("\\|");
                        System.arraycopy(splits, 0, data[i], 0, 2);
                        tableModel.addRow(data[i]);
                    }
                    publish(tableModel);

                return null; }


            @Override
            protected void process(List chunks) {
                tableHistory.setModel(tableModel);
                refreshButton.setEnabled(true);
            }
        };
        populare.execute();
    }

}


