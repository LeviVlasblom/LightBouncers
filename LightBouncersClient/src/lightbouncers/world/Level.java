package lightbouncers.world;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.file.JSONUtil;
import lightbouncers.objects.Actor;
import lightbouncers.objects.environment.EnvironmentObject;

import java.util.ArrayList;

public class Level
{
    private String name;
    private ArrayList<EnvironmentObject> environmentObjects;

    public Level(String name)
    {
        this.name = name;
        this.environmentObjects = new ArrayList<EnvironmentObject>();
    }

    public void checkCollisions(Actor actor)
    {
        if(actor != null)
        {
            for(EnvironmentObject environmentObject : this.environmentObjects)
            {
                environmentObject.checkCollision(actor);
            }
        }
    }

    public void addEnviromentObject(EnvironmentObject environmentObject)
    {
        if(environmentObject != null)
        {
            this.environmentObjects.add(environmentObject);
        }
    }

    public void addEnviromentObjects(EnvironmentObject... environmentObjects)
    {
        for(EnvironmentObject environmentObject : environmentObjects)
        {
            if(environmentObject != null)
            {
                this.environmentObjects.add(environmentObject);
            }
        }
    }

    public void draw(GraphicsContext graphicsContext)
    {
        for(EnvironmentObject environmentObject : this.environmentObjects)
        {
            environmentObject.draw(graphicsContext);
        }
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<EnvironmentObject> getEnvironmentObjects()
    {
        return this.environmentObjects;
    }

    public ArrayList<Actor> getEnvironmentObjectsAsActors()
    {
        ArrayList<Actor> actors = new ArrayList<Actor>();

        for(EnvironmentObject environmentObject : this.environmentObjects)
        {
            actors.add(environmentObject);
        }
        return actors;
    }
}
