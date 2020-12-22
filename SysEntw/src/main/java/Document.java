import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.record.impl.ODocument;

public class Document {

    public static void main (String[] args){

        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseDocument db = orient.open("DHBWDB","root", "123456");

        ODocument doc = new ODocument("Person");
        doc.field( "name", "Luke" );
        doc.field( "surname", "Skywalker" );
        doc.field( "city", new ODocument("City")
                .field("name","Rome")
                .field("country", "Italy") );

        db.save(doc);

        db.close();
        orient.close();

    }

}
