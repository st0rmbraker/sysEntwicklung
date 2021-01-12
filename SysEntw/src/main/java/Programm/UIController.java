package Programm;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.ORecord;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.record.impl.ORecordBytes;
import com.orientechnologies.orient.core.record.impl.ODocument;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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
    public Button change_info;

    Helpers h = new Helpers();
    OVertex user;
    Dialog <String[]> dialog;


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
            prepareCreateUser();
            Optional<String[]> result = dialog.showAndWait();

            createUser(result.get()[2], result.get()[0], result.get()[1]);
        }
    }

    public void prepareCreateUser(){
        // Eigenen JavaFX Dialog zum Nutzer erstellen
        dialog = new Dialog<>();
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

    public void onClick_change_info(){
        System.out.println("onClick_change_info");
        if(user.getProperty("userInfos")==null){
            TextInputDialog dialog = new TextInputDialog("Hier können sie weitere Informationen hinzufügen");
            dialog.setTitle("User Infos hinzufügen");
            dialog.setHeaderText("Look, a Text Input Dialog");
            dialog.setContentText("Please enter your name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                System.out.println("Your name: " + result.get());
                ODocument doc = new ODocument("userInfos");
                doc.field("name", result.get());
                doc.save();

            }
        }
        else{
            ODatabaseDocument info = user.getProperty("userInfos");
        }


    }

    public void onClick_following_button(ActionEvent actionEvent) {
        System.out.println("onClick_following_button");
        output_follower.setItems(prepareFollowers(user, "OUT"));
    }

    public void onClick_me_following_button(ActionEvent actionEvent) throws IOException {
//        System.out.println("onClick_me_following_button");
//        output_follower.setItems(prepareFollowers(user, "IN"));

        File file = new File("C:/Users/Alex/IdeaProjects/sysEntwicklung/SysEntw/src/main/resources/Haus.png");
        Image image = new Image(file.toURI().toString());

        ORecordId user = new ORecordId("#34:0");

        ;

        ODocument doc = new ODocument("Image");
        doc.field("binary", "C:/Users/Alex/IdeaProjects/sysEntwicklung/SysEntw/src/main/resources/Haus.png".getBytes());




        ByteArrayInputStream bai = new ByteArrayInputStream("/Haus.png".getBytes());

        BufferedImage bild = ImageIO.read(bai);
        Image realimage = SwingFXUtils.toFXImage(bild, null);
        profile_image.setImage(realimage);
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

