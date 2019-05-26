package lightbouncers.world;

import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.characters.PlayerCharacter;

import java.util.ArrayList;

public class World
{
    private ArrayList<Actor> actors;
    private PlayerCharacter player;

    private Level level;

    public World(PlayerCharacter player)
    {
        this.actors = new ArrayList<Actor>();
        this.player = player;
        this.actors.add(this.player);
    }

    public World(PlayerCharacter player, Level level)
    {
        this(player);
        changeLevel(level);
    }

    public void changeLevel(Level level)
    {
        if(level != null)
        {
            this.level = level;
        }
    }
}
