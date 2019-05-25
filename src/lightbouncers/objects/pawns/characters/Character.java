package lightbouncers.objects.pawns.characters;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lightbouncers.objects.pawns.Pawn;
import lightbouncers.math.Vector2D;

public abstract class Character extends Pawn
{
    public Character(Vector2D position, double rotation, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, maxVelocity, acceleration, scale);
    }

    public abstract void onMouseMoved(MouseEvent event);

    public abstract void onMousePressed(MouseEvent event);

    public abstract void onMouseReleased(MouseEvent event);

    public abstract void onKeyPressed(KeyEvent event);

    public abstract void onKeyReleased(KeyEvent event);
}
