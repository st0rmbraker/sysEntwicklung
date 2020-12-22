import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.OElement;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;


public class Relational {

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("VehicleHistoryGraph","root","123456");

        OElement element = db.newInstance("schueler");
        element.setProperty("name", "Juergen");
        element.setProperty("ID","1");
        element.setProperty("Schul_ID","1");

        OElement elementx = db.newInstance("schule");
        elementx.setProperty("name", "Testschule");
        elementx.setProperty("ID","1");

//at this stage the record is not yet persistent

        System.out.println(element.getIdentity()); //this will print a temporary RID (negative cluster position)

        element.save();
        elementx.save();

//the element is now persistent

        System.out.println(element.getIdentity()); //this will print the valid, final RID for that document.
        ORID temp = element.getIdentity();

        ORecordId temp2 = new ORecordId("#13:1");

        OElement doc = db.load(temp2);
        //    System.out.println(doc.getProperty("name"));


        String statement = "SELECT FROM Schule, Schüler WHERE Schüler.ID = ? AND Schüler.Schul_ID=Schule.ID";
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
