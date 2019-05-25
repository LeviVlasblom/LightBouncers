package lightbouncers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lightbouncers.gui.VisionFXViewport;

public class Main extends Application
{
    public static void Main(String[] args)
    {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        VisionFXViewport viewport = new VisionFXViewport(1920, 1080);

        Scene scene = new Scene(new Group(viewport, new Button("Test")), 1920, 1080);
        scene.setOnKeyPressed(event -> {
            viewport.onKeyPressed(event.getCode());
        });
        scene.setOnKeyReleased(event -> {
            viewport.onKeyReleased(event.getCode());
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
