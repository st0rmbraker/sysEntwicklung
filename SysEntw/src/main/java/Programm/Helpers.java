package Programm;

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
import javax.swing.*;

public class Helpers {

    public OrientDB orient;
    public ODatabaseSession db;

    public void session(){
        orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        db = orient.open("Netzwerk1","root","123456");
    }

    public String refreshAcc(){
        System.out.println("Refreshing");
        try{
            clear();
            String statement = "SELECT FROM Account";
            return outputQueryAcc(statement);

            //statement = "SELECT FROM Account WHERE @rid="+ownID.getText();
            //outputQueryAcc(statement, outputOwn);

            /*String statement2 = "SELECT FROM follows";
            OResultSet rs2 = db.query(statement2);
            while (rs2.hasNext()) {
                OResult row = rs2.next();
                outputAll.append(row.getProperty("in").toString());
            }*/
        }
        catch (ODatabaseException ex) {
            System.out.println("Ein Datenbankfehler ist aufgetreten:"+ex);
            return null;
        }
        finally{
            System.out.println("Refreshing complete");
        }
    }

    public void clear(){
        System.out.println("TO DOOOOOOOOOOOOOOOO");
    }

    public String outputQueryAcc(String statement) {
        session();
        String ret = null;
        db.query(statement);
        OResultSet rs = db.query(statement);

        while (rs.hasNext()) {
            OResult row = rs.next();
            ret.concat("ID: " + row.getIdentity().toString());
            ret.concat("Name: " + row.getProperty("firstName").toString() + " " + row.getProperty("lastName").toString() + "\n");
        }

        rs.close();
        return ret;
    }

    public OVertex createPerson (ODatabaseSession db, String nLastName, String nFirstName, String nUser){
        session();
        OVertex n = db.newVertex("Account");
        n.setProperty("lastName", nLastName);
        n.setProperty("firstName", nFirstName);
        n.setProperty("inputUser", nUser);
        n.save();
        return n;
    }

    //Gibt alle Knoten aus, denen der User folgt.
    public String listConnectedVertices(OVertex element){
        String ret = null;
        Iterable<OVertex> overtexList = element.getVertices(ODirection.OUT); //Gibt auch noch IN und BOTH für die Richtungen

        System.out.println("Du folgst: ");
        for(OVertex v : overtexList) {
            ret.concat("-" + v.getProperty("firstName").toString() + System.lineSeparator());
        }
        return ret;

    }

    //Erzeugt Edge "follows" von follower zu followed, wenn Edge noch nicht vorhanden und gibt erstellte Edge zuriecl
    public OEdge followUser(OVertex follower, OVertex followed){

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

    //Gibt ein Element passend zur property "username" aus der Tabelle Account zurueck... WICHTIG: Nutzernamen duerfen nur einmalig sein, ansonsten wird erstes gefundenes Element zurueckgegeben => Bei Erstellung beachten
    public OVertex getVertexByUsername(String userID){
        OResultSet rs = db.query("SELECT FROM Account WHERE username = ?", userID);
        while(rs.hasNext()){
            OResult row = rs.next();
            if(row.<String>getProperty("username").equals(userID)){
                ORecordId user = new ORecordId(row.getProperty("@rid").toString());
                OVertex ret = db.load(user);
                System.out.println("User "+userID+" gefunden.");
                rs.close();
                return ret;
            }
        }
        System.out.println("User "+userID+" nicht gefunden.");
        return null;
    }

    public void close(){
        db.close();
        orient.close();
    }

}