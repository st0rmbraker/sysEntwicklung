package Programm;

import com.orientechnologies.orient.core.exception.ODatabaseException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import javax.swing.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static javafx.fxml.FXMLLoader.load;



public class GUI extends Application {

    Optional<String> user;
    Helpers h = new Helpers();

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
        try {
            controller.setUser(user.get());
            primaryStage.setTitle("OrientDB-Test");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        }
        catch (ODatabaseException ex){
            System.out.println("Ein Datenbankfehler ist aufgetreten "+ex);
        }

    }

    public String getUser() {
        return user.get();
    }
}
