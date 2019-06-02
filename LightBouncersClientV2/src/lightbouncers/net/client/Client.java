package lightbouncers.net.client;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import lightbouncers.Main;
import lightbouncers.game.Ball;
import lightbouncers.game.Player;
import lightbouncers.math.Vector2D;
import lightbouncers.net.Command;
import lightbouncers.net.GameData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client
{
    private static Client instance;
    private String host;
    private int port;
    private Socket clientSocket;
    private Thread listenerThread;

    private boolean isConnected;
    private boolean gameIsInProgress;
    private boolean objectMode;

    private ArrayList<String> lobby;

    private IClientReceiver clientReceiver;

    public Client(String host, int port, boolean objectMode)
    {
        this.host = host;
        this.port = port;
        this.gameIsInProgress = false;
        this.isConnected = false;
        this.objectMode = objectMode;
        this.lobby = new ArrayList<String>();
        this.listenerThread = new Thread(){
            public void run()
            {
                while(isConnected)
                {
                    listen();
                }
            }
        };
    }

    public boolean connect(String username)
    {
        try
        {
            if(!this.isConnected)
            {
                this.clientSocket = new Socket(this.host, this.port);
                this.isConnected = true;
                this.listenerThread.start();

                if(!this.objectMode)
                {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("command", "connect");
                    jsonObject.put("username", username);
                    send(jsonObject.toJSONString());
                }
                else
                {
                    Command command = new Command("connect", username);
                    send(command);
                }

                return true;
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            System.out.println("Could not connect to server!");
        }
        return this.isConnected;
    }

    public void disconnect()
    {
        try
        {
            if(this.isConnected)
            {
                System.out.println("Disconnected from server!");
                this.isConnected = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void listen()
    {
        try
        {
            if(!this.objectMode)
            {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                String serverInput = clientInput.readLine();

                if(serverInput != null)
                {
                    receive(serverInput);
                }
            }
            else
            {
                ObjectInputStream clientInput = new ObjectInputStream(this.clientSocket.getInputStream());
                Object serverInput = clientInput.readObject();

                if(serverInput != null)
                {
                    receive(serverInput);
                }
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            this.clientReceiver.onDisconnect();
            disconnect();
        }
    }

    public void receive(Object object)
    {
        try
        {
            if(!this.objectMode)
            {
                String data = (String)object;
                JSONObject jsonData = (JSONObject)(new JSONParser()).parse(data);

                String command = jsonData.get("command").toString();

                if(command.equals("startgame"))
                {
                    this.gameIsInProgress = true;

                    if(this.clientReceiver != null)
                    {
                        this.clientReceiver.onGameStart();
                    }
                }
                else if(command.equals("update"))
                {
                    JSONObject player1Json = (JSONObject)jsonData.get("player1");
                    JSONObject player2Json = (JSONObject)jsonData.get("player2");
                    JSONObject ballJson = (JSONObject)jsonData.get("ball");

                    Vector2D player1Position = new Vector2D(Double.parseDouble(player1Json.get("positionx").toString()), Double.parseDouble(player1Json.get("positiony").toString()));
                    Vector2D player2Position = new Vector2D(Double.parseDouble(player2Json.get("positionx").toString()), Double.parseDouble(player2Json.get("positiony").toString()));
                    Vector2D ballPosition = new Vector2D(Double.parseDouble(ballJson.get("positionx").toString()), Double.parseDouble(ballJson.get("positiony").toString()));
                    Player player1 = new Player(Color.BLUE, player1Json.get("username").toString());
                    player1.setPosition(player1Position);
                    Player player2 = new Player(Color.BLUE, player1Json.get("username").toString());
                    player2.setPosition(player2Position);
                    Ball ball = new Ball(ballPosition, 10, Color.ORANGE);

                    GameData gameData = new GameData(player1, player2, ball);
                    this.clientReceiver.onUpdate(gameData);
                }
                else if(command.equals("players"))
                {

                }
                else if(command.equals("addplayer"))
                {

                }
            }
            else
            {
                Command command = (Command)object;

                if(command.getCommand().equals("startgame"))
                {
                    this.gameIsInProgress = true;

                    if(this.clientReceiver != null)
                    {
                        this.clientReceiver.onGameStart();
                    }
                }
                else if(command.getCommand().equals("update"))
                {
                    GameData gameData = (GameData)object;
                    this.clientReceiver.onUpdate(gameData);
                }
                else if(command.getCommand().equals("players"))
                {

                }
                else if(command.getCommand().equals("addplayer"))
                {

                }
            }
            System.out.println("Client received: " + object);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public void send(Object object)
    {
        try
        {
            if(this.isConnected && this.clientSocket != null)
            {
                if(!objectMode)
                {
                    PrintWriter clientOutput = new PrintWriter(this.clientSocket.getOutputStream());
                    clientOutput.println((String)object);
                    clientOutput.flush();
                    System.out.println("Client send: " + object);
                }
                else
                {
                    ObjectOutputStream clientOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());
                    clientOutput.writeObject(object);
                    clientOutput.flush();
                    System.out.println("Client send: " + object);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("No connection found!");
        }
    }

    public static Client getInstance(String host, int port, boolean objectMode)
    {
        if(instance == null)
        {
            instance = new Client(host, port, objectMode);
        }
        return instance;
    }

    public static Client getInstance()
    {
        return instance;
    }

    public boolean isObjectMode()
    {
        return this.objectMode;
    }

    public void setClientReceiver(IClientReceiver clientReceiver)
    {
        this.clientReceiver = clientReceiver;
    }
}
