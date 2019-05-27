package lightbouncers.net;

import java.net.Socket;

public class Client
{
    private String host;
    private int port;
    private Socket clientSocket;
    private Thread listenerThread;

    private boolean isConnected;

    public Client(String host, int port)
    {
        this.host = host;
        this.port = port;
        this.isConnected = false;
        this.listenerThread = new Thread("ClientSocketListener"){
            public void run()
            {
                while(isConnected)
                {
                    //listen();
                }
            }
        };
    }

    public void connect()
    {
        try
        {
            if(!this.isConnected)
            {
                this.clientSocket = new Socket(this.host, this.port);
                this.isConnected = true;
                this.listenerThread.start();
            }
        }
        catch (Exception e)
        {

        }
    }

    public void disconnect()
    {
        if(this.isConnected)
        {
            this.isConnected = false;
            if(this.listenerThread != null)
            {
                this.listenerThread.stop();
            }
        }
    }
}
