package lightbouncers.world;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.characters.PlayerCharacter;

import java.util.ArrayList;

public class World
{
    private ArrayList<Actor> actors;
    private PlayerCharacter player;

    private Level level;

    public World()
    {
        this.actors = new ArrayList<Actor>();
    }

    public World(PlayerCharacter player)
    {
        this();
        this.player = player;
        this.actors.add(this.player);
    }

    public void update(double deltaTime)
    {
        for(Actor actor : this.actors)
        {
            actor.update(deltaTime);
        }

        if(this.level != null)
        {
            this.level.checkCollisions(this.player);
        }
    }

    public void draw(GraphicsContext graphicsContext)
    {
        if(this.level != null)
        {
            this.level.draw(graphicsContext);
        }

        for(Actor actor : this.actors)
        {
            if(actor != this.player)
            {
                actor.draw(graphicsContext);
            }
        }
        this.player.draw(graphicsContext);
    }

    public void addActor(Actor actor)
    {
        if(actor != null)
        {
            this.actors.add(actor);
        }
    }

    public ArrayList<Actor> getNonPlayersActors()
    {
        ArrayList<Actor> nonPlayerActors = new ArrayList<Actor>();

        for(Actor actor : this.actors)
        {
            if(actor != this.player)
            {
                nonPlayerActors.add(actor);
            }
        }
        return nonPlayerActors;
    }

    public PlayerCharacter getPlayer()
    {
        return this.player;
    }

    public Level getLevel()
    {
        return this.level;
    }

    public void setPlayer(PlayerCharacter player)
    {
        this.player = player;
    }

    public void changeLevel(Level level)
    {
        if(level != null)
        {
            this.level = level;
        }
    }
}
