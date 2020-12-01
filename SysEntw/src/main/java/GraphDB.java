import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.record.OElement;

public class GraphDB {

    public static void main(String[] args) {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());

        ODatabaseSession db = orient.open("VehicleHistoryGraph","root","123456");

        OElement element = db.newInstance("Test");
        element.setProperty("name", "John");

//at this stage the record is not yet persistent

        System.out.println(element.getIdentity()); //this will print a temporary RID (negative cluster position)

        element.save();

//the element is now persistent

        System.out.println(element.getIdentity()); //this will print the valid, final RID for that document.
        ORID temp = element.getIdentity();

        ORecordID temp2 = new ORecordID("#13:1");

        OElement doc = db.load(temp);
        System.out.println(doc.getProperty("name"));



        db.close();
        orient.close();

        }
    }


