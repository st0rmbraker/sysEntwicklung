import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import java.util.Optional;

public class FollowTest {
//WICHTIG ALLGEMEIN: BEI AENDERUNGEN ERHOEHT SICH DIE @VERSION UM 1
    static OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
    static ODatabaseSession db = orient.open("Netzwerk1","root","123456");

    public static void main(String[] args) {
/**
        ORecordId maxID = new ORecordId("#62:0");
        OVertex max = db.load(maxID);

        ORecordId lennartID = new ORecordId("#63:0");
        OVertex lennart = db.load(lennartID);

        ORecordId alexID = new ORecordId("#61:0");
        OVertex alex = db.load(alexID);

        ORecordId homerID = new ORecordId("#58:0");
        OVertex homer = db.load(homerID);

        followUser(alex, max);
        followUser(alex, lennart);
        followUser(alex, homer);

        listConnectedVertices(alex);
*/
    OVertex test = getVertexByUsername("mfink");
    OVertex test2 = getVertexByUsername("lhergel");
    System.out.println(test.getProperty("firstName").toString());

    followUser(test2, test);
    listConnectedVertices(test);


    db.close();
    orient.close();
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

    //Gibt ein Element passend zur property "username" aus der Tabelle Account zurueck... WICHTIG: Nutzernamen duerfen nur einmalig sein, ansonsten wird erstes gefundenes Element zurueckgegeben => Bei Erstellung beachten

    public static OVertex getVertexByUsername(String userID)
    {
        String statement = "SELECT FROM Account WHERE username = ?";
        OResultSet rs = db.query(statement, userID);
        while(rs.hasNext()){
            OResult row = rs.next();

            System.out.println("Läuft soweit");
            if(row.<String>getProperty("username").equals(userID))
            {
                System.out.println("Geht in die IF");
                ORecordId user = new ORecordId(row.getProperty("@rid").toString());
                OVertex ret = db.load(user);

                System.out.println("Erfolg");
                rs.close();
                return ret;


            }
        }


        System.out.println("Misserfolg");
        return null;
    }


}
