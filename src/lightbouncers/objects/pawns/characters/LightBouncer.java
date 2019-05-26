package lightbouncers.objects.pawns.characters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lightbouncers.math.Angle;
import lightbouncers.objects.Actor;
import lightbouncers.objects.items.Item;
import lightbouncers.math.Vector2D;

public class LightBouncer extends PlayerCharacter
{
    private Item item;
    private double radius;

    public LightBouncer(Vector2D position, double rotation, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, maxVelocity, acceleration, scale);
        this.item = null;
        this.radius = 10;
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {

    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        if(this.item != null)
        {
            this.item.onItemUse();
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        if(this.item != null)
        {
            this.item.onItemStopUse();
        }
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillOval(this.worldPosition.x - this.radius, this.worldPosition.y - this.radius, this.radius * 2, this.radius * 2);
        graphicsContext.setFill(Color.GREEN);
        Vector2D newVector = Vector2D.fromAngleWithPosition(this.worldPosition, this.directionAngle, this.velocity.magnitude * 5);
        graphicsContext.strokeLine(this.worldPosition.x, this.worldPosition.y, newVector.x, newVector.y);
    }

    @Override
    public void checkCollision(Actor actor)
    {

    }

    @Override
    public double getDistanceFromPoint(Vector2D point)
    {
        return 0.0;
    }

    @Override
    public Vector2D getClosestPoint(Vector2D point)
    {
        return null;
    }
}
