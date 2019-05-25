package lightbouncers.objects.pawns.characters;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lightbouncers.objects.Actor;
import lightbouncers.objects.items.Item;
import lightbouncers.vectormath.Vector2D;

public class LightBouncer extends Character
{
    private Item item;
    private double radius;

    public LightBouncer(Vector2D position, double rotation, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, maxVelocity, acceleration, scale);
        this.item = null;
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
    public void onKeyPressed(KeyEvent event)
    {

    }

    @Override
    public void onKeyReleased(KeyEvent event)
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
        return 0.0;
    }
}
