package Programm;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class UIController {

    public Label helloWorld;
    public Button button2;
    public Button hanswurst;

    public void sayHelloWorld(ActionEvent actionEvent) {
        helloWorld.setText("Hello World!");
    }

    public void testbuitton(ActionEvent actionEvent) {
        hanswurst.setText("hhh");
    }
}
