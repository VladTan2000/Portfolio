package Client.AfisariGUI;

import javax.swing.*;

public class MeniuAdmin extends JFrame {
    private JRadioButton vizualizareAdminRadioButton;
    private JRadioButton inserareTableRadioButton;
    private JButton executeButton;
    private JPanel panel;
    private JRadioButton afisareTabelaRadioButton;



    MeniuAdmin(String a){
        this.setContentPane(this.panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        executeButton.addActionListener(e->{
            if(inserareTableRadioButton.isSelected()){
                new UpdateTable(true,0,a);
            }
           else if(vizualizareAdminRadioButton.isSelected()){
                new DefaultPage(2,a,"0");
            }
           else if(afisareTabelaRadioButton.isSelected()){
               new AfisareTabela(a);
            }

            else{
                JOptionPane.showConfirmDialog(null, "Va rugam sa selectati o functie", "Eroare", JOptionPane.DEFAULT_OPTION);
            }
                });
        this.pack();
        this.setVisible(true);
    }
}
