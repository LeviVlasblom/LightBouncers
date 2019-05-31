package lightbouncers.net.client;

import lightbouncers.world.World;

public interface ILobbyUpdate
{
    void onPlayerConnectedToLobby(String username);
    void onPlayerDisconnectedLobby(String username);
    void onMatchStart(World world);
}
