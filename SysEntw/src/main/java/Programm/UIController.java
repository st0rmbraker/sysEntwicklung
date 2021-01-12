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


    public ImageView profile_image;
    public MenuButton profile_settings;
    public TextFlow chat;
    public TextField insert_user;
    public Button plusButton;
    public ListView output_follower;
    public Button following_button;
    public Button me_following_button;
    public Button to_follow_button;
    public TextArea own_infos;
    public TextArea to_follow_infos;

    Helpers h = new Helpers();
    OVertex user;

    public void onClick_following_button(ActionEvent actionEvent) {
        output_follower.setText(listConnectedVertices(user,"OUT"));
    }

    public void onClick_me_following_button(ActionEvent actionEvent) {
        output_follower.setText(listConnectedVertices(user,"IN"));
    }


    //Der "+"-Button. Aktuell angemeldeter User folgt dem in dem Textfeld "insert_user" eingegeben Benutzernamen, wenn vorhanden.
    public void onClick_to_follow_button(ActionEvent event) {
        String userToFollow = insert_user.getText();
        if (getVertexByUsername(userToFollow) != null) {
            OVertex followed = getVertexByUsername(userToFollow);
            followUser(user, followed);
            System.out.println(user.getProperty("username")+" folgt jetzt "+userToFollow);
        } else {
            System.out.println("Folgen fehlgeschlagen, user "+userToFollow+"nicht vorhanden");
        }
    }

    public void setUser(OVertex user) {
        this.user = user;
    }
}

