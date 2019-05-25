package lightbouncers.tracing;

import lightbouncers.vectormath.Vector2D;
import lightbouncers.objects.ISceneObject;

public class TraceResult2D
{
    private ISceneObject objectHit;
    private Vector2D hitPoint;

    public TraceResult2D()
    {

    }

    public TraceResult2D(ISceneObject objectHit, Vector2D hitPoint)
    {
        this.objectHit = objectHit;
        this.hitPoint = hitPoint;
    }

    public ISceneObject getObjectHit()
    {
        return this.objectHit;
    }

    public Vector2D getHitPoint()
    {
        return this.hitPoint;
    }

    public void setObjectHit(ISceneObject objectHit)
    {
        this.objectHit = objectHit;
    }

    public void setHitPoint(Vector2D hitPoint)
    {
        this.hitPoint = hitPoint;
    }
}
