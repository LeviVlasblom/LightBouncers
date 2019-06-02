package lightbouncers.ui;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lightbouncers.game.Actor;
import lightbouncers.game.Ball;
import lightbouncers.game.Player;
import lightbouncers.lights.Light;
import lightbouncers.math.Vector2D;
import lightbouncers.menu.MultiplayerMenu;
import lightbouncers.net.Command;
import lightbouncers.net.GameData;
import lightbouncers.net.client.Client;
import lightbouncers.net.client.IClientReceiver;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class GameView extends View implements IClientReceiver
{
    private Canvas canvas;
    private Client client;
    private Button readybutton;

    //Game
    private Player player1;
    private Player player2;
    private Ball ball;
    private Light light;

    public GameView(int width, int height)
    {
        super(width, height);
        this.player1 = new Player(Color.BLUE, "Test1");
        this.player2 = new Player(Color.BLUE, "Test2");

        this.canvas = new Canvas();
        this.canvas.setWidth(width);
        this.canvas.setHeight(height);
        this.client = Client.getInstance();
        this.client.setClientReceiver(this);
        this.ball = new Ball(new Vector2D(this.width / 2, this.height / 2), 10, Color.ORANGE);
        this.light = new Light(this.ball.getPosition(), 0, Math.PI * 2, 400, 1.0, Color.rgb(130, 68, 255, 0.1),  Color.rgb(5, 121, 255, 1.0));
    }

    @Override
    protected void setupScene()
    {
        this.readybutton = new Button("Ready up!");
        this.readybutton.setFont(new Font("Arial", 20));
        this.readybutton.setPrefWidth(200);
        this.readybutton.setPrefHeight(100);
        this.readybutton.setTranslateX((this.width / 2) - (this.readybutton.getPrefWidth() / 2) - 20);
        this.readybutton.setTranslateY((this.height / 2) - (this.readybutton.getPrefHeight() / 2) - 20);

        this.readybutton.setOnAction(event -> {
            sendReady();
            this.readybutton.setText("Ready");
            this.readybutton.setDisable(true);
        });

        Scene scene = new Scene(new Group(this.canvas, readybutton), this.width, this.height);

        scene.setOnKeyPressed(event -> {
            onKeyPressed(event);
        });
        scene.setOnKeyReleased(event -> {
            onKeyReleased(event);
        });

        this.setScene(scene);
    }

    private void sendReady()
    {
        if(!this.client.isObjectMode())
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", "ready");
            this.client.send(jsonObject.toJSONString());
        }
        else
        {
            Command command = new Command("command", "ready");
            this.client.send(command);
        }
    }

    private void onKeyPressed(KeyEvent event)
    {
        if(event.getCode() == KeyCode.A)
        {
            if(!this.client.isObjectMode())
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "startmoveleft");
                this.client.send(jsonObject.toJSONString());
            }
            else
            {
                Command command = new Command("command", "startmoveleft");
                this.client.send(command);
            }
        }
        if(event.getCode() == KeyCode.D)
        {
            if(!this.client.isObjectMode())
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "startmoveright");
                this.client.send(jsonObject.toJSONString());
            }
            else
            {
                Command command = new Command("command", "startmoveright");
                this.client.send(command);
            }
        }
    }

    private void onKeyReleased(KeyEvent event)
    {
        if(event.getCode() == KeyCode.A)
        {
            if(!this.client.isObjectMode())
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "stopmoveleft");
                this.client.send(jsonObject.toJSONString());
            }
            else
            {
                Command command = new Command("command", "stopmoveleft");
                this.client.send(command);
            }
        }
        if(event.getCode() == KeyCode.D)
        {
            if(!this.client.isObjectMode())
            {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "stopmoveright");
                this.client.send(jsonObject.toJSONString());
            }
            else
            {
                Command command = new Command("command", "stopmoveright");
                this.client.send(command);
            }
        }
    }

    private void clear()
    {
        GraphicsContext graphicsContext = this.canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, this.width, this.height);
    }

    private void draw()
    {
//        clear();
//        GraphicsContext graphicsContext = this.canvas.getGraphicsContext2D();
//        this.player1.draw(graphicsContext);
//        this.player2.draw(graphicsContext);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clear();
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                player1.draw(graphicsContext);
                player2.draw(graphicsContext);
                light.draw(graphicsContext, new ArrayList<Actor>(Arrays.asList(player1, player2)));
                ball.draw(graphicsContext);
            }
        });
//        this.ball.draw(graphicsContext);
    }

    @Override
    public void onGameStart()
    {
        this.readybutton.setVisible(false);
    }

    @Override
    public void onGameEnd()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clear();
                readybutton.setText("Ready up!");
                readybutton.setDisable(false);
                readybutton.setVisible(true);
            }
        });
    }

    @Override
    public void onDisconnect()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                MultiplayerMenu menuView = new MultiplayerMenu("Lightbouncers",1280, 720);
                menuView.show();
                close();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Connection terminated!", ButtonType.OK);
                alert.show();
            }
        });
    }

    @Override
    public void onUpdate(GameData gameData)
    {
        this.player1.setPosition(gameData.getPlayer1().getPosition());
        this.player2.setPosition(gameData.getPlayer2().getPosition());
        this.ball.setPosition(gameData.getBall().getPosition());
        this.light.setPosition(gameData.getBall().getPosition());
        draw();
    }
}
