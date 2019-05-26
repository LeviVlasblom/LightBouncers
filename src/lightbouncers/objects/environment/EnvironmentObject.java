package lightbouncers.objects.environment;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;

public abstract class EnvironmentObject extends Actor
{
    public EnvironmentObject(Vector2D worldPosition, double rotation)
    {
        super(worldPosition, rotation);
    }

    @Override
    public void checkCollision(Actor actor)
    {

    }
}
