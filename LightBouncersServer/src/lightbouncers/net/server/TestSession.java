package lightbouncers.net.server;

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

public class TestSession
{
    private int maxPlayerCount;
    private HashMap<Socket, PlayerDataManager> sessionData;

    private Thread sessionThread;
    private Server server;
    private boolean gameIsInProgress;

    private JSONParser jsonParser;
    private boolean readWriteObjectMode;
    private boolean sessionIsConnected;

    public TestSession(Server server, int maxPlayerCount)
    {
        this.server = server;
        this.maxPlayerCount = maxPlayerCount;
        this.sessionData = new HashMap<Socket, PlayerDataManager>();
        this.gameIsInProgress = false;
        this.jsonParser = new JSONParser();
        this.readWriteObjectMode = false;
        this.sessionIsConnected = true;
        this.sessionThread = new Thread(){
            public void run()
            {
                while(sessionIsConnected)
                {
                    if(sessionData.size() > 1 && !gameIsInProgress)
                    {
                        gameIsInProgress = true;
                        if(!readWriteObjectMode)
                        {
                            broadcastUTF("start");
                        }
                        else
                        {
                            broadcastObject("start");
                        }
                    }
                    if(sessionData.size() != 0 && gameIsInProgress)
                    {
                        if(!readWriteObjectMode)
                        {
                            broadcastUTF(getSessionObjectsJson());
                        }
                        else
                        {
                            broadcastObject(new SessionData(getAllPlayers(), getAllProjectiles()));
                        }
                    }
                }
            }
        };
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

    public void broadcastUTF(String data)
    {
        for(PlayerDataManager playerData : this.sessionData.values())
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

    synchronized protected void listen(Socket socket)
    {
        try
        {
            if(!this.readWriteObjectMode)
            {
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                String clientInput = serverInput.readLine();

                if(clientInput != null)
                {
                    receiveUTFData(clientInput, socket);
                }
            }
            else
            {
                ObjectInputStream serverInput = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream serverOutput = new ObjectOutputStream(socket.getOutputStream());
                Object clientInput = serverInput.readObject();

                if(clientInput != null)
                {
                    receiveObjectData(clientInput, socket);
                }
            }
        }
        catch (Exception e)
        {
            this.sessionData.get(socket).disconnect();
            this.sessionData.remove(socket);
            System.out.println("Client disconnected with ip: " + socket.getInetAddress().getHostAddress() + " on port: " + socket.getPort());
        }
    }

    public void receiveUTFData(String data, Socket socket)
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

        jsonObject.put("players", playersJson);
        jsonObject.put("projectiles", projectilesJson);
        return jsonObject.toJSONString();
    }

    private void handleJsonReceive(String jsonString, Socket socket)
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
}
