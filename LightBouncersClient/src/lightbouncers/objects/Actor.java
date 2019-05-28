package lightbouncers.objects;

import com.sun.javafx.geom.transform.Affine2D;
import javafx.scene.canvas.GraphicsContext;
import lightbouncers.math.Vector2D;

public abstract class Actor
{
    protected Vector2D worldPosition;
    protected Vector2D localPosition;
    protected double rotation;

    protected Actor parent;

    public Actor(Vector2D worldPosition, double rotation)
    {
        this.worldPosition = worldPosition;
        this.localPosition = Vector2D.zero();
        this.rotation = rotation;
    }

    public void update(double deltatime)
    {
        if(this.parent != null)
        {
            Vector2D newPosition = Vector2D.add(this.parent.worldPosition, this.localPosition);
            double newRotation = this.parent.getRotation() + this.rotation;

            Affine2D affine2D = new Affine2D();
            affine2D.translate(newPosition.x, newPosition.y);
            affine2D.rotate(newRotation, this.parent.getWorldPosition().x, this.parent.getWorldPosition().y);

            this.worldPosition = new Vector2D(affine2D.getMxt(), affine2D.getMxy());
        }
    }

    public abstract void draw(GraphicsContext graphicsContext);

    public abstract void checkCollision(Actor actor);

    public abstract double getDistanceFromPoint(Vector2D point);

    public abstract Vector2D getClosestPoint(Vector2D point);

    public Vector2D getWorldPosition()
    {
        return this.worldPosition;
    }

    public Vector2D getLocalPosition()
    {
        return this.localPosition;
    }

    public Actor getParent()
    {
        return this.parent;
    }

    public double getRotation()
    {
        return this.rotation;
    }

    public void setWorldPosition(Vector2D worldPosition)
    {
        this.worldPosition = worldPosition;
    }

    public void setLocalPosition(Vector2D localPosition)
    {
        this.localPosition = localPosition;
    }

    public void setRotation(double rotation)
    {
        this.rotation = rotation;
    }

    public void setParent(Actor parent)
    {
        this.parent = parent;

        if(parent != null)
        {
            this.worldPosition = parent.getWorldPosition();
        }
        this.localPosition = Vector2D.zero();
    }

    public void setParentKeepWorld(Actor parent)
    {
        this.parent = parent;
    }

    public void setParentKeepLocal(Actor parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            this.worldPosition = parent.getWorldPosition();
        }
    }
}
