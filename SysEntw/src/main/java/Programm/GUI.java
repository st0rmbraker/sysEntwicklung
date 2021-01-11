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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        user = JOptionPane.showInputDialog(null,
                "Bitte geben sie ihren Benutzernamen ein.",
                JOptionPane.DEFAULT_OPTION);

        Parent root;
        root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        UIController ui = new UIController();

    }

    public String getUser(){
        return user;
    }





}
