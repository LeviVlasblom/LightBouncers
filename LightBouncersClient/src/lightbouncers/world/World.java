package lightbouncers.world;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.math.Vector2D;
import lightbouncers.net.PlayerObject;
import lightbouncers.net.SessionData;
import lightbouncers.net.client.ITCPClientReceiver;
import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.Pawn;
import lightbouncers.objects.pawns.characters.PlayerCharacter;
import lightbouncers.objects.pawns.projectiles.Projectile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class World implements ITCPClientReceiver
{
    private ArrayList<Pawn> playerActors;
    private ArrayList<Projectile> projectiles;
    private PlayerCharacter player;

    private Level level;

    public World()
    {
        this.playerActors = new ArrayList<Pawn>();
        this.projectiles = new ArrayList<Projectile>();
    }

    public World(PlayerCharacter player)
    {
        this();
        this.player = player;
        //this.playerActors.add(this.player);
    }

    public void update(double deltaTime)
    {
        for(Actor actor : this.playerActors)
        {
            actor.update(deltaTime);
        }
        for(Projectile projectile : this.projectiles)
        {
            projectile.update(deltaTime);
        }
        this.player.update(deltaTime);

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

        for(Actor actor : this.playerActors)
        {
            actor.draw(graphicsContext);
        }
        for(Projectile projectile : this.projectiles)
        {
            projectile.draw(graphicsContext);
        }
        this.player.draw(graphicsContext);
    }

    public void addPlayerActor(Pawn actor)
    {
        if(actor != null)
        {
            this.playerActors.add(actor);
        }
    }

    public void addProjectile(Projectile projectile)
    {
        if(projectile != null)
        {
            this.projectiles.add(projectile);
        }
    }

    public ArrayList<Actor> getNonPlayersActors()
    {
        ArrayList<Actor> nonPlayerActors = new ArrayList<Actor>();

        for(Actor actor : this.playerActors)
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

    @Override
    public void receive(String data)
    {
        JSONParser jsonParser = new JSONParser();
        try
        {
            JSONObject jsonObject = (JSONObject)jsonParser.parse(data);
            String command = jsonObject.get("command").toString();

            if(command.equals("update"))
            {
                JSONArray players = (JSONArray)jsonObject.get("players");
                JSONArray projectiles = (JSONArray)jsonObject.get("projectiles");

                for(Object playerJSON : players)
                {
                    JSONObject playerObject = (JSONObject)playerJSON;
                    Vector2D position = new Vector2D(Double.parseDouble(playerObject.get("positionx").toString()), Double.parseDouble(playerObject.get("positiony").toString()));
                    Vector2D velocity = new Vector2D(Double.parseDouble(playerObject.get("velocityx").toString()), Double.parseDouble(playerObject.get("velocityy").toString()));
                    String username = playerObject.get("username").toString();

                    if(!this.player.getName().equals(username))
                    {
                        for(Pawn playerActor : this.playerActors)
                        {
                            if(playerActor.getName().equals(username))
                            {
                                playerActor.setWorldPosition(position);
                                playerActor.setVelocity(velocity);
                                playerActor.setDirection(velocity.normalized());
                            }
                        }
                    }
                }

                for(Object projectileJSON : projectiles)
                {
                    JSONObject projectileObject = (JSONObject)projectileJSON;
                    Vector2D position = new Vector2D(Double.parseDouble(projectileObject.get("positionx").toString()), Double.parseDouble(projectileObject.get("positiony").toString()));
                    Vector2D velocity = new Vector2D(Double.parseDouble(projectileObject.get("velocityx").toString()), Double.parseDouble(projectileObject.get("velocityy").toString()));
                    String username = projectileObject.get("username").toString();

                    if(!this.player.getName().equals(username))
                    {
                        for(Projectile projectile : this.projectiles)
                        {
                            if(projectile.getName().equals(username))
                            {
                                projectile.setWorldPosition(position);
                                projectile.setVelocity(velocity);
                                projectile.setDirection(velocity.normalized());
                            }
                        }
                    }
                }
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Object data)
    {
        SessionData sessionData = (SessionData)data;

        for(PlayerObject playerObject : sessionData.getPlayers())
        {
            if(!this.player.getName().equals(playerObject.getUsername()))
            {
                for(Pawn playerActor : this.playerActors)
                {
                    if(playerActor.getName().equals(playerObject.getUsername()))
                    {
                        playerActor.setWorldPosition(playerObject.getPosition());
                        playerActor.setVelocity(playerObject.getVelocity());
                        playerActor.setDirection(playerObject.getVelocity().normalized());
                    }
                }
            }
        }
    }
}
