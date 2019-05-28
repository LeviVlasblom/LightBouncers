package lightbouncers.objects.environment;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;
import lightbouncers.objects.pawns.characters.PlayerCharacter;
import lightbouncers.world.World;

public abstract class EnvironmentObject extends Actor
{
    public EnvironmentObject(Vector2D worldPosition, double rotation, World world)
    {
        super(worldPosition, rotation, world);
    }

    @Override
    public void checkCollision(Actor actor)
    {
        Vector2D closestPointOnObject = getClosestPoint(actor.getWorldPosition());
        Vector2D closestPointOnActor = actor.getClosestPoint(closestPointOnObject);

        double distance = Vector2D.distance(closestPointOnActor, closestPointOnObject);
        if(actor instanceof PlayerCharacter && distance < ((PlayerCharacter)actor).getRadius())
        {
            Vector2D direction = Vector2D.direction(closestPointOnObject, actor.getWorldPosition());
            actor.setWorldPosition(Vector2D.add(closestPointOnObject, Vector2D.multiply(direction, ((PlayerCharacter)actor).getRadius() + 10)));
        }
    }
}
