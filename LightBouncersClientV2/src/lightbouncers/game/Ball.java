package lightbouncers.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.math.Vector2D;

import java.io.Serializable;

public class Ball extends Actor implements Serializable
{
    private double radius;
    private Vector2D velocity;

    public Ball(Vector2D position, double radius, Color color)
    {
        super(position, color);

        this.radius = radius;
        this.velocity = Vector2D.zero();
    }

    @Override
    public void update(double deltatime)
    {
        this.position = Vector2D.add(this.position, Vector2D.multiply(this.velocity, deltatime));
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        graphicsContext.setFill(this.color);
        graphicsContext.fillOval(this.position.x - this.radius, this.position.y - this.radius, this.radius * 2, this.radius * 2);
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

    public Vector2D getVelocity()
    {
        return this.velocity;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public void setVelocity(Vector2D velocity)
    {
        this.velocity = velocity;
    }
}
