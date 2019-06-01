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
        this.player = new PlayerObject(Vector2D.zero(), Vector2D.zero(), 10, "");

        initializeListenerThread();
    }

    private void initializeListenerThread()
    {
        this.listenerThread = new Thread(){
            public void run()
            {
                while(isConnected && session.getServer().isRunning())
                {
                    //Thread.yield();
                    listen(clientSocket);
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        };
        this.listenerThread.start();
    }

    private void listen(Socket socket)
    {
        try
        {
            if(!this.session.isReadWriteObjectMode())
            {
                BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream serverOutput = new DataOutputStream(socket.getOutputStream());
                String clientInput = serverInput.readLine();
//                DataInputStream serverInput = new DataInputStream(socket.getInputStream());
//                String clientInput = "";
//                byte[] buffer = new byte[serverInput.available()];
//                for(int i = 0; i < serverInput.available(); i++)
//                {
//                    buffer[i] = serverInput.readByte();
//                }
//                clientInput = new String(buffer);

                if(clientInput != null)
                {
                    this.session.receives.add(new Pair<String, Socket>(clientInput, socket));
                    //this.session.receiveUTFData(clientInput, socket);
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
            this.session.getSessionData().get(socket).disconnect();
            this.session.getSessionData().remove(socket);
            System.out.println("Client disconnected with ip: " + socket.getInetAddress().getHostAddress() + " on port: " + socket.getPort());
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
