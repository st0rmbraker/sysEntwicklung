package Programm;

import com.orientechnologies.orient.core.record.OVertex;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Background extends Thread {

    UIController u;
    String user;
    OVertex userV;
    Helpers h;
    String own_infos;
    String output_chat;
    ObservableList<String> output_follower = FXCollections.observableArrayList();

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
        while(true){
            try {
                h.session();
                userV = h.getVertexByUsername(user);
                own_infos=(h.printUserInfo(userV));
                output_chat=("Letzte Aktualisierung:\n"+new java.util.Date()+"\n");
                try{
                    if(u.getMe_Following()){
                        output_follower= (u.prepareFollowers(userV, "OUT"));

                    }
                    else{
                        output_follower = (u.prepareFollowers(userV, "IN"));
                    }
                }
                catch (Exception ex){
                    System.out.print("Fehler: "+ex);
                }

                Thread.sleep(1000);
                //i++;
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

            Thread taskThread = new Thread(() -> Platform.runLater(() -> {
                u.own_infos.setText(own_infos);
                u.output_chat.setText(output_chat);
                u.output_follower.setItems(output_follower);
            }));
            taskThread.start();

        }

        //https://riptutorial.com/javafx/example/7291/updating-the-ui-using-platform-runlater

    }
}
