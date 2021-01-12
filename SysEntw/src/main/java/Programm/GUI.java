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

    String user;
    Helpers h = new Helpers();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        UIController controller = fxmlLoader.<UIController>getController();
        try {
            controller.setUser(h.getVertexByUsername(user));
            primaryStage.setTitle("OrientDB-Test");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        }
        catch (ODatabaseException ex){
            System.out.println("Ein Datenbankfehler ist aufgetreten "+ex);
        }


        TextInputDialog dialog = new TextInputDialog("Userinput");
        dialog.setTitle("Text Input Dialog");
        dialog.setHeaderText("Look, a Text Input Dialog");
        dialog.setContentText("Please enter your name:");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            user = result.get();
        }

        controller.setUser(h.getVertexByUsername(user));


    }

    public String getUser() {
        return user;
    }
}
