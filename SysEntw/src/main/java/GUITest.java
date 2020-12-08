import javax.swing.*;

public class GUITest {
    private JButton button1;
    private JPanel panel1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new GUITest().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
