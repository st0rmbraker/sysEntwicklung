package Programm;

import com.orientechnologies.orient.core.db.ODatabaseSession;
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
        controller.dbcon();

        //beendet Programm wenn Fenster geschlossen wird
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                isWindowOpen = false;
                System.exit(42);
            }
        });
        if(isWindowOpen) {
            try {
                //ODatabaseSession db = new DBcon().getDb();
                controller.setUser(user.get());
                primaryStage.setTitle("OrientDB-Test");
                primaryStage.setScene(new Scene(root, 700, 450));

                Thread t = new Background(controller, user.get());
                t.start();

                //System.out.println(DBcon.getDb());

                primaryStage.show();

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
