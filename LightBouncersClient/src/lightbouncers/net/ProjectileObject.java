package lightbouncers.net;

import lightbouncers.math.Vector2D;

import java.io.Serializable;

public class ProjectileObject implements Serializable
{
    private Vector2D position;
    private Vector2D velocity;
    private double radius;
    private String username;

    public ProjectileObject(Vector2D position, Vector2D velocity, double radius, String username)
    {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.username = username;
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

    public String getUsername()
    {
        return this.username;
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

    public void setUsername(String username)
    {
        this.username = username;
    }
}
