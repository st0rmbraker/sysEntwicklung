package Programm;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.record.impl.ODocument;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

public class UIController extends Helpers {


    public ImageView profile_image;
    public MenuButton profile_settings;
    public TextField insert_user;
    public ListView output_follower;
    public Button following_button;
    public Button me_following_button;
    public Button to_follow_button;
    public TextArea own_infos;
    public TextArea to_follow_infos;
    public TextArea output_chat;
    public MenuItem setting_1;
    public MenuItem setting_2;
    public Button send_button;
    public TextField input_chat;

    public boolean me_following = true;
    public ImageView chat_Partner;
    Helpers h = new Helpers();
    OVertex user;
    @FXML
    OVertex chatPartner;
    Dialog <String[]> dialog;
    Dialog <String[]> dialogInfos;
    ODatabaseSession db;

    public Helpers getHelpers(){
        return h;
    }

    public void dbcon() throws InterruptedException {
        db = new DBcon().getDb();
    }


    public void setUser(String user) throws InterruptedException {
        db = new DBcon().getDb();
        if(checkUserExists(user, db)){
            this.user = h.getVertexByUsername(user, db);
            if(getPictureByUser(this.user) != null) {
                profile_image.setImage(convertToImg(getPictureByUser(this.user)));
            }
        }
        else{
            prepareCreateUser();
            Optional<String[]> result = dialog.showAndWait();

            createUser(result.get()[2], result.get()[0], result.get()[1], result.get()[3], db);
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
        TextField nFilePath = new TextField();
        nFilePath.setPromptText("Dateipfad zu Profilbild");

        grid.add(new Label("Vorname:"), 0, 0);
        grid.add(nFirstName, 1, 0);
        grid.add(new Label("Nachname:"), 0, 1);
        grid.add(nLastName, 1, 1);
        grid.add(new Label("Benutzername:"), 0, 2);
        grid.add(nUsername, 1, 2);
        grid.add(new Label("Dateipfad:"), 0, 3);
        grid.add(nFilePath, 1, 3);

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
                return new String[]{nFirstName.getText(), nLastName.getText(), nUsername.getText(), nFilePath.getText()};
            }
            return null;
        });
    }
    //Stellt Liste der Follower in Richtung Rein oder Raus bereit
    public ObservableList<String> prepareFollowers(OVertex element, String direction){
        session();
        ObservableList<String> ret = FXCollections.observableArrayList();
        Iterable<OVertex> overtexList;
        if(direction.equals("IN")) {
            overtexList = element.getVertices(ODirection.IN); //Gibt auch noch IN und BOTH fuer die Richtungen
        }
        else {
            overtexList = element.getVertices(ODirection.OUT); //Gibt auch noch IN und BOTH fuer die Richtungen
        }
        for(OVertex v : overtexList) {
            ret.add("-" + v.getProperty("firstName").toString() + " "+v.getProperty("lastName")+ " |" + v.getProperty("username").toString()+"|");
        }
        return ret;

    }

    public void onClick_setting_1(ActionEvent actionEvent){

        System.out.println("onClick_change_info");

        dialogInfos = new Dialog<>();
        dialogInfos.setTitle("Benutzerdaten ändern");
        dialogInfos.setHeaderText("Ändert die Benutzerdaten.");

        // Set the button types
        ButtonType okButton = new ButtonType("Bestätigen", ButtonBar.ButtonData.OK_DONE);
        dialogInfos.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nVariable = new TextField();
        nVariable.setPromptText("Eigenschaft:");
        TextField nValues = new TextField();
        nValues.setPromptText("Werte:");


        grid.add(new Label("Eigenschaft:"), 0, 0);
        grid.add(nVariable, 1, 0);
        grid.add(new Label("Werte:"), 0, 1);
        grid.add(nValues, 1, 1);

        dialogInfos.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> nVariable.requestFocus());
        nVariable.getText();
        dialogInfos.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return new String[]{nVariable.getText(), nValues.getText()};
            }
            return null;
        });

        Optional<String[]> result = dialogInfos.showAndWait();

        if (result.isPresent()){
            session();
            db.activateOnCurrentThread();
            ODocument doc;
            if(user.getProperty("userInfos")==null){
                doc = new ODocument("userInfos");
            }else {
                doc = user.getProperty("userInfos");
            }
            doc.field(result.get()[0], result.get()[1]);
            db.save(doc);
            user.setProperty("userInfos", doc);
            user.save();
        }
    }

    public void onClick_send_button(ActionEvent actionEvent){
        sendMessage();
    }

    public void sendMessage()
    {
        if(chatPartner!=null && input_chat.getText()!=null){
            ODocument chat = getChat(user, chatPartner, db);
            createMessage(user, input_chat.getText(), chat, db);
            input_chat.clear();
        }
        else if(input_chat.getText()==null){
            createAlert("Senden fehlgeschlagen", "Senden fehlgeschlagen", "Sie können keine leeren Nachrichten schicken.");
        }
    }

    public void onClick_following_button(ActionEvent actionEvent) {
        System.out.println("onClick_following_button");
        output_follower.setItems(prepareFollowers(user, "OUT"));
        following_button.setDisable(true);
        me_following_button.setDisable(false);
        me_following = true;
        System.out.println("Button");
    }

    public boolean getMe_Following(){
        return me_following;
    }

    public void onClick_me_following_button(ActionEvent actionEvent) throws IOException {
        System.out.println("onClick_me_following_button");
        output_follower.setItems(prepareFollowers(user, "IN"));
        me_following_button.setDisable(true);
        following_button.setDisable(false);
        me_following = false;
    }

    //Der "+"-Button. Aktuell angemeldeter User folgt dem in dem Textfeld "insert_user" eingegeben Benutzernamen, wenn vorhanden.
    public void onClick_to_follow_button(ActionEvent event) {
        System.out.println("onClick_to_follow_button");
        String userToFollow = insert_user.getText();
        OVertex toFollow = getVertexByUsername(userToFollow, db);
        if (toFollow != null || !toFollow.getProperty("username").toString().equals(userToFollow)) {
            if(followUser(user, toFollow, db)) {
                System.out.println(user.getProperty("username") + " folgt jetzt " + userToFollow);
                output_follower.setItems(prepareFollowers(user, "OUT"));
            }
            else {
                createAlert("Fehler", "Fehler", "User folgt bereits");
            }
        }
        else{
            createAlert("Fehler", "Fehler", "Selbst folgen ist traurig, hol dir Freunde");
        }
    }

    public void createAlert(String title, String header, String text)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        alert.showAndWait();
    }

    public void getListViewItem(javafx.scene.input.MouseEvent mouseEvent) {
        //Holt Textinhalt von ListView click
        if(output_follower.getSelectionModel().getSelectedItem()!=null){
            send_button.setDisable(false);
            String string = output_follower.getSelectionModel().getSelectedItem().toString();

            //Splittet den String bei den | vor und nach dem usernamen => result[1] ist der nutzername
            String[] result = string.split(Pattern.quote("|"));
            System.out.println("Clicked on: " + result[1]);

            //chatPartner wird aktualisiert
            chatPartner = getVertexByUsername(result[1], db);
            try {
                if (convertToImg(getPictureByUser(chatPartner)) != null) {
                    chat_Partner.setImage(convertToImg(getPictureByUser(chatPartner)));
                    chat_Partner.setVisible(true);
                } else {
                    chat_Partner.setVisible(false);
                }
            }
            catch (Exception e)
            {
                chat_Partner.setVisible(false);
            }
            to_follow_infos.setText(printUserInfo(chatPartner, db));
        }
        else {
            chatPartner = null;
            to_follow_infos.setText("");
            send_button.setDisable(true);
        }
    }

    public void onClick_setting_2(ActionEvent actionEvent) {
    }

    public void onEnter(ActionEvent actionEvent) {
        sendMessage();
    }
}

