package Programm;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;

public class DBcon {

    private OrientDB orient;
    private ODatabaseSession db;
    private boolean firstTime = true;


    public ODatabaseSession getDb() {
        if (firstTime){
            try {
                orient = new OrientDB("remote:wgay.hopto.org", OrientDBConfig.defaultConfig());
                db = orient.open("Netzwerk1", "root", "123456");
                firstTime = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return db;
    }
    public void close(){
        orient.close();
        db.close();
    }
}




