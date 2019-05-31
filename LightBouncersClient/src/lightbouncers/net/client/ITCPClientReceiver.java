package lightbouncers.net.client;

public interface ITCPClientReceiver
{
    void receive(String data);
    void receive(Object data);
}
