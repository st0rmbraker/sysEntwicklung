import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.record.OVertex;


public class Vertex {

    public static void main(String[] args) {

        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("DHBWDB","root","123456");

        OVertex matt = createPerson(db, "Matt", "Michael");
        OVertex wiedemann = createPerson(db, "Wiedemann", "Armin");
        OVertex grimm = createPerson(db, "Grimm", "Simon");

        OEdge edge1 = matt.addEdge(wiedemann, "jagt");
        OEdge edge2 = wiedemann.addEdge(grimm, "jagt");
        edge1.save();
        edge2.save();


        db.close();
        orient.close();

    }


    private static OVertex createPerson(ODatabaseSession db, String lastName, String firstName)
    {
        OVertex result = db.newVertex("PersonV");
        result.setProperty("lastName", lastName);
        result.setProperty("firstName", firstName);
        result.save();

        return result;
    }

}
