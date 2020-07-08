package keyboy.helper;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import keyboy.KeyBoy;

import java.io.IOException;
import java.net.URISyntaxException;

public class StageHelper {

    public static void showStage(String title, String layout, boolean resizable, Stage parentStage, boolean hideParent) {
        Stage stage = new Stage();
        try {

            Class who = KeyBoy.class;

            FXMLLoader loader = new FXMLLoader(who.getResource(layout));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);

            // set the icon of the application
            try {
                String path = KeyBoy.class.getResource("icon/keyboy.png").toURI().toString();
                stage.getIcons().add(new Image(path));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            if (hideParent) {
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        stage.show();
                    }
                });
                stage.hide();
            }

            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setResizable(resizable);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeStage(Node node) {
        ((Stage) node.getScene().getWindow()).close();
    }

}
