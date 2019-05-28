package lightbouncers.objects.pawns.projectiles;

import lightbouncers.objects.pawns.Pawn;
import lightbouncers.math.Vector2D;
import lightbouncers.world.World;

public abstract class Projectile extends Pawn
{
    public Projectile(Vector2D position, double rotation, World world, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, world, maxVelocity, acceleration, scale);
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
    }
}
