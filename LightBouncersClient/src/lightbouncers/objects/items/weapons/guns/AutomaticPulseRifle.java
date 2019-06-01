package lightbouncers.objects.items.weapons.guns;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.Main;
import lightbouncers.math.Vector2D;
import lightbouncers.net.ProjectileObject;
import lightbouncers.net.client.Client;
import lightbouncers.net.client.SessionJSONUtil;
import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.projectiles.Projectile;
import lightbouncers.world.World;
import org.json.simple.JSONObject;

public class AutomaticPulseRifle extends Gun
{
    public AutomaticPulseRifle(Vector2D worldPosition, double rotation, World world)
    {
        super(worldPosition, rotation, world, FireType.FULLAUTO, 300);
    }

    @Override
    protected void fire()
    {
        Projectile projectile = new Projectile(this.worldPosition, this.rotation, this.world, 20.0, 400.0, 1.0, Vector2D.fromAngleWithPosition(Vector2D.zero(), this.rotation, 10).normalized(), 6);
        //AutomaticPulseRifleProjectile projectile = new AutomaticPulseRifleProjectile(this.worldPosition, this.rotation, this.world, Vector2D.fromAngleWithPosition(Vector2D.zero(), this.rotation, 10).normalized());
        //this.world.addProjectile(projectile);

        ProjectileObject projectileObject = new ProjectileObject(projectile.getWorldPosition(), Vector2D.fromAngleWithPosition(this.worldPosition, this.rotation, 20.0), projectile.getRadius(), Main.username);
        JSONObject jsonObject = SessionJSONUtil.getProjectileObjectJson(projectileObject);
        jsonObject.put("command", "addprojectile");
        Client.getInstance("", 0, null, null).sendUTF(jsonObject.toJSONString());
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
        //graphicsContext.setStroke(Color.YELLOW);
        //graphicsContext.strokeLine(this.worldPosition.x, this.worldPosition.y, Vector2D.fromAngleWithPosition(this.worldPosition, this.rotation, 10).x, Vector2D.fromAngleWithPosition(this.worldPosition, this.rotation, 10).y);
    }

    @Override
    public void checkCollision(Actor actor)
    {

    }

    @Override
    public double getDistanceFromPoint(Vector2D point)
    {
        return 0;
    }

    @Override
    public Vector2D getClosestPoint(Vector2D point)
    {
        return null;
    }
}
