package Programm;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.*;

public class DBcon {

    private OrientDB orient;
    private ODatabaseSession db;
    private boolean firstTime = true;
    private ODatabasePool pool;


    public ODatabaseSession getDb() throws InterruptedException {

       // if (firstTime){
            try {
                orient = new OrientDB("remote:wgay.hopto.org", OrientDBConfig.defaultConfig());
                db = orient.open("Netzwerk1", "root", "123456");
                firstTime = false;
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(3000);
                orient = new OrientDB("remote:wgay.hopto.org", OrientDBConfig.defaultConfig());
                db = orient.open("Netzwerk1", "root", "123456");
                firstTime = false;
            }
            System.out.println("DB zugriff");
      //  }
        return db;
    }

    public void close(){
        db.close();
    }

    protected void finalize() throws Throwable {
        System.out.println("DB geschlossen");
        db.close();
        orient.close();
    }
}