package lightbouncers.objects.items;

import lightbouncers.objects.Actor;
import lightbouncers.math.Vector2D;
import lightbouncers.world.World;

public abstract class Item extends Actor
{
    public Item(Vector2D worldPosition, double rotation, World world)
    {
        super(worldPosition, rotation, world);
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
    }

    public abstract void onItemUse();

    public abstract void onItemStopUse();
}
