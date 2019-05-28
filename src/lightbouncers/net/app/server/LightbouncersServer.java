package lightbouncers.net.app.server;

import lightbouncers.net.server.ITCPServerReceiver;
import lightbouncers.net.server.Server;

import java.net.Socket;

public class LightbouncersServer implements ITCPServerReceiver
{
    private static LightbouncersServer instance;
    private Server server;

    private LightbouncersServer()
    {

    }

    public static LightbouncersServer getInstance()
    {
        if(instance == null)
        {
            instance = new LightbouncersServer();
        }
        return instance;
    }

    @Override
    public void receive(String data, Socket socket)
    {

    }
}
