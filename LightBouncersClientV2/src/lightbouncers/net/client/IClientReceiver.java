package lightbouncers.net.client;

import lightbouncers.net.GameData;

public interface IClientReceiver
{
    void onGameStart();
    void onGameEnd();
    void onDisconnect();
    void onUpdate(GameData gameData);
}
