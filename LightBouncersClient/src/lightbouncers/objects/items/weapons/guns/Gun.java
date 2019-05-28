package lightbouncers.objects.items.weapons.guns;

import lightbouncers.objects.items.Item;
import lightbouncers.math.Vector2D;
import lightbouncers.world.World;

public abstract class Gun extends Item
{
    private FireType fireType;
    private int fireRatePerMinute;
    private double fireDelayInSeconds;

    protected double fireRateCounter;
    private boolean isFiring;

    private int burstAmmount;
    private int burstCounter;

    public Gun(Vector2D worldPosition, double rotation, World world, FireType fireType, int fireRatePerMinute)
    {
        super(worldPosition, rotation, world);

        this.fireType = fireType;
        this.fireRatePerMinute = fireRatePerMinute;
        this.fireDelayInSeconds = (60.0 / fireRatePerMinute);
        this.fireRateCounter = 0.0;
        this.isFiring = false;
        this.burstAmmount = 4;
        this.burstCounter = 0;
    }

    @Override
    public void update(double deltatime)
    {
        if(this.isFiring)
        {
            if(this.fireType == FireType.FULLAUTO)
            {
                this.fireRateCounter += deltatime;
                if(this.fireRateCounter >= this.fireDelayInSeconds)
                {
                    fire();
                    this.fireRateCounter -= this.fireDelayInSeconds;
                }
            }
            else if(this.fireType == FireType.BURST)
            {
                this.fireRateCounter += deltatime;
                if(this.fireRateCounter >= this.fireDelayInSeconds && this.burstCounter < this.burstAmmount)
                {
                    fire();
                    this.fireRateCounter -= this.fireDelayInSeconds;
                    this.burstCounter++;
                }
                else
                {
                    this.isFiring = false;
                    this.burstCounter = 0;
                }
            }
            else if(this.fireType == FireType.SEMIAUTO)
            {
                this.isFiring = false;
                fire();
            }
        }
        else
        {
            this.fireRateCounter = 0;
        }
        super.update(deltatime);
    }

    @Override
    public void onItemUse()
    {
        this.isFiring = true;
        fire();
        if(this.fireType == FireType.BURST)
        {
            this.burstCounter++;
        }
    }

    @Override
    public void onItemStopUse()
    {
        this.isFiring = false;
    }

    protected abstract void fire();

    public void setFireRatePerMinute(int fireRatePerMinute)
    {
        if(fireRatePerMinute != 0)
        {
            this.fireRatePerMinute = fireRatePerMinute;
            this.fireDelayInSeconds = (60 / fireRatePerMinute);
        }
    }

    protected enum FireType
    {
        SEMIAUTO,
        BURST,
        FULLAUTO
    }
}
