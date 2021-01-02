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
    OVertex malhoffen = getVertexByUsername("atheob");
    System.out.println(malhoffen.getProperty("firstName").toString());


    }

    public static void listConnectedVertices(OVertex element)
    {
        Iterable<OVertex> overtexList = element.getVertices(ODirection.OUT);

        System.out.println("Du folgst: ");
        for(OVertex v : overtexList) {
            System.out.print("-" + v.getProperty("firstName").toString() + System.lineSeparator());
        }

    }

    public static OEdge followUser(OVertex follower, OVertex followed)
    {
        boolean followsAlready = false;

        //checkt ob User bereits folgt damit keine doppelten Edges kommen

        Iterable<OVertex> overtexList = follower.getVertices(ODirection.OUT);
        for(OVertex v : overtexList) {
            if(v.getIdentity() == followed.getIdentity())
            {
                System.out.println("User folgt bereits");
                return null;
            }
        }

        OEdge temp = follower.addEdge(followed, "follows");
        temp.save();
        return temp;
    }

    public static OVertex getVertexByUsername(String userID)
    {
        String statement = "SELECT FROM Account WHERE username = ?";
        OResultSet rs = db.query(statement, userID);
        while(rs.hasNext()){
            OResult row = rs.next();
            //Date birthDate = row.getProperty("birthDate");

            System.out.println("Läuft soweit");
            if(row.<String>getProperty("username").equals(userID))
            {
                System.out.println("Geht in die IF");
                ORecordId user = new ORecordId(String.valueOf(row.getIdentity()));
                OVertex ret = db.load(user);

                System.out.println("Erfolg");
                return ret;
            }
        }
        rs.close();

        System.out.println("Misserfolg");
        return null;
    }


}
