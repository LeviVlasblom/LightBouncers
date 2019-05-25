package lightbouncers.objects.pawns.projectiles;

import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.Pawn;
import lightbouncers.vectormath.Vector2D;

public abstract class Projectile extends Pawn
{
    protected double damage;

    public Projectile(Vector2D position, double rotation, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, maxVelocity, acceleration, scale);
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
    }
}
