package Programm;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.util.*;
//benötigt um bilder in binary data zu konvertieren
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javafx.scene.image.Image;

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
     * Diese Methode überprüft, ob ein User existiert
     * @param username: String
     * @return: boolean
     */
    public boolean checkUserExists(String username){
        session();
        OResultSet rs = db.query("SELECT FROM Account WHERE username =?", username);
        while (rs.hasNext()) {
            OResult row = rs.next();
            //System.out.println(row.<String>getProperty("username"));
            if(row.<String>getProperty("username").equals(username)) return true;
        }
        return false;
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

    //Erzeugt Edge "follows" von follower zu followed, wenn Edge noch nicht vorhanden und gibt erstellte Edge zurück
    public boolean followUser(OVertex follower, OVertex followed){
        session();
        boolean followsAlready = false;
        //checkt ob User bereits folgt damit keine doppelten Edges kommen

        Iterable<OVertex> overtexList = follower.getVertices(ODirection.OUT);
        for(OVertex v : overtexList) {
            if(v.getProperty("username").toString().equals(followed.getProperty("username").toString()))
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
                //System.out.println("User "+userID+" gefunden.");
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
    public ODocument createUserDocument(String city, String mail, Date birthDay)
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

    /**
     * Erstellt einen neuen User, wenn der User noch nicht existiert
     * @param userName
     * @param firstName
     * @param lastName
     * @return
     */
    public OVertex createUser(String userName, String firstName, String lastName, String filePath)
    {
        if(checkUserExists(userName) == false)
        {
            OVertex user = db.newVertex("Account");
            user.setProperty("username", userName);
            user.setProperty("firstName", firstName);
            user.setProperty("lastName", lastName);

            user.setProperty("profile", uploadImage(filePath));
            //user.setProperty("userInfos", createUserDocument("Mannheim", "alex@web.de", new Date(1,1,1)));

            user.save();
            return user;
        }
        System.out.println("User bereits vorhanden");
        return null;

        //ERGÄNZEN: WENN USER VORHANDEN DATEN ZIEHEN//ToDo
    }

    /**
     * ToDo
     * @param city
     * @return
     */
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

    /**
     * Gibt die User-properties als String zurück
     * @param u Vertex des Benutzers
     * @return user-properties
     */
    public String printUserInfo(OVertex u) {
        session();
        String ret;
        ret = ("Benutzername: " + u.getProperty("username") + "\n");
        ret = ret.concat("Vorname: " + u.getProperty("firstName") + "\n");
        ret = ret.concat("Nachname: " + u.getProperty("lastName") + "\n");
        //ret = ret.concat("Dokument: " + u.getProperty("userInfos") + "\n");

        ODocument d = u.getProperty("userInfos");
        Set<String> s = d.getPropertyNames();
        for (String temp : s){
            ret = ret.concat(temp+" : "+d.getProperty(temp.toString()) + "\n");
        }
        return ret;
    }

    /**
     * Wie printUserInfo, jedoch mit Benutzernamen anstatt Objekt
     * @param user
     * @return
     */
    public String printUserInfo(String user) {
        return printUserInfo(getVertexByUsername(user));
    }

    //Dieser Code muss noch in eine Funktion übernommen werden ToDo
     /*
        OResultSet rs = db.query("SELECT FROM Account WHERE username="+user);
        String ret;
        while(rs.hasNext())
        {
            OResult row = rs.next();
            OResultSet rsuserInfos = db.query("SELECT FROM userInfos WHERE @rid=?", row.getProperty("userInfos").toString());
            if(rsuserInfos.hasNext())
            {

                rsuserInfos.next();
            }

            System.out.println(row.getProperty("userInfos").toString());

        }
        */

    //ToDo
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

    /**
     * Methode konvertiert von .jpg Datei in binary array
     * So kann das Bild in Orient DB gespeichert werden
     * @return: Es wird der Binary Array returned
     */
    //https://www.codespeedy.com/how-to-convert-an-image-to-binary-data-in-java/
    //https://stackoverflow.com/questions/6702423/convert-image-and-audio-files-to-binary-in-java/43427851
    public byte[] convertToBinary(String filepath){
        try {
            //bild konvertieren
            System.out.println("superman");
            BufferedImage sourceimage = ImageIO.read(new File(filepath));
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ImageIO.write(sourceimage, "jpg", bytes);
            byte[] jpgByteArray = bytes.toByteArray();
            //String resultantimage = Base64.encode(bytes.toByteArray());
            //System.out.println(resultantimage);
            //return bytes;
            //dieser Teil konvertiert es in einen String, aus 0en und 1en
            StringBuilder sb = new StringBuilder();
            for (byte by : jpgByteArray) {
                sb.append(Integer.toBinaryString(by & 0xFF));
            }
            System.out.println(sb);
            return jpgByteArray;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Bild in Form einer Binary Arrays wird in DB gespeichert
     * @param bild: vorher duch convertToBinary() aufrufen, um Format anzupassen
     */
    public ODocument saveImage(byte[] bild){
        session();
        ODocument n = new ODocument("Bild");
        n.field("bild", bild, OType.BINARY);
        n.field("Name", "test10");
        n.save();
        return n;
    }

    /**
     * Mathoede konvertiert bild von binary datei in Image-objekt für JavaFX.
     * @param bild: bild in binary version
     */
    public Image convertToImg(byte[] bild) {
        //byte[] original = obj.orig_seq.getBytes();

        Image img = new Image(new ByteArrayInputStream(bild));
        InputStream in = new ByteArrayInputStream(bild);
        try {
            BufferedImage img2 = ImageIO.read(in);
            System.out.println(img2);
            ImageIO.write(img2, "jpg",
                    new File("C:\\Users\\Alex\\IdeaProjects\\sysEntwicklung\\SysEntw\\src\\main\\resources\\Screenshot.jpg"));
        }catch(Exception e){
            System.out.println("Fehler");
        }

        return img;
    }

    /**
     * Methode lädt das Bild byte[] herunter und gibt es zurück
     * @param bildname
     * @return: Datensatz mit dem neuen Bild
     */
    public byte[] getPictureByName(String bildname){
        session();
//        OResultSet rs = db.query("SELECT FROM Bild WHERE Name = ?", bildname);
//        if(rs.hasNext()) {
//            OResult row = rs.next();
//            //System.out.println(row.getProperty("name").toString());
//            ORecordId rid = new ORecordId("#51:0");
//            ODocument doc = db.load(rid);
//
//            ORecordBytes record = doc.getProperty("bild");
//            byte[] content = record.toStream();
//            return content;
//        }
        for(ODocument images : db.browseClass("Bild")) {
            //System.out.println(images.field("name").toString());
            if(images.field("Name").toString().equals(bildname)) {
                System.out.println(images.getProperty("Name").toString());
                byte[] content = images.getProperty("bild");
                return content;
            }
        }
        return null;
    }

    /**
     * Methode konvertiert das am Pfad gegebene Bild in Binary
     * Lädt es in Orient DB und gibt das neue Dokumetn zurück
     * @param path: Pfad zum Bild welches hochgeladen werden soll
     * @return
     */
    public ODocument uploadImage(String path){
        return saveImage(convertToBinary(path));
    }
}
