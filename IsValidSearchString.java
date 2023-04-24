
import javax.swing.*;
import java.awt.event.*;
public class IsValidSearchString {

    public class InputValidator {

        private static final int MAX_LENGTH = 20; // maximum allowed length of input

        public static void validateInput(JTextField textField) {
            String input = textField.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid input.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (input.length() > MAX_LENGTH) {
                JOptionPane.showMessageDialog(null, "Search input cannot be longer than " + MAX_LENGTH + " characters.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //Performing search
                JOptionPane.showMessageDialog(null, "Performing search for \"" + input + "\"");
            }
        }

    }
}