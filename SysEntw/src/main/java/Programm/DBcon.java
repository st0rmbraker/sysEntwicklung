package Programm;

import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.*;

public class DBcon {

    private static OrientDB orient;
    private static ODatabaseSession db;
    private static boolean firstTime = true;
    private static ODatabasePool pool;
    private static int count;
    private static int closed;

    public static ODatabaseSession getDb() throws InterruptedException {
        if (firstTime) {
            try {
                orient = new OrientDB("remote:wgay.hopto.org", OrientDBConfig.defaultConfig());
                db = orient.open("Netzwerk1", "root", "123456");
                firstTime = false;
                System.out.println("Neue Verbindung hergestellt.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("DB abgefragt");
        return db;
    }

    public static void close(){
        db.close();
        orient.close();
    }

    protected void finalize() throws Throwable {
        closed++;
        System.out.println("DB geschlossen"+closed);
        System.out.println("Active"+(count-closed));
        db.close();
        orient.close();
    }
}