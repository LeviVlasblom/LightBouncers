package lightbouncers.net.client;

import lightbouncers.math.Vector2D;

public class SessionObject
{
    private Vector2D position;
    private Vector2D velocity;
    private double radius;
    private String name;

    public SessionObject(Vector2D position, Vector2D velocity, double radius, String name)
    {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.name = name;
    }

    public Vector2D getPosition()
    {
        return this.position;
    }

    public Vector2D getVelocity()
    {
        return this.velocity;
    }

    public double getRadius()
    {
        return this.radius;
    }

    public String getName()
    {
        return this.name;
    }

    public void setPosition(Vector2D position)
    {
        this.position = position;
    }

    public void setVelocity(Vector2D velocity)
    {
        this.velocity = velocity;
    }

    public void setRadius(double radius)
    {
        this.radius = radius;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
