package lightbouncers.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.math.Vector2D;

import java.io.Serializable;

public abstract class Actor implements Serializable
{
    protected Vector2D position;
    protected Color color;

    public Actor(Vector2D position, Color color)
    {
        this.position = position;
        this.color = color;
    }

    public abstract void update(double deltatime);

    public abstract void draw(GraphicsContext graphicsContext);

    public Vector2D getPosition()
    {
        return this.position;
    }

    public Color getColor()
    {
        return this.color;
    }

    public void setPosition(Vector2D position)
    {
        this.position = position;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public abstract double getDistanceFromPoint(Vector2D point);

    public abstract Vector2D getClosestPoint(Vector2D point);
}
