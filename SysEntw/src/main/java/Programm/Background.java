package Programm;

import com.orientechnologies.orient.core.record.OVertex;

public class Background extends Thread {

    UIController u;
    String user;
    OVertex userV;
    Helpers h;

    public Background(UIController nU, String nUser){
        setDaemon( true );

        this.u = nU;
        this.user = nUser;

        h = new Helpers();
        userV = h.getVertexByUsername(user);
    }


    //Die Methode, die synchron im Hintergrund läuft
    //Läuft als Daemon, aktualisiert alle zwei Sekunden
    //http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_14_003.htm#mjd0f19999270d6e1fbfd4af3a16273eef
    @Override
    public void run() {
        if(isDaemon()) System.out.println("Background-Aktualisierung als daemon gestartet.");
        else System.out.println("Problem");
        //int i=0;
        while(true){
            try {
                //if(i=5){
                userV = h.getVertexByUsername(user);
                //  i=0;
                //}
                h.session();
                u.own_infos.setText(h.printUserInfo(userV));
                u.output_chat.setText("Letzte Aktualisierung:\n"+new java.util.Date()+"\n");
                Thread.sleep(2000);
                //i++;
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }


    }
}
