package Programm;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Helpers {

    public OrientDB orient;
    public ODatabaseSession db;

    public void session(){
        orient = new OrientDB("remote:wgay.hopto.org", OrientDBConfig.defaultConfig());
        db = orient.open("Netzwerk1", "root", "123456");

    }

    public String refreshAcc(){
        System.out.println("Refreshing");
        try{
            session();
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

    /**
     * Diese Methode überprüft, ob ein User exitsiert. Dafür muss der USername übergeben werden
     * @param username: String
     * @return: es wird ein boolean zurückgegeben
     */
    public boolean checkUserExists(String username){
        session();
        OResultSet rs = db.query("SELECT FROM Account WHERE username =?", username);
        while (rs.hasNext()) {
            OResult row = rs.next();
            System.out.println(row.<String>getProperty("username"));
            if(row.<String>getProperty("username").equals(username)) return true;
        }
        return false;
    }

    /**
     * ???
     */
    public void clear(){
        System.out.println("Macht im Moment nichts.");
    }

    public String outputQueryAcc(String statement) {
        session();
        String ret = "Nutzer in der Datenbank:\n";
        OResultSet rs = db.query(statement);

        while (rs.hasNext()) {
            OResult row = rs.next();
            ret = ret+("ID: " + row.getIdentity().toString());
            ret = ret+("Name: " + row.getProperty("firstName").toString() + " " + row.getProperty("lastName").toString() + "\n");
        }

        rs.close();
        return ret;
    }

    /**
     * Methode erstellt einen neuen Nutzer mit allen dazugehörigen Attributen in der gegeben Datenbank
     * @param nLastName: Nachname des Users
     * @param nFirstName: Vorname des Users
     * @param nUsername: USername des Users
     * @return: gibt den neu erstellten Nutzer zurück
     */
    public OVertex createPerson ( String nFirstName, String nLastName, String nUsername){
        session();
        OVertex n = db.newVertex("Account");
        n.setProperty("lastName", nLastName);
        n.setProperty("firstName", nFirstName);
        n.setProperty("username", nUsername);
        n.save();
        return n;
    }

    //Gibt alle Knoten aus, denen der User folgt.
    public String listConnectedVertices(OVertex element, String direction){
        session();
        String ret = "";
        Iterable<OVertex> overtexList;
        if(direction.equals("IN")) {
           overtexList = element.getVertices(ODirection.IN); //Gibt auch noch IN und BOTH für die Richtungen
        }
        else {
            overtexList = element.getVertices(ODirection.OUT); //Gibt auch noch IN und BOTH für die Richtungen
        }
        for(OVertex v : overtexList) {
            ret += ("-" + v.getProperty("firstName").toString() + System.lineSeparator());
        }
        return ret;

    }

    //Erzeugt Edge "follows" von follower zu followed, wenn Edge noch nicht vorhanden und gibt erstellte Edge zuriecl
    public boolean followUser(OVertex follower, OVertex followed){
        session();
        boolean followsAlready = false;
        //checkt ob User bereits folgt damit keine doppelten Edges kommen

        Iterable<OVertex> overtexList = follower.getVertices(ODirection.OUT);
        for(OVertex v : overtexList) {
            if(v.getProperty("username") == followed.getProperty("username"))
            {
                System.out.println(follower.getProperty("username") + " folgt bereits " + followed.getProperty("username"));
                return false;
            }
        }

        OEdge temp = follower.addEdge(followed, "follows");
        System.out.println("WARUM");
        temp.save();
        return true;
    }

    //Gibt ein Element passend zur property "username" aus der Tabelle Account zurueck... WICHTIG: Nutzernamen duerfen nur einmalig sein, ansonsten wird erstes gefundenes Element zurueckgegeben => Bei Erstellung beachten
    public OVertex getVertexByUsername(String userID){
        session();
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

    public String countFollowers(OVertex user, String direction)
    {
        session();

        Iterable<OVertex> list;

        if(direction.equals("IN"))
        {
            list = user.getVertices(ODirection.IN);
        }

        else {
            list = user.getVertices(ODirection.OUT);
        }

        int counter = 0;
        for(OVertex v : list) {
            counter++;
        }

        return Integer.toString(counter);

    }

    /**
     * erstellt Userinfos Objekt in der klasse userInfos mit Stadt, Mail und Geburtstag, letzteres als java.sql.Date
     * @return erstelltes Object der userInfos klasse
     */
    public ODocument createUserInfos(String city, String mail, Date birthDay)
    {
        session();
        ODocument doc = new ODocument("userInfos");
        doc.field( "city", city );
        doc.field( "mail", mail );
        doc.field( "birthday", birthDay);

        db.save(doc);

        return doc;
    }

    public void close(){
        db.close();
        orient.close();
    }

    public OVertex createUser(String userName, String firstName, String lastName)
    {
        if(checkUserExists(userName) == false)
        {
            OVertex user = db.newVertex("Account");
            user.setProperty("username", userName);
            user.setProperty("firstName", firstName);
            user.setProperty("lastName", lastName);
            user.setProperty("userInfos", createUserInfos("Mannheim", "alex@web.de", new Date(1,1,1)));

            user.save();
            return user;
        }
        System.out.println("User bereits vorhanden");
        return null;

        //ERGÄNZEN: WENN USER VORHANDEN DATEN ZIEHEN
    }


    public List<Optional<OVertex>> getUsersByCity(String city)
    {
        session();

        List<Optional<OVertex>> list = new ArrayList<>();

        OResultSet rs = db.query("SELECT FROM Account");
        while(rs.hasNext())
        {
            OResult row = rs.next();

            list.add(row.toElement().asVertex());
            System.out.println(row.<String>getProperty("username"));

        }

        return list;
    }

    public void printTest() {
        session();

        OResultSet rs = db.query("SELECT FROM Account WHERE username='atheob'");
        while(rs.hasNext())
        {
            OResult row = rs.next();

            OResultSet rsuserInfos = db.query("SELECT FROM userInfos WHERE @rid=?", row.getProperty("userInfos").toString());

            if(rsuserInfos.hasNext())
            {

                rsuserInfos.next();
                //System.out.println(rsuserInfos.);
            }

            System.out.println(row.getProperty("userInfos").toString());

        }
    }


    public String getUsersFromLand(String land){

        session();
        String ret = "User: \n";
        OResultSet rs = db.query("SELECT * FROM Account WHERE userInfos.land.kuerzel=?", land);

        while(rs.hasNext())
        {
            OResult row = rs.next();
            ret = ret + "\n - " +row.getProperty("username").toString();
        }
        System.out.println(ret);
        return ret;
    }
}
