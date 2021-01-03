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

    public void session(){
        orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        db = orient.open("Netzwerk1","root","123456");
    }

    public void refresh(){
        System.out.println("Refreshing");
        try{
            clear();
            String statement;

            statement = "SELECT FROM Account";
            outputQueryAcc(statement, outputAll);
            statement = "SELECT FROM Account WHERE @rid="+ownID.getText();
            outputQueryAcc(statement, outputOwn);

            /*String statement2 = "SELECT FROM follows";
            OResultSet rs2 = db.query(statement2);
            while (rs2.hasNext()) {
                OResult row = rs2.next();
                outputAll.append(row.getProperty("in").toString());
            }*/
        }
        catch (ODatabaseException ex) {
            System.out.println("Ein Datenbankfehler ist aufgetreten:"+ex);
        }
        finally{
            System.out.println("Refreshing complete");
        }
    }

    public void clear(){
        outputAll.setText("");
        outputOwn.setText("");
    }

    private void outputQueryAcc(String statement, JTextArea output) {
        session();
        db.query(statement);
        OResultSet rs = db.query(statement);

        while (rs.hasNext()) {
            OResult row = rs.next();
            output.append("ID: " + row.getIdentity().toString());
            output.append("Name: " + row.getProperty("firstName").toString() + " " + row.getProperty("lastName").toString() + "\n");
        }
        rs.close();
    }

    public OVertex createPerson (ODatabaseSession db, String nLastName, String nFirstName,String nUser){
        session();
        OVertex n = db.newVertex("Account");
        n.setProperty("lastName", nLastName);
        n.setProperty("firstName", nFirstName);
        n.setProperty("inputUser", nUser);
        n.save();
        return n;
    }

    //Gibt alle Knoten aus, denen der User folgt.
    public static void listConnectedVertices(OVertex element)
    {
        Iterable<OVertex> overtexList = element.getVertices(ODirection.OUT); //Gibt auch noch IN und BOTH für die Richtungen

        System.out.println("Du folgst: ");
        for(OVertex v : overtexList) {
            System.out.print("-" + v.getProperty("firstName").toString() + System.lineSeparator());
        }

    }

    //Erzeugt Edge "follows" von follower zu followed, wenn Edge noch nicht vorhanden und gibt erstellte Edge zuriecl
    public static OEdge followUser(OVertex follower, OVertex followed)
    {
        boolean followsAlready = false;
        //checkt ob User bereits folgt damit keine doppelten Edges kommen

        Iterable<OVertex> overtexList = follower.getVertices(ODirection.OUT);
        for(OVertex v : overtexList) {
            if(v.getIdentity() == followed.getIdentity())
            {
                System.out.println(follower.getProperty("username") + " folgt bereits " + followed.getProperty("username"));
                return null;
            }
        }

        OEdge temp = follower.addEdge(followed, "follows");
        temp.save();
        return temp;
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
