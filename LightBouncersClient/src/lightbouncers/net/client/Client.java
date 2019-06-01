package lightbouncers.net.client;

import javafx.util.Pair;
import lightbouncers.Main;
import lightbouncers.math.Vector2D;
import lightbouncers.net.PlayerObject;
import lightbouncers.net.ProjectileObject;
import lightbouncers.net.SessionData;
import lightbouncers.objects.pawns.Pawn;
import lightbouncers.objects.pawns.characters.LightBouncer;
import lightbouncers.objects.pawns.characters.PlayerCharacter;
import lightbouncers.objects.pawns.projectiles.Projectile;
import lightbouncers.world.LevelBuilder;
import lightbouncers.world.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Client
{
    private static Client instance;
    private ITCPClientReceiver receiver;
    private ILobbyUpdate lobbyUpdate;
    private String host;
    private int port;
    private Socket clientSocket;
    private Thread listenerThread;
    private Thread gameThread;

    private boolean readWriteObjectMode;
    private boolean gameIsInProgress;
    private boolean isConnected;

    private World world;
    private ArrayList<String> lobby;

    private Client(String host, int port, ITCPClientReceiver receiver, ILobbyUpdate lobbyUpdate)
    {
        this.host = host;
        this.port = port;
        this.readWriteObjectMode = false;
        this.gameIsInProgress = false;
        this.isConnected = false;
        this.receiver = receiver;
        this.lobbyUpdate = lobbyUpdate;
        this.lobby = new ArrayList<String>();
        this.listenerThread = new Thread("ClientSocketListener"){
            public void run()
            {
                while(isConnected)
                {
                    listen();
                }
            }
        };

        this.gameThread = new Thread(){
            public void run()
            {
                while(isConnected)
                {
                    if(gameIsInProgress)
                    {
                        if(world != null)
                        {
//                            try {
//                                Thread.sleep(2);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            PlayerCharacter playerCharacter = world.getPlayer();
                            PlayerObject playerObject = new PlayerObject(playerCharacter.getWorldPosition(), playerCharacter.getVelocity(), playerCharacter.getRadius(), Main.username, playerCharacter.getRotation());
                            if(!readWriteObjectMode)
                            {
                                JSONObject jsonObject = SessionJSONUtil.getPlayerObjectJson(playerObject);
                                jsonObject.put("command", "updateposition");
                                sendUTF(jsonObject.toJSONString());
                                //sends.add(jsonObject.toJSONString());
                            }
                            else
                            {
                                sendObject(playerObject);
                            }
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
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

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "setusername");
                jsonObject.put("username", username);
                sendUTF(jsonObject.toJSONString());

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
            if(!this.readWriteObjectMode)
            {
                BufferedReader clientInput = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                DataOutputStream clientOutput = new DataOutputStream(this.clientSocket.getOutputStream());

                String serverInput = clientInput.readLine();

                if(serverInput != null)
                {
                    receiveUTFData(serverInput);
                }
            }
            else
            {
                ObjectInputStream clientInput = new ObjectInputStream(this.clientSocket.getInputStream());
                ObjectOutputStream clientOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());

                Object serverInput = clientInput.readObject();

                if(serverInput != null)
                {
                    receiveObjectData(serverInput);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            disconnect();
        }
    }

    public void receiveUTFData(String data)
    {
        if(!data.isEmpty())
        {
//            if(this.receiver != null)
//            {
//                this.receiver.receive(data);
//            }
            handleReceiveUTF(data);
            System.out.println("Client received: " + data);
        }
    }

    public void receiveObjectData(Object data)
    {
        if(data != null)
        {
            if(this.receiver != null)
            {
                this.receiver.receive(data);
            }
            System.out.println("Client received: " + data);
        }
    }

    public void sendUTF(String data)
    {
        try
        {
            if(this.isConnected)
            {
                DataOutputStream clientOutput = new DataOutputStream(this.clientSocket.getOutputStream());
                if(this.clientSocket != null)
                {
                    clientOutput.writeBytes(data + "\n");
                    System.out.println("Client send: " + data);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("No connection found!");
        }
    }

    public void sendObject(Object data)
    {
        try
        {
            if(this.isConnected)
            {
                ObjectOutputStream clientOutput = new ObjectOutputStream(this.clientSocket.getOutputStream());
                if(this.clientSocket != null)
                {
                    clientOutput.writeObject(data);
                    System.out.println("Client send: " + data);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("No connection found!");
        }
    }

    public void handleReceiveUTF(String data)
    {
        JSONParser jsonParser = new JSONParser();
        try
        {
            JSONObject jsonObject = (JSONObject)jsonParser.parse(data);
            String command = jsonObject.get("command").toString();

            if(command.equals("update"))
            {
                JSONArray players = (JSONArray)jsonObject.get("players");
                JSONArray projectiles = (JSONArray)jsonObject.get("projectiles");

                for(Object playerJSON : players)
                {
                    JSONObject playerObject = (JSONObject)playerJSON;
                    Vector2D position = new Vector2D(Double.parseDouble(playerObject.get("positionx").toString()), Double.parseDouble(playerObject.get("positiony").toString()));
                    Vector2D velocity = new Vector2D(Double.parseDouble(playerObject.get("velocityx").toString()), Double.parseDouble(playerObject.get("velocityy").toString()));
                    String username = playerObject.get("username").toString();
                    double rotation = Double.parseDouble(playerObject.get("rotation").toString());

                    if(world != null && !world.getPlayer().getName().equals(username))
                    {
                        for(Pawn playerActor : this.world.getPlayerActors())
                        {
                            if(playerActor.getName().equals(username))
                            {
                                playerActor.setWorldPosition(position);
                                playerActor.setVelocity(velocity);
                                playerActor.setRotation(rotation);
                                //playerActor.setDirection(velocity.normalized());
                            }
                        }
                    }
                }

                for(Object projectileJSON : projectiles)
                {
                    JSONObject projectileObject = (JSONObject)projectileJSON;
                    Vector2D position = new Vector2D(Double.parseDouble(projectileObject.get("positionx").toString()), Double.parseDouble(projectileObject.get("positiony").toString()));
                    Vector2D velocity = new Vector2D(Double.parseDouble(projectileObject.get("velocityx").toString()), Double.parseDouble(projectileObject.get("velocityy").toString()));
                    String username = projectileObject.get("username").toString();

                    if(!world.getPlayer().getName().equals(username))
                    {
                        for(Projectile projectile : this.world.getProjectiles())
                        {
                            //projectile.setWorldPosition(position);
                            projectile.setVelocity(velocity);
                            //projectile.setDirection(velocity.normalized());
                        }
                    }
                }
            }
            else if(command.equals("addplayer"))
            {
                String username = jsonObject.get("username").toString();
                if(!Main.username.equals(username))
                {
                    this.lobby.add(username);
                }
                System.out.println("Player " + username + " connected to lobby!");

            }
            else if(command.equals("addprojectle"))
            {
                Vector2D position = new Vector2D(Double.parseDouble(jsonObject.get("positionx").toString()), Double.parseDouble(jsonObject.get("positiony").toString()));
                Vector2D velocity = new Vector2D(Double.parseDouble(jsonObject.get("velocityx").toString()), Double.parseDouble(jsonObject.get("velocityy").toString()));
                double radius = Double.parseDouble(jsonObject.get("radius").toString());
                String username = jsonObject.get("username").toString();
                Projectile projectile = new Projectile(position, Vector2D.getAngle(velocity), this.world, 20.0, 400.0, 1.0, velocity.normalized(), radius);
                projectile.setName(username);
                this.world.addProjectile(projectile);
            }
            else if(command.equals("startmatch"))
            {
                this.world = new World();
                this.world.changeLevel(LevelBuilder.loadLevelFromFile(new File("src/lightbouncers/resources/levels/LevelStandard.json"), world));
                this.world.setPlayer(new LightBouncer(new Vector2D(100, 100), 0.0, world, 2.0, 40.0, 1.0, Main.username));

                for(String username : this.lobby)
                {
                    LightBouncer lightBouncer = new LightBouncer(Vector2D.zero(), 0.0, world, 2.0, 40.0, 1.0, username);
                    this.world.addPlayerActor(lightBouncer);
                }
                this.gameIsInProgress = true;
                lobbyUpdate.onMatchStart(this.world);
                this.gameThread.start();
            }
            else if(command.equals("endmatch"))
            {
                this.world.setPlayer(null);
                this.world.getPlayerActors().clear();
                this.world.getProjectiles().clear();
                this.gameIsInProgress = false;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public void receive(Object data)
    {
        SessionData sessionData = (SessionData)data;

        for(PlayerObject playerObject : sessionData.getPlayers())
        {
            if(!this.world.getPlayer().getName().equals(playerObject.getUsername()))
            {
                for(Pawn playerActor : this.world.getPlayerActors())
                {
                    if(playerActor.getName().equals(playerObject.getUsername()))
                    {
                        //playerActor.setWorldPosition(playerObject.getPosition());
                        playerActor.setVelocity(playerObject.getVelocity());
                        playerActor.setDirection(playerObject.getVelocity().normalized());
                        playerActor.setRotation(playerObject.getRotation());
                    }
                }
            }
        }

        for(ProjectileObject projectileObject : sessionData.getProjectiles())
        {
            for(Projectile projectile : this.world.getProjectiles())
            {
                if(projectile.getName().equals(projectileObject.getUsername()))
                {
                    //projectile.setWorldPosition(projectileObject.getPosition());
                    projectile.setVelocity(projectileObject.getVelocity());
                    projectile.setDirection(projectileObject.getVelocity().normalized());
                }
            }
        }
    }

    public ArrayList<String> getLobby()
    {
        return this.lobby;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }

    public static Client getInstance(String host, int port, ITCPClientReceiver receiver, ILobbyUpdate lobbyUpdate)
    {
        if(instance == null)
        {
            instance = new Client(host, port, receiver, lobbyUpdate);
        }
        return instance;
    }
}
