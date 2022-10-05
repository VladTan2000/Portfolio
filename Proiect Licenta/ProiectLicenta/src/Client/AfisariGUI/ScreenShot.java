package Client.AfisariGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class ScreenShot extends JFrame {
    private JPanel panel;
    private JLabel jLable;

    private Socket socket;

    private BufferedReader reader;

    private PrintWriter writer;

    private BufferedImage image;

    private String user;

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
    public void populareSwing(){
        SwingWorker populare=new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    writer.println("screenCapture");
                    writer.println(user);
                    if("Eroare".equals(reader.readLine())){
                        JOptionPane.showMessageDialog(null,"A aparut o problema cu conectarea la utilizator, va rugam incercati mai tarziu","Eroare",JOptionPane.ERROR_MESSAGE);
                        dispose();
                        deconectareSwing();
                        return null;

                    }
                        ByteArrayOutputStream baos = null;
                        ByteArrayInputStream bais = null;
                        DataInputStream is = new DataInputStream(socket.getInputStream());
                        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                        int size = is.readInt();
                        baos = new ByteArrayOutputStream(size);
                        byte[] buffer = new byte[1024];
                        int bytesRead = 0;
                        int bytesIn = 0;
                        while (bytesRead < size) {
                            bytesIn = is.read(buffer);
                            bytesRead += bytesIn;
                            baos.write(buffer, 0, bytesIn);
                        }
                        baos.close();
                        bais = new ByteArrayInputStream(baos.toByteArray());
                        image = ImageIO.read(bais);
                        bais.close();
                        publish(image);


            }catch (Exception ignored){

                }
                return null;
            }

            @Override
            protected void process(List chunks) {
                ImageIcon imageIcon=new ImageIcon(image);
                jLable.setIcon(imageIcon);
            }
        };
        populare.execute();
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


    ScreenShot(String user) {
        this.user=user;
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conectareSwing();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                deconectareSwing();
            }
        });
        this.pack();
        this.setVisible(true);
        JOptionPane.showMessageDialog(null,"Va rog asteptati pana imaginea se incarca","Atentie",JOptionPane.INFORMATION_MESSAGE);
        populareSwing();
    }
}
