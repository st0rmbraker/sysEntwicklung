import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;


public class Vertex {

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        //FICK DOCH MEIN LEBEN GRAPHDB IST DER GRÖßTE BS
        Vertex v = null;

        orient.close();
    }

}
