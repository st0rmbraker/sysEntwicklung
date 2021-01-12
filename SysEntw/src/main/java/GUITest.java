import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.record.OVertex;

//import Programm.Helpers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUITest {
    private static JFrame frame;
    private JButton update;
    private JPanel panel1;
    private JTextArea outputAll;
    private JButton addPerson;
    private JTextField inputFirst;
    private JTextField inputLast;
    private JTextArea outputOwn;
    private JTextField ownID;
    private JTextField inputUser;
    private ODatabaseSession db;
    private OrientDB orient;

    Programm.Helpers h = new Programm.Helpers();

    public static String user;

    //Programm.Helpers h = new Programm.Helpers();

    public GUITest() {
        System.out.println("Startup");
        h.session();

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputAll.setText(h.refreshAcc());
            }

        });

        addPerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OVertex n = h.createPerson(inputLast.getText(), inputFirst.getText(), inputUser.getText());
                h.refreshAcc();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                //orient.close();
                System.out.println("Closing");
            }
        });
    }


    public void onStart(){
        h.refreshAcc();
        outputAll.setText("");
    }


    public void clear(){
        outputAll.setText("");
        outputOwn.setText("");
    }


    public static void main(String[] args) {

        frame = new JFrame("Netzwerk");
        frame.setContentPane(new GUITest().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        GUITest g = new GUITest();

        user = JOptionPane.showInputDialog(null,
                "Bitte geben sie ihren Benutzernamen ein.",
                JOptionPane.DEFAULT_OPTION);

        System.out.println(g.h.checkUserExists(user));

/**
        if(true){
            g.addPerson.setVisible(false);
            g.inputFirst.setVisible(false);
            g.inputLast.setVisible(false);
            g.inputUser.setVisible(false);
        }
        frame.validate();
        frame.repaint();
        frame.getContentPane().validate();
        frame.getContentPane().repaint();
**/
        System.out.println(user);
        frame.setVisible(true);
    }
}