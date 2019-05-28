package lightbouncers.objects.pawns;

import lightbouncers.objects.Actor;
import lightbouncers.math.Vector2D;
import lightbouncers.world.World;

public abstract class Pawn extends Actor
{
    protected Vector2D velocity;
    protected double maxVelocity;
    protected double acceleration;
    protected double scale;
    protected Vector2D direction;

    protected boolean isMoving;

    public Pawn(Vector2D position, double rotation, World world, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, world);

        this.velocity = Vector2D.zero();
        this.maxVelocity = maxVelocity;
        this.acceleration = acceleration;
        this.scale = scale;
        this.direction = Vector2D.zero();
    }

    @Override
    public void update(double deltatime)
    {
        updateMovement(deltatime);
        super.update(deltatime);
    }

    protected void updateMovement(double deltatime)
    {
        if(this.velocity.magnitude > this.maxVelocity)
        {
            this.velocity = Vector2D.multiply(this.velocity.normalized(), this.maxVelocity);
        }
        else if(this.velocity.magnitude < 0)
        {
            this.velocity = Vector2D.zero();
        }

        if(this.isMoving)
        {
            this.velocity = Vector2D.add(this.velocity, Vector2D.multiply(this.direction, this.acceleration * deltatime));
        }
        else
        {
            this.velocity = Vector2D.zero();
            this.direction = Vector2D.zero();
        }

        if(this.parent == null)
        {
            this.worldPosition = Vector2D.add(this.worldPosition, this.velocity);
        }
        else
        {
            this.localPosition = Vector2D.add(this.localPosition, this.velocity);
        }
    }

    public Vector2D getVelocity()
    {
        return this.velocity;
    }

    public double getMaxVelocity()
    {
        return this.maxVelocity;
    }

    public double getAcceleration()
    {
        return this.acceleration;
    }

    public double getScale()
    {
        return this.scale;
    }

    public void setVelocity(Vector2D velocity)
    {
        this.velocity = velocity;
    }

    public void setMaxVelocity(double maxVelocity)
    {
        this.maxVelocity = maxVelocity;
    }

    public void setAcceleration(double acceleration)
    {
        this.acceleration = acceleration;
    }

    public void setScale(double scale)
    {
        this.scale = scale;
    }
}
