package guiTests;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import Programm.*;

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

    public static String user;




    public GUITest() {
        session();
        refresh();
        System.out.println("Startup");

        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        addPerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OVertex n = createPerson(db,inputLast.getText(), inputFirst.getText(), inputUser.getText());
                refresh();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                orient.close();
                System.out.println("Closing");
            }
        });
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

        user = JOptionPane.showInputDialog(null,
                "Bitte geben sie ihren Benutzernamen ein.",
                JOptionPane.DEFAULT_OPTION);

        System.out.println(user);

        frame.setVisible(true);
    }
}
