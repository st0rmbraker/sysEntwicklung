package Programm;

import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
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
     * https://riptutorial.com/javafx/example/7291/updating-the-ui-using-platform-runlater
     * Ausgabe getrennt als platform runlater, da aktualisierung der UI nicht im Hintergrund möglich ist,
     * aber die DB aktualisierung die UI sonst unresponsive macht
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
                own_infos = (h.printUserInfo(userV));
                output_chat = ("Letzte Aktualisierung:\n" + new java.util.Date() + "\n");
                if (u.getMe_Following()) {
                    //output_follower.add(h.countFollowers(userV, "OUT"));
                    output_follower = (u.prepareFollowers(userV, "OUT"));
                } else {
                    //output_follower.add(h.countFollowers(userV, "IN"));
                    output_follower = (u.prepareFollowers(userV, "IN"));
                }
                if (u.chatPartner != null){
                    ODocument chat = h.getChat(userV, u.chatPartner);
                    output_chat.concat(h.printMessagesFromChat(chat));
                }

                Thread.sleep(1000);

            }
            catch (InterruptedException e) {
               e.printStackTrace();
            }


            Thread taskThread = new Thread(() -> Platform.runLater(() -> {
                u.own_infos.setText(own_infos);
                u.output_chat.setText(output_chat);
                u.output_follower.setItems(output_follower);
            }));
            taskThread.start();
        }



    }
}
