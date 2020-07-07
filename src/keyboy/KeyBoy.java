package keyboy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

public class KeyBoy extends Application {

    // preference keys
    public static final String PREF_ROOT_NODE = "ahad.keyboy";
    public static final String KEY_SHAPE = "shape";
    public static final String KEY_KEY = "key";
    public static final String KEY_CHORD = "chord";
    public static final String KEY_INPUT = "input";
    public static final String KEY_OUTPUT = "output";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout/layout_main.fxml"));
        primaryStage.setTitle("Key Boy");
        primaryStage.setScene(new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
