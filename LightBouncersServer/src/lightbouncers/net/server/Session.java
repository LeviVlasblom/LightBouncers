package lightbouncers.net.server;

import lightbouncers.math.Vector2D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Session
{
    private int maxPlayerCount;
    private ArrayList<Socket> clientSockets;
    private HashMap<SessionObject, Socket> players;
    //<Projectile, Owner>
    private HashMap<SessionObject, Socket> projectiles;
    private ArrayList<Thread> listenerThreads;

    private Thread sessionThread;
    private Server server;
    private boolean gameIsInProgress;

    private JSONParser jsonParser;

    public Session(Server server)
    {
        this.maxPlayerCount = 4;
        this.clientSockets = new ArrayList<Socket>();
        this.players = new HashMap<SessionObject, Socket>();
        this.projectiles = new HashMap<SessionObject, Socket>();
        this.listenerThreads = new ArrayList<Thread>();
        this.server = server;
        this.gameIsInProgress = false;
        this.jsonParser = new JSONParser();
        this.sessionThread = new Thread(){
            public void run()
            {
                if(players.size() > 1)
                {
                    gameIsInProgress = true;
                    broadcast("start");
                }
                if(players.size() != 0 && gameIsInProgress)
                {
                    broadcast(getSessionObjectsJson());
                }
            }
        };
    }

//    public void stop()
//    {
//        try
//        {
//            if(this.server.isRunning())
//            {
//                for(Thread listenerThread : this.listenerThreads)
//                {
//                    listenerThread = null;
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    public void addSocketToSession(Socket clientSocket)
    {
        if(clientSocket != null)
        {
            this.clientSockets.add(clientSocket);
            System.out.println("Client connected with ip: " + clientSocket.getInetAddress().getHostAddress() + " on port: " + clientSocket.getPort());

            Thread listenerThread = new Thread(){
                public void run()
                {
                    while(server.isRunning())
                    {
                        listen(clientSocket);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            };

            listenerThreads.add(listenerThread);
            listenerThread.start();
        }
    }

    public void broadcast(String data)
    {
        for(Socket socket : this.clientSockets)
        {
            try
            {
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                serverOutput.writeBytes(data + '\n');
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    synchronized private void listen(Socket socket)
    {
        try
        {
            BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());

            String clientInput = serverInput.readLine();

            if(clientInput != null)
            {
                receive(clientInput, socket);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void send(String data, Socket socket)
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

    public void receive(String data, Socket socket)
    {
        try
        {
            if(socket != null)
            {
                handleJsonReceive(data, socket);
                System.out.println("Server received: " + data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getSessionObjectsJson()
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray playersJson = new JSONArray();
        JSONArray projectilesJson = new JSONArray();

        for(SessionObject player : this.players.keySet())
        {
            playersJson.add(getSessionObjectJson(player));
        }

        for(SessionObject projectile : this.projectiles.keySet())
        {
            projectilesJson.add(getSessionObjectJson(projectile));
        }

        jsonObject.put("players", playersJson);
        jsonObject.put("projectiles", projectilesJson);
        return jsonObject.toJSONString();
    }

    private JSONObject getSessionObjectJson(SessionObject sessionObject)
    {
        JSONObject objectJson = new JSONObject();
        objectJson.put("name", sessionObject.getName());
        objectJson.put("radius", sessionObject.getRadius());
        objectJson.put("positionx", sessionObject.getPosition().x);
        objectJson.put("positiony", sessionObject.getPosition().y);
        objectJson.put("velocityx", sessionObject.getVelocity().x);
        objectJson.put("velocityy", sessionObject.getVelocity().y);
        return objectJson;
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
                String name = jsonObject.get("name").toString();
                SessionObject projectile = new SessionObject(position, velocity, radius, name);
                this.projectiles.put(projectile, socket);
            }
            else if(command.toLowerCase().equals("updateposition"))
            {
                Vector2D position = new Vector2D(Double.parseDouble(jsonObject.get("positionx").toString()), Double.parseDouble(jsonObject.get("positiony").toString()));
                Vector2D velocity = new Vector2D(Double.parseDouble(jsonObject.get("velocityx").toString()), Double.parseDouble(jsonObject.get("velocityy").toString()));
                this.players.keySet().forEach(sessionObject -> {
                    if(this.players.get(sessionObject) == socket)
                    {
                        sessionObject.setPosition(position);
                        sessionObject.setVelocity(velocity);
                    }
                });
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    public int getMaxPlayerCount()
    {
        return this.maxPlayerCount;
    }

    public int getConnectedSocketsCount()
    {
        return this.clientSockets.size();
    }
}
