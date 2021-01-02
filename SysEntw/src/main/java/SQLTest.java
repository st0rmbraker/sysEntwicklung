import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.sql.executor.*;


public class SQLTest {

    public static void main(String []args){


      OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("VehicleHistoryGraph","root","123456");

        String statement = "SELECT FROM person WHERE firstName = ?";
        OResultSet rs = db.query(statement, "Lennart");
        while(rs.hasNext()){
            OResult row = rs.next();
            //Date birthDate = row.getProperty("birthDate");
            System.out.println("name: "+row.<String>getProperty("firstName"));
        }
        rs.close();
    }


}
