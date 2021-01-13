package Programm;

import com.orientechnologies.orient.core.record.OVertex;
import javafx.application.Platform;

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


    /**
     * Die Methode, die synchron im Hintergrund läuft
     * Läuft als Daemon, aktualisiert alle zwei Sekunden
     * http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_14_003.htm#mjd0f19999270d6e1fbfd4af3a16273eef
     */
    @Override
    public void run() {
        Platform.setImplicitExit(false);
        if(isDaemon()) System.out.println("Background-Aktualisierung als daemon gestartet.");
        else System.out.println("Problem");
        //int i=0;
       /* while(true){
            try {
                //if(i=5){
                userV = h.getVertexByUsername(user);
                //  i=0;
                //}
                h.session();
                u.own_infos.setText(h.printUserInfo(userV));
                u.output_chat.setText("Letzte Aktualisierung:\n"+new java.util.Date()+"\n");
                try{
                    if(u.getMe_Following()){
                        u.output_follower.setItems(u.prepareFollowers(userV, "OUT"));

                    }
                    else{
                        u.output_follower.setItems(u.prepareFollowers(userV, "IN"));
                    }
                }
                catch (Exception ex){
                    System.out.print("Fehler: "+ex);
                }

                Thread.sleep(2000);
                //i++;
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
        }

        */
        Thread taskThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //Platform.setImplicitExit(false);
                //if(isDaemon()) System.out.println("Background-Aktualisierung als daemon gestartet.");
                //else System.out.println("Problem");
                //int i=0;
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    OVertex userV = h.getVertexByUsername(user);
                    h.session();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            u.own_infos.setText(h.printUserInfo(userV));
                            u.output_chat.setText("Letzte Aktualisierung:\n"+new java.util.Date()+"\n");
                            if(u.getMe_Following()){
                                u.output_follower.setItems(u.prepareFollowers(userV, "OUT"));
                            }
                            else{
                                u.output_follower.setItems(u.prepareFollowers(userV, "IN"));
                            }
                        }
                    });

                }
            }
        });
        taskThread.start();
    }
}
