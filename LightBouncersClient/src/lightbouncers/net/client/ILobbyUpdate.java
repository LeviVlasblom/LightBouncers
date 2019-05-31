package lightbouncers.net.client;

public interface ILobbyUpdate
{
    void onPlayerConnectedToLobby(String username);
    void onPlayerDisconnectedLobby(String username);
}
