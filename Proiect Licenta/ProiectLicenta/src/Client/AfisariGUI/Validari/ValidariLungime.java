package Client.AfisariGUI.Validari;

import javax.swing.*;

public class ValidariLungime extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        JTextField textField=(JTextField) input;
        if(textField.getText().length()<3){
            JOptionPane.showMessageDialog(null,"Text-ul trebuie sa contina mai mult de 3 caracter","Eroare",JOptionPane.ERROR_MESSAGE);
            return false;
        }
       return true;
    }
}
