package lightbouncers.objects.items.weapons.guns;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;
import lightbouncers.world.World;

public class AutomaticPulseRifle extends Gun
{
    public AutomaticPulseRifle(Vector2D worldPosition, double rotation, World world)
    {
        super(worldPosition, rotation, world, FireType.FULLAUTO, 50);
    }

    @Override
    protected void fire()
    {

    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {

    }

    @Override
    public void checkCollision(Actor actor)
    {

    }

    @Override
    public double getDistanceFromPoint(Vector2D point)
    {
        return 0;
    }

    @Override
    public Vector2D getClosestPoint(Vector2D point)
    {
        return null;
    }
}
