package Programm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;

import static javafx.fxml.FXMLLoader.load;



public class GUI extends Application {

    String user;
    Helpers h = new Helpers();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        user = JOptionPane.showInputDialog(null,
                "Bitte geben sie ihren Benutzernamen ein.",
                JOptionPane.DEFAULT_OPTION);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        UIController controller = fxmlLoader.<UIController>getController();

        controller.setUser(h.getVertexByUsername(user));

        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add("/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public String getUser() {
        return user;
    }
}
