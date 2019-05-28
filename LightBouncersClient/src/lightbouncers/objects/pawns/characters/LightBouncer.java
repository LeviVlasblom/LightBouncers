package lightbouncers.objects.pawns.characters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import lightbouncers.math.Angle;
import lightbouncers.objects.Actor;
import lightbouncers.objects.items.Item;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.lights.Light;
import lightbouncers.world.World;

public class LightBouncer extends PlayerCharacter
{
    private Item item;
    private Light light;

    public LightBouncer(Vector2D position, double rotation, World world, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, world, maxVelocity, acceleration, scale);
        this.item = null;
        this.light = new Light(new Vector2D(100, 100), 600, 1.0, Color.rgb(173, 168, 65, 0.1), Color.YELLOW);
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
        this.light.setPosition(this.worldPosition);
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
        this.light.draw(graphicsContext, this.world.getLevel().getEnvironmentObjectsAsActors());
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillOval(this.worldPosition.x - this.radius, this.worldPosition.y - this.radius, this.radius * 2, this.radius * 2);
        graphicsContext.setFill(Color.GREEN);
//        Vector2D newVector = Vector2D.fromAngleWithPosition(this.worldPosition, this.directionAngle, this.velocity.magnitude * 5);
//        graphicsContext.strokeLine(this.worldPosition.x, this.worldPosition.y, newVector.x, newVector.y);
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
