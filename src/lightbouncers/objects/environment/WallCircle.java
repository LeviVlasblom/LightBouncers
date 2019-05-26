package lightbouncers.objects.environment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;

public class WallCircle extends EnvironmentObject
{
    private double radius;

    public WallCircle(Vector2D worldPosition, double rotation, double radius)
    {
        super(worldPosition, rotation);

        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
//        graphicsContext.setFill(Color.ORANGE);
//        graphicsContext.fillOval(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.strokeOval(this.worldPosition.x - this.radius, this.worldPosition.y - this.radius, this.radius * 2, this.radius * 2);
    }

    @Override
    public void checkCollision(Actor actor)
    {

    }

    @Override
    public double getDistanceFromPoint(Vector2D point)
    {
        return Vector2D.distance(this.worldPosition, point) - this.radius;
    }

    @Override
    public Vector2D getClosestPoint(Vector2D point)
    {
        return Vector2D.fromAngleWithPosition(this.worldPosition, Vector2D.getAngle(Vector2D.direction(this.worldPosition, point)), this.radius);
    }
}
