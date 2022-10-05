package Client.AfisariGUI.Validari;

import javax.swing.*;

public class ValidareDepartamente extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        JComboBox jComboBox=(JComboBox) input;
        if(jComboBox.getSelectedIndex()<0){
            JOptionPane.showMessageDialog(null,"Alegeti un departament","Eroare",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
