package lightbouncers.world;

import lightbouncers.file.JSONUtil;
import lightbouncers.objects.Actor;
import lightbouncers.objects.environment.EnvironmentObject;

import java.util.ArrayList;

public class Level
{
    private ArrayList<EnvironmentObject> environmentObjects;

    public Level()
    {
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

    public ArrayList<EnvironmentObject> getEnvironmentObjects()
    {
        return this.environmentObjects;
    }
}
