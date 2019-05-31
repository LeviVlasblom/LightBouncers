package lightbouncers.net.server;

import lightbouncers.math.Vector2D;
import lightbouncers.net.PlayerObject;
import lightbouncers.net.ProjectileObject;
import lightbouncers.net.SessionObject;
import lightbouncers.net.server.TestSession;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class PlayerDataManager implements Serializable
{
    private Socket clientSocket;
    private Thread listenerThread;

    private PlayerObject player;
    private ArrayList<ProjectileObject> projectiles;
    private TestSession session;

    private int points;
    private boolean isConnected;

    private String username;
    private boolean isReady;

    public PlayerDataManager(Socket clientSocket, TestSession session)
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
                    session.listen(clientSocket);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        this.listenerThread.start();
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

    public TestSession getSession()
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
