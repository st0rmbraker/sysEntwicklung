package objecttest;

import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.orientechnologies.orient.object.db.OObjectDatabaseTx;

public class Object {
    public static void main(String[] args) throws ClassNotFoundException {
/*
        ODatabaseObjectPool pool = new ODatabaseObjectPool("remote:localhost/ObjectDB",
                "root", "123456", OrientDBConfig.defaultConfig());
        ODatabaseObject db = pool.acquire();


// REGISTER CLASS ONLY ONCE AFTER DB IS OPENED/CREATED
        db.getEntityManager().registerEntityClass(Class.forName("City"));
        db.getEntityManager().registerEntityClass(Class.forName("Country"));

// CREATE NEW PROXIED OBJECT AND FILL IT
        Account account = db.newInstance(Account.class);
        account.setName("Tully");
        account.setSurname("Cicero");

        db.newIn

        City rome = db.newInstance(City.class, "Rome",
                db.newInstance(Country.class, "Italy"));
        account.getAdress().add(
                new Adress("Residence", rome, "Piazza Navona, 1"));

// SAVE
        db.save(account);

// CREATE NEW PROXIED OBJECT AND FILL IT
        Account account = new Account();
        account.setName("Alessandro");
        account.setSurname("Manzoni");

        City milan = new City("Milan",
                new Country("Italy"));
        account.getAdress()
                .add(new Adress("Residence", milan,
                        "Piazza Manzoni, 1"));

// SAVE ACCOUNT: ORIENTDB SERIALIZES OBJECT & GIVES PROXIED INSTANCE
        account = db.save(account);

*/

        OObjectDatabaseTx db = new OObjectDatabaseTx ("remote:localhost/ObjectDB").open("root", "123456");

        //Add objects in package objecttest to db
        db.getEntityManager().registerEntityClasses("objecttest");

        //create sample object
        Account account = new Account();
        account.setSurname("Lol");
        account.setName("Yolo");
        account = db.save(account);



        String statement = "SELECT * FROM Account WHERE surname = ?";
        OResultSet rs = db.query(statement, "Lol");
        while(rs.hasNext()){
            OResult row = rs.next();
            System.out.println("name: "+row.<String>getProperty("surname"));
        }


        rs.close();
        db.close();
    }
}