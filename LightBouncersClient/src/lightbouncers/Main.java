package lightbouncers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lightbouncers.gui.Viewport;
import lightbouncers.net.client.Client;

public class Main extends Application
{
    public static void Main(String[] args)
    {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        //Client client = new Client("localhost", 4509, null);
        //client.connect();
        Viewport viewport = new Viewport(1920, 1080);

        Scene scene = new Scene(new Group(viewport), 1920, 1080);
//        scene.setOnKeyPressed(event -> viewport.onKeyPressed(event));
//        scene.setOnKeyReleased(event -> viewport.onKeyReleased(event));
        scene.setOnKeyPressed(event -> {
            viewport.onKeyPressed(event);
        });
        scene.setOnKeyReleased(event -> {
            viewport.onKeyReleased(event);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
