package lightbouncers.net.server;

import javafx.util.Pair;
import lightbouncers.math.Vector2D;
import lightbouncers.net.PlayerObject;
import lightbouncers.net.ProjectileObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerDataManager implements Serializable
{
    private Socket clientSocket;
    private Thread listenerThread;

    private PlayerObject player;
    private ArrayList<ProjectileObject> projectiles;
    private Session session;

    private int points;
    private boolean isConnected;

    private String username;
    private boolean isReady;

    public PlayerDataManager(Socket clientSocket, Session session)
    {
        this.clientSocket = clientSocket;
        this.session = session;
        this.projectiles = new ArrayList<ProjectileObject>();
        this.username = "";
        this.isConnected = true;
        this.isReady = false;
        this.player = new PlayerObject(Vector2D.zero(), Vector2D.zero(), 10, "", 0);

        initializeListenerThread();
    }

    private void initializeListenerThread()
    {
        this.listenerThread = new Thread(){
            public void run()
            {
                while(isConnected && session.getServer().isRunning())
                {
                    listen(clientSocket);
                    Thread.yield();
//                    try {
//                        Thread.sleep(2);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        };
        this.listenerThread.start();
    }

    private synchronized void listen(Socket socket)
    {
        try
        {
            if(!this.session.isReadWriteObjectMode())
            {
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                String clientInput = serverInput.readLine();

                if(clientInput != null)
                {
                    //this.session.receives.add(new Pair<String, Socket>(clientInput, socket));
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
                    this.session.receiveObjectData(clientInput, socket);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.session.getSessionData().get(socket).disconnect();
            this.session.getSessionData().remove(socket);
            System.out.println("Client disconnected with ip: " + socket.getInetAddress().getHostAddress() + " on port: " + socket.getPort());
        }
    }

    public void sendUTF(String data)
    {
        try
        {
            if(this.clientSocket != null)
            {
                //Thread.yield();
                Thread.sleep(2);
                DataOutputStream serverOutput = new DataOutputStream(this.clientSocket.getOutputStream());
                serverOutput.writeBytes(data + '\n');
                //System.out.println("Server send: " + data);
                Thread.sleep(2);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void receiveUTFData(String data, Socket socket)
    {
        if(socket != null && !data.isEmpty())
        {
            handleJsonReceive(data, socket);
            //System.out.println("Server received: " + data);
        }
    }

    protected void handleJsonReceive(String jsonString, Socket socket)
    {
        try
        {
            JSONObject jsonObject = (JSONObject)(new JSONParser()).parse(jsonString);
            String command = jsonObject.get("command").toString();

            if(command.toLowerCase().equals("addprojectile"))
            {
                Vector2D position = new Vector2D(Double.parseDouble(jsonObject.get("positionx").toString()), Double.parseDouble(jsonObject.get("positiony").toString()));
                Vector2D velocity = new Vector2D(Double.parseDouble(jsonObject.get("velocityx").toString()), Double.parseDouble(jsonObject.get("velocityy").toString()));
                double radius = Double.parseDouble(jsonObject.get("radius").toString());
                String username = jsonObject.get("username").toString();
                ProjectileObject projectile = new ProjectileObject(position, velocity, radius, username);
                this.session.getSessionData().get(socket).addProjectile(projectile);

                for(PlayerDataManager playerDataManager : this.session.getSessionData().values())
                {
                    JSONObject newProjectileJSON = SessionJSONUtil.getProjectileObjectJson(projectile);
                    newProjectileJSON.put("command", "addprojectle");

                    if(!playerDataManager.getPlayer().getUsername().equals(username))
                    {
                        playerDataManager.sendUTF(newProjectileJSON.toJSONString());
                    }
                }
            }
            else if(command.toLowerCase().equals("updateposition"))
            {
                Vector2D position = new Vector2D(Double.parseDouble(jsonObject.get("positionx").toString()), Double.parseDouble(jsonObject.get("positiony").toString()));
                Vector2D velocity = new Vector2D(Double.parseDouble(jsonObject.get("velocityx").toString()), Double.parseDouble(jsonObject.get("velocityy").toString()));
                double rotation = Double.parseDouble(jsonObject.get("rotation").toString());
                this.session.getSessionData().get(socket).getPlayer().setPosition(position);
                this.session.getSessionData().get(socket).getPlayer().setVelocity(velocity);
                this.session.getSessionData().get(socket).getPlayer().setRotation(rotation);
            }
            else if(command.toLowerCase().equals("setusername"))
            {
                String username = jsonObject.get("username").toString();
                this.session.getSessionData().get(socket).getPlayer().setUsername(username);

                JSONObject addPlayerJSON = new JSONObject();
                addPlayerJSON.put("command", "addplayer");
                addPlayerJSON.put("username", username);

                //this.broadcasts.add(addPlayerJSON.toJSONString());
                this.session.broadcastUTF(addPlayerJSON.toJSONString());

                for(PlayerDataManager playerDataManager : this.session.getSessionData().values())
                {
                    JSONObject newPlayerJSON = new JSONObject();
                    newPlayerJSON.put("command", "addplayer");
                    newPlayerJSON.put("username", playerDataManager.getPlayer().getUsername());
                    if(!playerDataManager.getPlayer().getUsername().equals(username))
                    {
                        //this.sends.add(new Pair<String, Socket>(newlayerJSON.toJSONString(), socket));
                        sendUTF(newPlayerJSON.toJSONString());
                    }
                }
            }
            else if(command.toLowerCase().equals("ready"))
            {
                String username = jsonObject.get("username").toString();
                this.session.getSessionData().get(socket).setReady(true);

                JSONObject readyPlayerJSON = new JSONObject();
                readyPlayerJSON.put("command", "ready");
                readyPlayerJSON.put("username", username);

                //this.broadcasts.add(readyPlayerJSON.toJSONString());
                this.session.broadcastUTF(readyPlayerJSON.toJSONString());
            }
        }
        catch (ParseException e)
        {
            //e.printStackTrace();
        }
    }

    public void disconnect()
    {
        this.isConnected = false;
        this.projectiles.clear();
    }

    public void addPoint()
    {
        this.points++;
    }

    public void addProjectile(ProjectileObject projectileObject)
    {
        if(projectileObject != null)
        {
            this.projectiles.add(projectileObject);
        }
    }

    public Socket getClientSocket()
    {
        return this.clientSocket;
    }

    public Thread getListenerThread()
    {
        return this.listenerThread;
    }

    public Session getSession()
    {
        return this.session;
    }

    public PlayerObject getPlayer()
    {
        return this.player;
    }

    public ArrayList<ProjectileObject> getProjectiles()
    {
        return this.projectiles;
    }

    public int getPoints()
    {
        return this.points;
    }

    public boolean isConnected()
    {
        return this.isConnected;
    }

    public String getUsername()
    {
        return this.username;
    }

    public boolean isReady()
    {
        return this.isReady;
    }

    public void setPlayer(PlayerObject player)
    {
        if(player != null)
        {
            this.player = player;
        }
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setReady(boolean ready)
    {
        isReady = ready;
    }
}
