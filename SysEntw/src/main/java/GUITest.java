import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.exception.ODatabaseException;
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
    private JTextArea outputAll;
    private JButton addPerson;
    private JTextField inputFirst;
    private JTextField inputLast;
    private JTextArea outputOwn;
    private JTextField ownID;
    private ODatabaseSession db;
    private OrientDB orient;

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
                OVertex n = createPerson(db,inputLast.getText(), inputFirst.getText());
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
                if (JOptionPane.showConfirmDialog(frame,
                        "Möchten sie dieses Fenster wirklich schließen?", "Fenster schließen?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    System.exit(0);
                    System.out.println("Close");
                }
            }
        });
    }

    public void session(){
        orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        db = orient.open("Netzwerk1","root","123456");
    }

    public void refresh(){

        System.out.println("Refreshing");
        try{
            session();
            outputAll.setText("");
            outputOwn.setText("");

            String statement = "SELECT FROM Account";
            db.query(statement);
            OResultSet rs = db.query(statement);

            while (rs.hasNext()) {
                OResult row = rs.next();
                outputAll.append("ID: " + row.getIdentity().toString());
                outputAll.append("Name: " + row.getProperty("firstName").toString() + " " + row.getProperty("lastName").toString() + "\n");
            }
            rs.close();

            /*String statement2 = "SELECT FROM follows";
            OResultSet rs2 = db.query(statement2);
            while (rs2.hasNext()) {
                OResult row = rs2.next();
                //outputAll.append(row.getProperty("in").toString());
            }*/


            //rs2.close();

            session();
            String statement3 = "SELECT FROM Account WHERE @rid="+ownID.getText();
            db.query(statement3);
            OResultSet rs3 = db.query(statement3);

            while (rs3.hasNext()) {
                OResult row = rs3.next();
                outputOwn.append("ID: " + row.getIdentity().toString());
                outputOwn.append("Name: " + row.getProperty("firstName").toString() + " " + row.getProperty("lastName").toString() + "\n");
            }
            rs3.close();




        }
        catch (ODatabaseException ex) {
            System.out.println("Ein Datenbankfehler ist aufgetreten"+ex);
        }
        finally{
            System.out.println("Refreshing complete");
        }

    }

    public OVertex createPerson (ODatabaseSession db, String lastName, String firstName){
        session();
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
