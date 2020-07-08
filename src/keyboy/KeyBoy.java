package keyboy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import keyboy.controller.Main;

import java.net.URISyntaxException;

public class KeyBoy extends Application {

    // preference keys
    public static final String PREF_ROOT_NODE = "ahad.keyboy";
    public static final String KEY_SHAPE = "shape";
    public static final String KEY_KEY = "key";
    public static final String KEY_CHORD = "chord";
    public static final String KEY_INPUT = "input";
    public static final String KEY_OUTPUT = "output";
    public static final String KEY_PIN = "pin";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // get am FXML loader & load it
        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout/main.fxml"));
        Parent root = loader.load();

        // get the controller and set the stage
        Main mainController = loader.getController();
        mainController.setStage(primaryStage);

        // set the icon of the application
        try {
            String path = KeyBoy.class.getResource("icon/keyboy.png").toURI().toString();
            primaryStage.getIcons().add(new Image(path));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // customize the window
        primaryStage.setTitle("Key Boy 1.0.7");
        primaryStage.setResizable(false);

        primaryStage.setScene(new Scene(root, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
