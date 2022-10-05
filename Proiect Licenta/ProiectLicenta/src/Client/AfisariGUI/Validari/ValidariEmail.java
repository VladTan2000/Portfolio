package Client.AfisariGUI.Validari;

import javax.swing.*;

public class ValidariEmail extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        JTextField textField=(JTextField) input;
            if(textField.getText().length()<3){
                JOptionPane.showMessageDialog(null,"Email-ul trebuie sa fie mai lung de 3 caractere","Eroare",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if(!(textField.getText().contains("@"))){
                JOptionPane.showMessageDialog(null,"Email-ul trebuie sa contina un @","Eroare",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;

    }
}
