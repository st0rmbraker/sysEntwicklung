package Programm;

import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static javafx.fxml.FXMLLoader.load;



public class GUI extends Application {

    Optional<String> user;
    Helpers h = new Helpers();
    boolean isWindowOpen = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Benutzername eingeben");
        dialog.setHeaderText("Bitte geben sie ihren Benutzernamen ein.");
        dialog.setContentText("Bitte Namen eingeben:");
        user = dialog.showAndWait();


        System.out.println("Loading UI");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        UIController controller = fxmlLoader.<UIController>getController();
        System.out.println("Loading DB");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isWindowOpen = false;
                System.exit(0);
            }
        });
        if(isWindowOpen) {
            try {
                controller.setUser(user.get());
                primaryStage.setTitle("OrientDB-Test");
                primaryStage.setScene(new Scene(root, 700, 450));

                Thread t = new Background(controller, user.get());
                t.start();

                primaryStage.show();

     /*       Thread taskThread = new Thread(new Runnable() {
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
                        OVertex userV = h.getVertexByUsername(user.get());
                        h.session();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                controller.own_infos.setText(h.printUserInfo(userV));
                                controller.output_chat.setText("Letzte Aktualisierung:\n"+new java.util.Date()+"\n");
                                if(controller.getMe_Following()){
                                    controller.output_follower.setItems(controller.prepareFollowers(userV, "OUT"));
                                }
                                else{
                                    controller.output_follower.setItems(controller.prepareFollowers(userV, "IN"));
                                }
                            }
                        });

                    }
                }
            });

            taskThread.start();

      */

            } catch (ODatabaseException ex) {
                System.out.println("Ein Datenbankfehler ist aufgetreten " + ex);
                System.exit(42);
            }
        }

    }

    public String getUser() {
        return user.get();
    }

    private void closeWindowEvent(WindowEvent event) {
        System.out.println("Window close request ...");
        isWindowOpen = false;

    }

}
