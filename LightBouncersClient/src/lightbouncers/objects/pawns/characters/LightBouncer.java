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
import lightbouncers.objects.items.weapons.guns.AutomaticPulseRifle;
import lightbouncers.objects.lights.Light;
import lightbouncers.world.World;

public class LightBouncer extends PlayerCharacter
{
    private Item item;
    private Light light;

    public LightBouncer(Vector2D position, double rotation, World world, double maxVelocity, double acceleration, double scale, String name)
    {
        super(position, rotation, world, maxVelocity, acceleration, scale);
        this.item = new AutomaticPulseRifle(this.worldPosition, this.rotation, this.world);
        //this.light = new Light(new Vector2D(100, 100), 600, 1.0, Color.rgb(173, 168, 65, 0.1), Color.YELLOW);
        //this.light = new Light(new Vector2D(100, 100), 600, 1.0, Color.rgb(5, 121, 255, 0.1),  Color.rgb(220, 0, 255, 1.0));
        this.light = new Light(new Vector2D(100, 100), rotation, Math.PI / 2, 600, 1.0, Color.rgb(130, 68, 255, 0.1),  Color.rgb(5, 121, 255, 1.0));
        this.name = name;
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
        this.light.setPosition(this.worldPosition);
        this.light.setRotation(this.rotation);
        this.item.setWorldPosition(this.worldPosition);
        this.item.setRotation(this.rotation);
        this.item.update(deltatime);
    }

    @Override
    public void onMouseMoved(MouseEvent event)
    {
        Vector2D mousePosition = new Vector2D(event.getX(), event.getY());
        Vector2D direction = Vector2D.direction(this.worldPosition, mousePosition);
        this.rotation = Vector2D.getAngle(direction);
    }

    @Override
    public void onMousePressed(MouseEvent event)
    {
        System.out.println("Fire start!");
        if(this.item != null)
        {
            this.item.onItemUse();
        }
    }

    @Override
    public void onMouseReleased(MouseEvent event)
    {
        System.out.println("Fire stop!");
        if(this.item != null)
        {
            this.item.onItemStopUse();
        }
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        if(world.getLevel() != null)
        {
            this.light.draw(graphicsContext, this.world.getLevel().getEnvironmentObjectsAsActors());
        }
        else
        {
            this.light.draw(graphicsContext, null);
        }
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillOval(this.worldPosition.x - this.radius, this.worldPosition.y - this.radius, this.radius * 2, this.radius * 2);
        graphicsContext.setFill(Color.GREEN);
//        Vector2D newVector = Vector2D.fromAngleWithPosition(this.worldPosition, this.directionAngle, this.velocity.magnitude * 5);
//        graphicsContext.strokeLine(this.worldPosition.x, this.worldPosition.y, newVector.x, newVector.y);
        this.item.draw(graphicsContext);
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

    @Override
    public void setRotation(double rotation)
    {
        this.rotation = rotation;
        this.light.setRotation(Vector2D.getAngle(direction));
    }
}
