import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import jnr.x86asm.RID;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUITest {
    private JButton button1;
    private JPanel panel1;
    private JTextArea textArea1;

    public GUITest() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

                ODatabaseSession db = orient.open("DHBWDB","root","123456");

                String statement = "SELECT FROM PersonV";
                OResultSet rs = db.query(statement);
                while(rs.hasNext()){
                    OResult row = rs.next();
                    //Date birthDate = row.getProperty("birthDate");
                    textArea1.append("ID: "+row.getIdentity().toString());
                    textArea1.append("Name: "+row.<String>getProperty("firstName")+" "+row.<String>getProperty("lastName")+"\n");
                }
                String statement2
                rs.close();
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setContentPane(new GUITest().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
