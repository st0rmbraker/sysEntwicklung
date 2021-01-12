package Programm;

import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public TextField insert_user;
    public Button plusButton;
    public ListView output_follower;
    public Button following_button;
    public Button me_following_button;
    public Button to_follow_button;
    public TextArea own_infos;
    public TextArea to_follow_infos;
    public TextArea output_chat;

    Helpers h = new Helpers();
    OVertex user;



    //Der "+"-Button. Aktuell angemeldeter User folgt dem in dem Textfeld "insert_user" eingegeben Benutzernamen, wenn vorhanden.
    public void onClickFollowButton(ActionEvent event) {
        String userToFollow = insert_user.getText();
        if (getVertexByUsername(userToFollow) != null) {
            OVertex followed = getVertexByUsername(userToFollow);
            followUser(user, followed);
        } else {
            System.out.println("Folgen fehlgeschlagen, user nicht vorhanden");
        }
    }

    public void setUser(String user) {
        if(checkUserExists(user)){
            this.user = h.getVertexByUsername(user);
        }
        else{
            createUser(
                JOptionPane.showInputDialog(null,"Firstname:"
                    JOptionPane.DEFAULT_OPTION),
                JOptionPane.showInputDialog(null,"Lastname:",
                    JOptionPane.DEFAULT_OPTION),
                JOptionPane.showInputDialog(null,"username",
                    JOptionPane.DEFAULT_OPTION)
            );
        }

    }


    //Stellt Liste der Follower in Richtung Rein oder Raus bereit
    public ObservableList<String> prepareFollowers(OVertex element, String direction){
        session();
        ObservableList<String> ret = FXCollections.observableArrayList();
        ret.add("Start");
        Iterable<OVertex> overtexList;
        System.out.println("test");
        if(direction.equals("IN")) {
            overtexList = element.getVertices(ODirection.IN); //Gibt auch noch IN und BOTH fuer die Richtungen
        }
        else {
            overtexList = element.getVertices(ODirection.OUT); //Gibt auch noch IN und BOTH fuer die Richtungen
        }
        for(OVertex v : overtexList) {
            ret.add("-" + v.getProperty("firstName").toString() + " " + v.getProperty("username").toString());
        }
        return ret;

    }

    /*
    public void onClickFollowerDetails(javafx.scene.input.MouseEvent mouseEvent) {
        System.out.println("clicked on " + follower_details.getSelectionModel().getSelectedItem());
    }
    */

    public void onClick_following_button(ActionEvent actionEvent) {
        System.out.println("onClick_following_button");
        output_follower.setItems(prepareFollowers(user, "OUT"));
    }

    public void onClick_me_following_button(ActionEvent actionEvent) {
        System.out.println("onClick_me_following_button");
        output_follower.setItems(prepareFollowers(user, "IN"));
    }


    //Der "+"-Button. Aktuell angemeldeter User folgt dem in dem Textfeld "insert_user" eingegeben Benutzernamen, wenn vorhanden.
    public void onClick_to_follow_button(ActionEvent event) {
        System.out.println("onClick_to_follow_button");
        String userToFollow = insert_user.getText();
        if (getVertexByUsername(userToFollow) != null) {
            OVertex followed = getVertexByUsername(userToFollow);
            followUser(user, followed);
            System.out.println(user.getProperty("username")+" folgt jetzt "+userToFollow);
            output_follower.setItems(prepareFollowers(user, "OUT"));
        } else {
            System.out.println("Folgen fehlgeschlagen, user "+userToFollow+"nicht vorhanden");
        }
    }
}

