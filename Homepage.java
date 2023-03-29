import javax.swing.*;
public class Homepage {
    public static void main(String[] args){
        JFrame a = new JFrame("SaturnStorage");
        JTextField b = new JTextField("Homepage");
        b.setBounds(50,100,200,30);
        a.add(b);
        a.setSize(300,300);
        a.setLayout(null);
        a.setVisible(true);
    }

}
