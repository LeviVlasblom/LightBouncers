package lightbouncers.net.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
    private int port;
    private ServerSocket serverSocket;
    private Thread connectionThread;
    private ArrayList<Socket> clientSockets;
    private ArrayList<Session> sessions;

    private boolean isRunning;

    public Server(int port)
    {
        this.port = port;
        this.isRunning = false;
        this.clientSockets = new ArrayList<Socket>();
        this.sessions = new ArrayList<Session>();
    }

    public void start()
    {
        try
        {
            if(!this.isRunning)
            {
                this.serverSocket = new ServerSocket(this.port);
                this.isRunning = true;
                listenForConnections();
                System.out.println("Server started on port: " + this.port);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        try
        {
            if(this.isRunning)
            {
                this.isRunning = false;
//                for(Session session : this.sessions)
//                {
//                    session.stop();
//                }
                this.sessions.clear();
                if(this.connectionThread != null)
                {
                    this.connectionThread = null;
                }

                if(this.serverSocket != null)
                {
                    this.serverSocket.close();
                    System.out.println("Server stopped on port: " + this.port);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void listenForConnections()
    {
        this.connectionThread = new Thread("SocketConnectionListener")
        {
            public void run()
            {
                while(isRunning)
                {
                    try
                    {
                        Socket clientSocket = serverSocket.accept();
                        assignSocketToSession(clientSocket);
                    }
                    catch (Exception e)
                    {
                        //e.printStackTrace();
                    }
                }
            }
        };
        this.connectionThread.start();
    }

    private void assignSocketToSession(Socket clientSocket)
    {
        Session session = null;

        if(this.sessions.size() != 0 && this.sessions.get(this.sessions.size() - 1).getMaxPlayerCount() > this.sessions.get(this.sessions.size() - 1).getConnectedSocketsCount())
        {
            session = this.sessions.get(this.sessions.size() - 1);
        }
        else
        {
            session = new Session(this);
        }
        session.addSocketToSession(clientSocket);
    }

    public boolean isRunning()
    {
        return this.isRunning;
    }
}
