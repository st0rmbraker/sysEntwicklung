package Programm;

import com.orientechnologies.orient.core.record.OVertex;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;

public class UIController extends Helpers{

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

    public void onClickFollowButton(ActionEvent actionEvent)
    {
//        String userToFollow = insert_user.getText();
//        if(h.getVertexByUsername(userToFollow) != null ?! h.getVertexByUsername())
//        h.followUser()
        System.out.println(user.getProperty("firstName").toString());

    }


    public void test(String test)
    {
        System.out.println(test);
    }




    //lollll
    //lllooooollll
    //lalala


    public void setUser(OVertex user) {
        this.user = user;
    }
}

