import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;

public class FollowTest {

    public static void main(String[] args) {

        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("Netzwerk1","root","123456");

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


}
