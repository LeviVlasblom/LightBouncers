package lightbouncers.objects.environment;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;

public class WallBox extends EnvironmentObject
{
    private double width;
    private double height;

    public WallBox(Vector2D worldPosition, double rotation, double width, double height)
    {
        super(worldPosition, rotation);

        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
//        graphicsContext.setFill(Color.ORANGE);
//        graphicsContext.fillOval(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2);
        graphicsContext.setStroke(Color.WHITE);
        graphicsContext.strokeRect(this.worldPosition.x - (this.width / 2), this.worldPosition.y - (this.height / 2), this.width, this.height);
    }

    @Override
    public void checkCollision(Actor actor)
    {

    }

    @Override
    public double getDistanceFromPoint(Vector2D point)
    {
        double closeX = point.x;
        double closeY = point.y;

        //Check left
        if(point.x < this.worldPosition.x - (this.width / 2))
        {
            closeX = this.worldPosition.x - (this.width / 2);
        }
        //Check right
        if(point.x > this.worldPosition.x + (this.width / 2))
        {
            closeX = this.worldPosition.x + (this.width / 2);
        }
        //Check top
        if(point.y < this.worldPosition.y - (this.height / 2))
        {
            closeY = this.worldPosition.y - (this.height / 2);
        }
        //Check bottom
        if(point.y > this.worldPosition.y + (this.height / 2))
        {
            closeY = this.worldPosition.y + (this.height / 2);
        }

        Vector2D closestPoint = new Vector2D(closeX, closeY);

        return Vector2D.distance(closestPoint, point);
    }

    @Override
    public Vector2D getClosestPoint(Vector2D point)
    {
        double closeX = point.x;
        double closeY = point.y;

        //Check left
        if(point.x < this.worldPosition.x - (this.width / 2))
        {
            closeX = this.worldPosition.x - (this.width / 2);
        }
        //Check right
        if(point.x > this.worldPosition.x + (this.width / 2))
        {
            closeX = this.worldPosition.x + (this.width / 2);
        }
        //Check top
        if(point.y < this.worldPosition.y - (this.height / 2))
        {
            closeY = this.worldPosition.y - (this.height / 2);
        }
        //Check bottom
        if(point.y > this.worldPosition.y + (this.height / 2))
        {
            closeY = this.worldPosition.y + (this.height / 2);
        }

        return new Vector2D(closeX, closeY);
    }
}
