package Programm;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.*;

public class DBcon {

    private static OrientDB orient;
    private static ODatabaseSession db;
    private static OrientDB orient2;
    private static ODatabaseSession db2;
    private static boolean firstTime = true;
    private static boolean firstTime2 = true;
    private static ODatabasePool pool;
    private static int count;
    private static int closed;

    public static ODatabaseSession getDb(){
        if (firstTime) {
            try {
                orient = new OrientDB("remote:wwi19sysentw.hopto.org", OrientDBConfig.defaultConfig());
                db = orient.open("Netzwerk1", "root", "WWI19netzwerk");
                firstTime = false;
                System.out.println("Neue Verbindung hergestellt.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//      System.out.println("DB1 abgefragt");
        return db;
    }

    public static ODatabaseSession getDb2(){
        if (firstTime2) {
            try {
                orient2 = new OrientDB("remote:wgay.hopto.org", OrientDBConfig.defaultConfig());
                db2 = orient.open("Netzwerk1", "root", "123456");
                firstTime2 = false;
                System.out.println("Neue Verbindung hergestellt.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//      System.out.println("DB2 abgefragt");
        return db2;
    }

}