package lightbouncers.net;

import lightbouncers.math.Vector2D;

import java.io.Serializable;

public class PlayerObject implements Serializable
{
    private Vector2D position;
    private Vector2D velocity;
    private double radius;
    private String username;
    private double rotation;

    public PlayerObject(Vector2D position, Vector2D velocity, double radius, String username, double rotation)
    {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.username = username;
        this.rotation = rotation;
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

    public double getRotation()
    {
        return this.rotation;
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

    public void setRotation(double rotation)
    {
        this.rotation = rotation;
    }
}
