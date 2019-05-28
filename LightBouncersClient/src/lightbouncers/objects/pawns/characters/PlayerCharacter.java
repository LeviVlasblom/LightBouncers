package lightbouncers.objects.pawns.characters;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lightbouncers.math.Angle;
import lightbouncers.objects.pawns.Pawn;
import lightbouncers.math.Vector2D;
import lightbouncers.world.World;

public abstract class PlayerCharacter extends Pawn
{
    protected double radius;
    private boolean isMovingUp, isMovingDown, isMovingLeft, isMovingRight;

    public PlayerCharacter(Vector2D position, double rotation, World world, double maxVelocity, double acceleration, double scale)
    {
        super(position, rotation, world, maxVelocity, acceleration, scale);

        this.isMovingUp = false;
        this.isMovingDown = false;
        this.isMovingLeft = false;
        this.isMovingRight = false;
        this.radius = 10;
    }

    @Override
    public void update(double deltatime)
    {
        super.update(deltatime);
    }

    @Override
    protected void updateMovement(double deltatime)
    {
        Vector2D direction = Vector2D.zero();

        if(this.isMovingUp)
        {
            direction = Vector2D.add(direction, Vector2D.down());
        }
        if(this.isMovingDown)
        {
            direction = Vector2D.add(direction, Vector2D.up());
        }
        if(this.isMovingLeft)
        {
            direction = Vector2D.add(direction, Vector2D.left());
        }
        if(this.isMovingRight)
        {
            direction = Vector2D.add(direction, Vector2D.right());
        }

        if(this.isMovingUp || this.isMovingDown || this.isMovingLeft || this.isMovingRight)
        {
            this.direction = direction;
        }

        super.updateMovement(deltatime);
    }

    public abstract void onMouseMoved(MouseEvent event);

    public abstract void onMousePressed(MouseEvent event);

    public abstract void onMouseReleased(MouseEvent event);

    public void onKeyPressed(KeyEvent event)
    {
        if(event.getCode() == KeyCode.W)
        {
            this.isMovingUp = true;
            this.isMoving = true;
        }
        if(event.getCode() == KeyCode.S)
        {
            this.isMovingDown = true;
            this.isMoving = true;
        }
        if(event.getCode() == KeyCode.A)
        {
            this.isMovingLeft = true;
            this.isMoving = true;
        }
        if(event.getCode() == KeyCode.D)
        {
            this.isMovingRight = true;
            this.isMoving = true;
        }
    }

    public void onKeyReleased(KeyEvent event)
    {
        if(event.getCode() == KeyCode.W)
        {
            this.isMovingUp = false;
        }
        if(event.getCode() == KeyCode.S)
        {
            this.isMovingDown = false;
        }
        if(event.getCode() == KeyCode.A)
        {
            this.isMovingLeft = false;
        }
        if(event.getCode() == KeyCode.D)
        {
            this.isMovingRight = false;
        }

        if(!this.isMovingUp && !this.isMovingDown && !this.isMovingLeft && !this.isMovingRight)
        {
            this.isMoving = false;
            this.direction.flip();
        }
    }

    public double getRadius()
    {
        return this.radius;
    }
}
