package lightbouncers.tracing;

import lightbouncers.game.Actor;
import lightbouncers.math.Vector2D;

public class TraceResult2D
{
    private Actor objectHit;
    private Vector2D hitPoint;

    public TraceResult2D()
    {

    }

    public TraceResult2D(Actor objectHit, Vector2D hitPoint)
    {
        this.objectHit = objectHit;
        this.hitPoint = hitPoint;
    }

    public Actor getObjectHit()
    {
        return this.objectHit;
    }

    public Vector2D getHitPoint()
    {
        return this.hitPoint;
    }

    public void setObjectHit(Actor objectHit)
    {
        this.objectHit = objectHit;
    }

    public void setHitPoint(Vector2D hitPoint)
    {
        this.hitPoint = hitPoint;
    }
}
