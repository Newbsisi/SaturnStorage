import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;

public class Homepage {
    public static void main(String[] args) {
        JFrame a = new JFrame("SaturnStorage");
        JTextField b = new JTextField("Search");

        JButton cameraB = new JButton("Camera");
        JButton lightB = new JButton("Light");
        JButton cablesB = new JButton("Cables");
        JButton propsB = new JButton("Props");
        JButton camerastandsB = new JButton("Stands");
        JButton microphoneB = new JButton("Microphone");

        JPanel buttonPanel = new JPanel(new GridLayout(6, 1));
        buttonPanel.add(cameraB);
        buttonPanel.add(lightB);
        buttonPanel.add(cablesB);
        buttonPanel.add(propsB);
        buttonPanel.add(camerastandsB);
        buttonPanel.add(microphoneB);

        cameraB.setPreferredSize(new Dimension(200, 30));
        lightB.setPreferredSize(new Dimension(200, 30));
        cablesB.setPreferredSize(new Dimension(200, 30));
        propsB.setPreferredSize(new Dimension(200, 30));
        camerastandsB.setPreferredSize(new Dimension(200, 30));
        microphoneB.setPreferredSize(new Dimension(200, 30));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(b, BorderLayout.NORTH);
        searchPanel.add(buttonPanel, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username");
        JButton returnButton = new JButton("Return");

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(usernameLabel, BorderLayout.WEST);
        userPanel.add(returnButton, BorderLayout.EAST);

        a.add(searchPanel);
        a.add(userPanel, BorderLayout.NORTH);

        a.setSize(800, 500);
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setVisible(true);
    }
}
