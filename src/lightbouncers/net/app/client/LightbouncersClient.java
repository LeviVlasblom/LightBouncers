package lightbouncers.net.app.client;

import lightbouncers.net.client.Client;
import lightbouncers.net.client.ITCPClientReceiver;

public class LightbouncersClient implements ITCPClientReceiver
{
    private static LightbouncersClient instance;
    private Client client;

    private LightbouncersClient()
    {

    }

    public static LightbouncersClient getInstance()
    {
        if(instance == null)
        {
            instance = new LightbouncersClient();
        }
        return instance;
    }

    @Override
    public void receive(String data)
    {

    }
}
