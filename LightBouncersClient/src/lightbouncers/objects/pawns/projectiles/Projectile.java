package lightbouncers.objects.pawns.projectiles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.Pawn;
import lightbouncers.math.Vector2D;
import lightbouncers.world.World;

public class Projectile extends Pawn
{
    private double radius;

    public Projectile(Vector2D position, double rotation, World world, double maxVelocity, double acceleration, double scale, Vector2D direction, double radius)
    {
        super(position, rotation, world, maxVelocity, acceleration, scale);
        this.isMoving = true;
        this.direction = direction;
        this.radius = radius;
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillOval(this.worldPosition.x - this.radius, this.worldPosition.y - this.radius, this.radius * 2, this.radius * 2);
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

    public double getRadius()
    {
        return this.radius;
    }
}
