package javafx;
//https://www.jetbrains.com/help/idea/developing-a-javafx-application-examples.html#fix-the-code

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class SampleController {

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
