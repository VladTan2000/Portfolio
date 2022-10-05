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

public class ListaProcese extends JFrame {
    private JTable tableProcese;
    private JPanel panel1;
    private JScrollPane scrollList;
    private JButton opresteButton;
    private JButton refreshButton;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private final String user;

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
    private void populareSwing(){
        SwingWorker populare=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                    opresteButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                    tableModel=new DefaultTableModel();
                    String[] coloane={"Nume","ID","Timp Incepere"};
                    tableModel.setColumnIdentifiers(coloane);
                    writer.println("listaProcese");
                    writer.println(user);
                    String comanda = reader.readLine();
                    if("Eroare".equals(comanda)){
                        JOptionPane.showMessageDialog(null,"A aparut o eroare cu conectarea la utilizator, va rugam incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                        return null;
                    }
                    int size = Integer.parseInt(comanda);
                    String[][] data = new String[size/3][3];
                    for (int i = 0; i < size/3; i++) {
                        for(int j=0;j<3;j++) {
                            comanda=reader.readLine();
                            comanda = comanda.substring(11);
                            data[i][j] = comanda;
                        }
                        tableModel.addRow(data[i]);
                        publish(tableModel);
                    }

                return null;
            }

            @Override
            protected void process(List chunks) {
                tableProcese.setModel(tableModel);
                opresteButton.setEnabled(true);
                refreshButton.setEnabled(true);
            }
        };

        populare.execute();
    }
    private void oprireProcesSwing(){
        SwingWorker swingWorker=new SwingWorker() {
            @Override
            protected Object doInBackground() throws IOException {
                int row=tableProcese.getSelectedRow();
                if(row==-1){
                    JOptionPane.showConfirmDialog(null, "Va rugam sa selectati un rand", "Eroare", JOptionPane.DEFAULT_OPTION);
                }
                else {
                    opresteButton.setEnabled(false);
                    refreshButton.setEnabled(false);
                    String proces = (String) tableProcese.getValueAt(row, 1);
                    writer.println("stopProcese");
                    writer.println(user);
                    writer.println(proces);
                    if("Eroare".equals(reader.readLine())){
                        JOptionPane.showMessageDialog(null,"A aparut o eroare la oprirea procesului, va rugam incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        deconectareSwing();
                        dispose();
                    }
                    opresteButton.setEnabled(true);
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

    ListaProcese(String user,int a) {
        this.user=user;
        this.setContentPane(this.panel1);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.scrollList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        conectareSwing();
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        tableProcese.removeEditor();
        populareSwing();
        refreshButton.addActionListener(e->{
            populareSwing();
        });
        if(a>0){opresteButton.setVisible(true);
            opresteButton.addActionListener(e -> {
                oprireProcesSwing();

            });
        return;}
        opresteButton.setVisible(false);

    }
}


