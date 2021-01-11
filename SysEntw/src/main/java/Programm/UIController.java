package Programm;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;

public class UIController {

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

    //public void sayHelloWorld(ActionEvent actionEvent) {helloWorld.setText("Hello World!");}

    public void follows_me_action(ActionEvent actionEvent) {

        System.out.println("Test");
        h.session();
        insert_user.setText(h.outputQueryAcc("SELECT FROM ACCOUNT"));
    }

    public void whoFollowsMe(ActionEvent actionEvent) {

    }

    public void onClickFollowButton(ActionEvent actionEvent)
    {

    }


    public void test(String test)
    {
        System.out.println(test);
    }


    //lollll
    //lllooooollll
    //lalala




}

