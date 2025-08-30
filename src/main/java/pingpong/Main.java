package pingpong;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Pingpong using FXML.
 */
public class Main extends Application {

    private Pingpong pingpong = new Pingpong("./data/pingpong.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Pingpong");
            fxmlLoader.<MainWindow>getController().setPingpong(pingpong); // inject the Pingpong instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
