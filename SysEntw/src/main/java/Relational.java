import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;


public class Relational {

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("DHBWDB","root","123456");

        OElement element = db.newInstance("Hochschulex");
        element.setProperty("name", "3");

        element.save();

        OElement elementx = db.newInstance("Studierend");
        elementx.setProperty("id_schule", element.getIdentity(), OType.LINK);
        elementx.setProperty("Name", "Student1");
        elementx.save();
//at this stage the record is not yet persistent

        System.out.println(element.getIdentity()); //this will print a temporary RID (negative cluster position)




//the element is now persistent

        System.out.println(element.getIdentity()); //this will print the valid, final RID for that document.
        ORID temp = element.getIdentity();



        String statement = "SELECT FROM Schüler WHERE Schüler.ID = ? AND Schüler.Schul_ID=Schule.ID";
        OResultSet rs = db.query(statement, "1");
        while(rs.hasNext()){
            OResult row = rs.next();
            //Date birthDate = row.getProperty("birthDate");
            System.out.println("name: "+row.<String>getProperty("name"));
        }
        rs.close();
        db.close();
        orient.close();



    }
}
