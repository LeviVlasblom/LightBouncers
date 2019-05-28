package lightbouncers.net.server;

import java.net.Socket;

public interface ITCPServerReceiver
{
    void receive(String data, Socket socket);
}
