import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.record.OVertex;

public class Vertex {

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("DHBWDB","root","123456");

        db.createVertexClass("Person");

        OVertex person1 = db.newVertex("Person");
        OVertex person2 = db.newVertex("Person");




        db.close();
        orient.close();
    }

}
