package lightbouncers.objects.items.weapons.guns;

import lightbouncers.objects.items.Item;
import lightbouncers.math.Vector2D;

public abstract class Gun extends Item
{
    public Gun(Vector2D worldPosition, double rotation, String name)
    {
        super(worldPosition, rotation, name);
    }
}
