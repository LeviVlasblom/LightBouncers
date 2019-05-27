package lightbouncers.objects.items;

import lightbouncers.objects.Actor;
import lightbouncers.math.Vector2D;

public abstract class Item extends Actor
{
    public Item(Vector2D worldPosition, double rotation)
    {
        super(worldPosition, rotation);
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
    }

    public abstract void onItemUse();

    public abstract void onItemStopUse();
}
