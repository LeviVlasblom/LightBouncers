package lightbouncers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lightbouncers.gui.Viewport;
import lightbouncers.net.client.Client;
import lightbouncers.world.LevelBuilder;
import lightbouncers.world.World;
import org.json.simple.JSONObject;

import java.io.File;

public class Main extends Application
{
    public static String username;

    public static void Main(String[] args)
    {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        TextField textField = new TextField();
        Button button = new Button("Submit");
        Button readyButton = new Button("Not ready!");

        button.setOnAction(event -> {
            Main.username = textField.getText();

            Viewport viewport = new Viewport(800, 600);

            Scene scene = new Scene(new Group(viewport, readyButton), 800, 600);
            scene.setOnKeyPressed(event1 -> {
                viewport.onKeyPressed(event1);
            });
            scene.setOnKeyReleased(event1 -> {
                viewport.onKeyReleased(event1);
            });

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            primaryStage.close();
        });

        readyButton.setOnAction(event -> {
            if(readyButton.getText().equals("Not ready!"))
            {
                JSONObject readyPlayerJSON = new JSONObject();
                readyPlayerJSON.put("command", "ready");
                readyPlayerJSON.put("username", username);
                Client.getInstance("", 0, null, null).sendUTF(readyPlayerJSON.toJSONString());
            }

            readyButton.setText("Ready");
        });

        Scene test = new Scene(new VBox(textField, button), 500, 600);
        primaryStage.setScene(test);
        primaryStage.show();

//        Viewport viewport = new Viewport(1920, 1080);
//
//        Scene scene = new Scene(new Group(viewport), 1920, 1080);
////        scene.setOnKeyPressed(event -> viewport.onKeyPressed(event));
////        scene.setOnKeyReleased(event -> viewport.onKeyReleased(event));
//        scene.setOnKeyPressed(event -> {
//            viewport.onKeyPressed(event);
//        });
//        scene.setOnKeyReleased(event -> {
//            viewport.onKeyReleased(event);
//        });
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }
}
