package lightbouncers.net;

import java.io.Serializable;
import java.util.ArrayList;

public class SessionData implements Serializable
{
    private ArrayList<PlayerObject> players;
    private ArrayList<ProjectileObject> projectiles;

    public SessionData(ArrayList<PlayerObject> players, ArrayList<ProjectileObject> projectiles)
    {
        this.players = players;
        this.projectiles = projectiles;
    }

    public ArrayList<PlayerObject> getPlayers()
    {
        return this.players;
    }

    public ArrayList<ProjectileObject> getProjectiles()
    {
        return this.projectiles;
    }
}
