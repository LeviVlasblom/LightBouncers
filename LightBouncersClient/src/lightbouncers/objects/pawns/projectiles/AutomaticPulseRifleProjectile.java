package lightbouncers.objects.pawns.projectiles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;
import lightbouncers.world.World;

public class AutomaticPulseRifleProjectile extends Projectile
{
    public AutomaticPulseRifleProjectile(Vector2D position, double rotation, World world, Vector2D direction)
    {
        super(position, rotation, world, 40.0, 400.0, 1.0, direction);
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillOval(this.worldPosition.x - 4, this.worldPosition.y - 4, 8, 8);
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
