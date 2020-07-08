package keyboy.controller;

import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class About {

    public void openWeb(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().browse(URI.create("http://www.abdulahad.net"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(MouseEvent mouseEvent) {
        try {
            Desktop.getDesktop().mail(URI.create("mailto:csabdulahad@gmail.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
