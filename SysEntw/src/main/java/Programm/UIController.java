package Programm;

import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import java.io.IOException;
import java.util.Optional;

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
            // Eigenen JavaFX Dialog zum Nutzer erstellen
            Dialog <String[]> dialog = new Dialog<>();
            dialog.setTitle("Neuen Nutzer erstellen");
            dialog.setHeaderText("Neuen Nutzer erstellen");

            //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

            // Set the button types.
            ButtonType create = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(create, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nFirstName = new TextField();
            nFirstName.setPromptText("Vorname");
            TextField nLastName = new TextField();
            nLastName.setPromptText("Nachname");
            TextField nUsername = new TextField();
            nUsername.setPromptText("Benutzername");

            grid.add(new Label("Vorname:"), 0, 0);
            grid.add(nFirstName, 1, 0);
            grid.add(new Label("Nachname:"), 0, 1);
            grid.add(nLastName, 1, 1);
            grid.add(new Label("Benutzername:"), 0, 2);
            grid.add(nUsername, 1, 2);

            // Enable/Disable login button depending on whether a username was entered.
            Node loginButton = dialog.getDialogPane().lookupButton(create);
            loginButton.setDisable(true);

            // Do some validation (using the Java 8 lambda syntax).
            nUsername.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton.setDisable(newValue.trim().isEmpty());
            });

            dialog.getDialogPane().setContent(grid);

            // Request focus on the username field by default.
            Platform.runLater(() -> nFirstName.requestFocus());
            nFirstName.getText();
            // Convert the result to a username-password-pair when the login button is clicked.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == create) {
                    return new String[]{nFirstName.getText(), nLastName.getText(), nUsername.getText()};
                }
                return null;
            });

            Optional<String[]> result = dialog.showAndWait();

           /*
            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Benutzername eingeben");
            dialog.setHeaderText("Bitte geben sie ihren Benutzernamen ein.");
            dialog.setContentText("Bitte Namen eingeben:");
            Optional<String> userC = dialog.showAndWait();
            user = userC.get();
*/
            createUser(result.get()[2], result.get()[0], result.get()[1]);
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
        }
        else{
            System.out.println("Folgen fehlgeschlagen, user "+userToFollow+" nicht vorhanden");
        }
    }
}

