package Client.AfisariGUI.Validari;

import javax.swing.*;

public class ValidariTelefon extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        JTextField tf = (JTextField) input;
        if (!(tf.getText().length() == 10)) {
            JOptionPane.showMessageDialog(null, "Numarul nu este de 10 caractere, va rugam corectari", "Eroare", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!tf.getText().matches("^[0-9]*$")) {
            JOptionPane.showMessageDialog(null, "Numarul nu contine numai cifre, va rugam corectari", "Eroare", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!(tf.getText().substring(0,2).equalsIgnoreCase("07"))){
            JOptionPane.showMessageDialog(null, "Numarul trebuie sa inceapa cu 07", "Eroare", JOptionPane.ERROR_MESSAGE);
            return false;
        }

            return true;

        }
    }

