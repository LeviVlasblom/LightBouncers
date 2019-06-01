package lightbouncers.net.server;

import javafx.util.Pair;
import lightbouncers.math.Vector2D;
import lightbouncers.net.PlayerObject;
import lightbouncers.net.ProjectileObject;
import lightbouncers.net.SessionData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Session
{
    private int maxPlayerCount;
    private HashMap<Socket, PlayerDataManager> sessionData;

    private Thread sessionThread;
    private Server server;
    private boolean gameIsInProgress;

    private JSONParser jsonParser;
    private boolean readWriteObjectMode;
    private boolean sessionIsConnected;
    public Queue<String> broadcasts;
    public Queue<Pair<String, Socket>> sends;
    public Queue<Pair<String, Socket>> receives;

    public Session(Server server, int maxPlayerCount)
    {
        this.server = server;
        this.maxPlayerCount = maxPlayerCount;
        this.sessionData = new HashMap<Socket, PlayerDataManager>();
        this.gameIsInProgress = false;
        this.jsonParser = new JSONParser();
        this.readWriteObjectMode = false;
        this.sessionIsConnected = true;
        this.broadcasts = new LinkedList<String>();
        this.sends = new LinkedList<Pair<String, Socket>>();
        this.receives = new LinkedList<Pair<String, Socket>>();
        this.sessionThread = new Thread(){
            public void run()
            {
                while(sessionIsConnected)
                {
//                    try {
//                        Thread.sleep(2);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    //System.out.println("Session size: " + sessionData.size() + " is in progress: " + gameIsInProgress + " are palyers ready : " + arePlayersReady());
                    if(sessionData.size() > 1 && !gameIsInProgress && arePlayersReady())
                    {
                        if(!readWriteObjectMode)
                        {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("command", "startmatch");
                            broadcasts.add(jsonObject.toJSONString());
                            //broadcastUTF(jsonObject.toJSONString());
                            gameIsInProgress = true;
                        }
                        else
                        {
                            broadcastObject("start");
                        }
                    }
                    else if(sessionData.size() != 0 && gameIsInProgress)
                    {
                        if(!readWriteObjectMode)
                        {
                            broadcasts.add(getSessionObjectsJson());
                            //broadcastUTF(getSessionObjectsJson());
                        }
                        else
                        {
                            broadcastObject(new SessionData(getAllPlayers(), getAllProjectiles()));
                        }
                    }
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this.sessionThread.start();

        Thread broadcastsThread = new Thread(){
            public void run()
            {
                while(sessionIsConnected)
                {
                    Thread.yield();
                    if(broadcasts.size() != 0)
                    {
                        broadcastUTF(broadcasts.poll());
                    }
                }
            }
        };
        broadcastsThread.start();

        Thread sendsThread = new Thread(){
            public void run()
            {
                while(sessionIsConnected)
                {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(sends.size() != 0)
                    {
                        Pair<String, Socket> pair = sends.poll();
                        sendUTF(pair.getKey(), pair.getValue());
                    }
                }
            }
        };
        sendsThread.start();

        Thread receivesThread = new Thread(){
            public void run()
            {
                while(sessionIsConnected)
                {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(receives.size() != 0)
                    {
                        Pair<String, Socket> pair = receives.poll();
                        receiveUTFData(pair.getKey(), pair.getValue());
                    }
                }
            }
        };
        receivesThread.start();
    }

    private boolean arePlayersReady()
    {
        int counter = 0;

        for(PlayerDataManager playerDataManager : this.sessionData.values())
        {
            if(playerDataManager.isReady())
            {
                counter++;
            }
        }

        return (counter == this.sessionData.size()) ? true : false;
    }

    public void stop()
    {
        try
        {
            if(this.server.isRunning())
            {
                for(PlayerDataManager playerDataManager : this.sessionData.values())
                {
                    playerDataManager.disconnect();
                }
                this.gameIsInProgress = false;
                this.sessionIsConnected = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addSocketToSession(Socket clientSocket)
    {
        if(clientSocket != null)
        {
            PlayerDataManager playerData = new PlayerDataManager(clientSocket, this);
            this.sessionData.put(clientSocket, playerData);
            System.out.println("Client connected with ip: " + clientSocket.getInetAddress().getHostAddress() + " on port: " + clientSocket.getPort());
        }
    }

    private void broadcastUTF(String data)
    {
        for(PlayerDataManager playerData : sessionData.values())
        {
            try
            {
                DataOutputStream serverOutput = new DataOutputStream(playerData.getClientSocket().getOutputStream());
                serverOutput.writeBytes(data + '\n');
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void broadcastObject(Object data)
    {
        for(PlayerDataManager playerData : this.sessionData.values())
        {
            try
            {
                ObjectOutputStream serverOutput = new ObjectOutputStream(playerData.getClientSocket().getOutputStream());
                serverOutput.writeObject(data);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    protected void receiveUTFData(String data, Socket socket)
    {
        if(socket != null && !data.isEmpty())
        {
            handleJsonReceive(data, socket);
            System.out.println("Server received: " + data);
        }
    }

    public void receiveObjectData(Object data, Socket socket)
    {
        if(socket != null && data != null)
        {
            handleObjectReceive(data, socket);
            System.out.println("Server received: " + data);
        }
    }

    public void sendUTF(String data, Socket socket)
    {
        try
        {
            if(socket != null)
            {
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                serverOutput.writeBytes(data + '\n');
                System.out.println("Server send: " + data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void sendObject(Object data, Socket socket)
    {
        try
        {
            if(socket != null)
            {
                ObjectOutputStream serverOutput = new ObjectOutputStream(socket.getOutputStream());
                serverOutput.writeObject(data);
                System.out.println("Server send: " + data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<PlayerObject> getAllPlayers()
    {
        ArrayList<PlayerObject> playerObjects = new ArrayList<PlayerObject>();

        for(PlayerDataManager playerData : this.sessionData.values())
        {
            playerObjects.add(playerData.getPlayer());
        }

        return playerObjects;
    }

    private ArrayList<ProjectileObject> getAllProjectiles()
    {
        ArrayList<ProjectileObject> projectileObjects = new ArrayList<ProjectileObject>();

        for(PlayerDataManager playerData : this.sessionData.values())
        {
            projectileObjects.addAll(playerData.getProjectiles());
        }

        return projectileObjects;
    }

    private String getSessionObjectsJson()
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray playersJson = new JSONArray();
        JSONArray projectilesJson = new JSONArray();

        for(PlayerObject player : getAllPlayers())
        {
            playersJson.add(SessionJSONUtil.getPlayerObjectJson(player));
        }

        for(ProjectileObject projectile : getAllProjectiles())
        {
            projectilesJson.add(SessionJSONUtil.getProjectileObjectJson(projectile));
        }

        jsonObject.put("command", "update");
        jsonObject.put("players", playersJson);
        jsonObject.put("projectiles", projectilesJson);
        return jsonObject.toJSONString();
    }

    protected void handleJsonReceive(String jsonString, Socket socket)
    {
        try
        {
            JSONObject jsonObject = (JSONObject)this.jsonParser.parse(jsonString);
            String command = jsonObject.get("command").toString();

            if(command.toLowerCase().equals("addprojectile"))
            {
                Vector2D position = new Vector2D(Double.parseDouble(jsonObject.get("positionx").toString()), Double.parseDouble(jsonObject.get("positiony").toString()));
                Vector2D velocity = new Vector2D(Double.parseDouble(jsonObject.get("velocityx").toString()), Double.parseDouble(jsonObject.get("velocityy").toString()));
                double radius = Double.parseDouble(jsonObject.get("radius").toString());
                String username = jsonObject.get("username").toString();
                ProjectileObject projectile = new ProjectileObject(position, velocity, radius, username);
                this.sessionData.get(socket).addProjectile(projectile);
            }
            else if(command.toLowerCase().equals("updateposition"))
            {
                Vector2D position = new Vector2D(Double.parseDouble(jsonObject.get("positionx").toString()), Double.parseDouble(jsonObject.get("positiony").toString()));
                Vector2D velocity = new Vector2D(Double.parseDouble(jsonObject.get("velocityx").toString()), Double.parseDouble(jsonObject.get("velocityy").toString()));
                this.sessionData.get(socket).getPlayer().setPosition(position);
                this.sessionData.get(socket).getPlayer().setVelocity(velocity);
            }
            else if(command.toLowerCase().equals("setusername"))
            {
                String username = jsonObject.get("username").toString();
                this.sessionData.get(socket).getPlayer().setUsername(username);

                JSONObject addPlayerJSON = new JSONObject();
                addPlayerJSON.put("command", "addplayer");
                addPlayerJSON.put("username", username);

                this.broadcasts.add(addPlayerJSON.toJSONString());
                //broadcastUTF(addPlayerJSON.toJSONString());

                for(PlayerDataManager playerDataManager : this.sessionData.values())
                {
                    JSONObject newlayerJSON = new JSONObject();
                    newlayerJSON.put("command", "addplayer");
                    newlayerJSON.put("username", playerDataManager.getPlayer().getUsername());
                    if(!playerDataManager.getPlayer().getUsername().equals(username))
                    {
                        this.sends.add(new Pair<String, Socket>(newlayerJSON.toJSONString(), socket));
                        //sendUTF(newlayerJSON.toJSONString(), socket);
                    }
                }
            }
            else if(command.toLowerCase().equals("ready"))
            {
                String username = jsonObject.get("username").toString();
                this.sessionData.get(socket).setReady(true);

                JSONObject readyPlayerJSON = new JSONObject();
                readyPlayerJSON.put("command", "ready");
                readyPlayerJSON.put("username", username);

                this.broadcasts.add(readyPlayerJSON.toJSONString());
                //broadcastUTF(readyPlayerJSON.toJSONString());
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void handleObjectReceive(Object data, Socket socket)
    {
        if(data instanceof PlayerObject)
        {
            this.sessionData.get(socket).getPlayer().setPosition(((PlayerObject)data).getPosition());
            this.sessionData.get(socket).getPlayer().setVelocity(((PlayerObject)data).getVelocity());
        }
        else if(data instanceof ProjectileObject)
        {
            this.sessionData.get(socket).addProjectile((ProjectileObject)data);
        }
        else
        {
            System.out.println("Unknown object received!");
        }
    }

    public Server getServer()
    {
        return this.server;
    }

    public int getMaxPlayerCount()
    {
        return this.maxPlayerCount;
    }

    public int getConnectedSocketsCount()
    {
        return this.sessionData.size();
    }

    public HashMap<Socket, PlayerDataManager> getSessionData()
    {
        return sessionData;
    }

    public boolean isReadWriteObjectMode()
    {
        return readWriteObjectMode;
    }
}
