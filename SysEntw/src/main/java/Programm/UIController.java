package Programm;

import com.orientechnologies.orient.core.record.OVertex;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class UIController extends Helpers {

    //public Label helloWorld;
    public Button follows_me;
    public ImageView profile_image;
    public MenuButton profile_settings;
    public ImageView follower_image;
    public ListView follower_details;
    public TextFlow chat;
    public Button profile_following;
    public ListView examples;
    public TextField insert_user;
    public Button plusButton;

    Helpers h = new Helpers();
    OVertex user;

    //public void sayHelloWorld(ActionEvent actionEvent) {helloWorld.setText("Hello World!");}

    public void follows_me_action(ActionEvent actionEvent) {

        System.out.println("Test");
        session();
        insert_user.setText(outputQueryAcc("SELECT FROM ACCOUNT"));
    }

    public void whoFollowsMe(ActionEvent actionEvent) {

    }

    public void onClickFollowButton(ActionEvent event) {
        String userToFollow = insert_user.getText();
        if (getVertexByUsername(userToFollow) != null) {
            OVertex followed = getVertexByUsername(userToFollow);
            followUser(user, followed);
        } else {
            System.out.println("Folgen fehlgeschlagen, user nicht vorhanden");
        }


    }




    //lollll
    //lllooooollll
    //lalala


}

