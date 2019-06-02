package lightbouncers.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lightbouncers.Main;
import lightbouncers.net.Command;
import lightbouncers.net.client.Client;
import org.json.simple.JSONObject;

import java.awt.*;

public class MenuView extends View
{
    public MenuView(int width, int height)
    {
        super(width, height);
    }

    @Override
    protected void setupScene()
    {
        TextField host = new TextField("localhost");
        TextField port = new TextField("25575");
        TextField username = new TextField();
        Button connectbutton = new Button("Connect");

        connectbutton.setOnAction(event -> {
            Main.username = username.getText();
            if(Client.getInstance(host.getText(), Integer.parseInt(port.getText()), false).connect(username.getText()))
            {
                System.out.println("Client connected!");

                GameView gameView = new GameView(this.width, this.height);
                gameView.display();
                this.close();
            }
        });

        VBox vbox = new VBox(host, port, username, connectbutton);

        Scene scene = new Scene(vbox, this.width, this.height);
        this.setScene(scene);
    }
}
