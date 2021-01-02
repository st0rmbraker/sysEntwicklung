import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUITest {
    private static JFrame frame;
    private JButton update;
    private JPanel panel1;
    private JTextArea output;
    private JButton addPerson;
    private JTextField input;
    private ODatabaseSession db;
    private OrientDB orient;

    public GUITest() {

        onStart();
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
                OVertex n = createPerson(db,"ToDo",input.getText());
                refresh();

                //OVertex wiedemann = createPerson(db, "Wiedemann", "Armin");
                //OVertex grimm = createPerson(db, "Grimm", "Simon");

                //OEdge edge1 = matt.addEdge(wiedemann, "jagt");
                //OEdge edge2 = wiedemann.addEdge(grimm, "jagt");
                //edge1.save();
                //edge2.save();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                orient.close();
                System.out.println("Close");
                if (JOptionPane.showConfirmDialog(frame,
                        "Möchten sie dieses Fenster wirklich schließen?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });
    }

    public void onStart(){
        orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        db = orient.open("Netzwerk1","root","123456");
    }

    public void refresh(){
        onStart();
        System.out.println("Refresh");
        try{
            output.setText("");
            String statement = "SELECT FROM Account";
            db.query(statement);
            OResultSet rs = db.query(statement);
            while (rs.hasNext()) {
                OResult row = rs.next();
                output.append("ID: " + row.getIdentity().toString());
                output.append("Name: " + row.getProperty("firstName").toString() + " " + row.getProperty("lastName").toString() + "\n");
            }
            /*String statement2 = "SELECT FROM follows";
            OResultSet rs2 = db.query(statement2);
            while (rs2.hasNext()) {
                OResult row = rs2.next();
                //output.append(row.getProperty("in").toString());
            }
            rs.close();
            rs2.close();*/
        }
        catch (ODatabaseException ex) {
            System.out.println("Ein Datenbankfehler ist aufgetreten: "+ex);
        }

    }

    public OVertex createPerson (ODatabaseSession db, String lastName, String firstName){
        OVertex n = db.newVertex("Account");
        n.setProperty("lastName", lastName);
        n.setProperty("firstName", firstName);
        n.save();
        return n;
    }

    public static void main(String[] args) {
        frame = new JFrame("Netzwerk");
        frame.setContentPane(new GUITest().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
