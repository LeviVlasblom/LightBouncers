package lightbouncers.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lightbouncers.math.Vector2D;

import java.io.Serializable;

public class Player extends Actor implements Serializable
{
    private String username;
    private double width;
    private double height;
    private double movementSpeed;

    private boolean isMovingLeft, isMovingRight;

    private int points;

    public Player(Color color, String username)
    {
        super(Vector2D.zero(), color);

        this.width = 100;
        this.height = 10;
        this.movementSpeed = 100.0;
        this.username = username;
        this.isMovingLeft = false;
        this.isMovingRight = false;
    }

    @Override
    public void update(double deltatime)
    {
        if(this.isMovingLeft)
        {
            this.position = Vector2D.add(this.position, Vector2D.multiply(Vector2D.left(), this.movementSpeed * deltatime));
        }
        if(this.isMovingRight)
        {
            this.position = Vector2D.add(this.position, Vector2D.multiply(Vector2D.right(), this.movementSpeed * deltatime));
        }
    }

    @Override
    public void draw(GraphicsContext graphicsContext)
    {
//        graphicsContext.setFill(this.color);
//        graphicsContext.fillRect(this.position.x, this.position.y, this.width, this.height);
        graphicsContext.setFill(this.color);
        graphicsContext.fillRect(this.position.x - (this.width / 2), this.position.y - (this.height / 2), this.width, this.height);
    }

    @Override
    public double getDistanceFromPoint(Vector2D point)
    {
        double closeX = point.x;
        double closeY = point.y;

        //Check left
        if(point.x < this.position.x - (this.width / 2))
        {
            closeX = this.position.x - (this.width / 2);
        }
        //Check right
        if(point.x > this.position.x + (this.width / 2))
        {
            closeX = this.position.x + (this.width / 2);
        }
        //Check top
        if(point.y < this.position.y - (this.height / 2))
        {
            closeY = this.position.y - (this.height / 2);
        }
        //Check bottom
        if(point.y > this.position.y + (this.height / 2))
        {
            closeY = this.position.y + (this.height / 2);
        }

        Vector2D closestPoint = new Vector2D(closeX, closeY);

        return Vector2D.distance(closestPoint, point);
    }

    @Override
    public Vector2D getClosestPoint(Vector2D point)
    {
        double closeX = point.x;
        double closeY = point.y;

        //Check left
        if(point.x < this.position.x - (this.width / 2))
        {
            closeX = this.position.x - (this.width / 2);
        }
        //Check right
        if(point.x > this.position.x + (this.width / 2))
        {
            closeX = this.position.x + (this.width / 2);
        }
        //Check top
        if(point.y < this.position.y - (this.height / 2))
        {
            closeY = this.position.y - (this.height / 2);
        }
        //Check bottom
        if(point.y > this.position.y + (this.height / 2))
        {
            closeY = this.position.y + (this.height / 2);
        }

        return new Vector2D(closeX, closeY);
    }

    public void addPoint()
    {
        this.points++;
    }

    public double getWidth()
    {
        return this.width;
    }

    public double getHeight()
    {
        return this.height;
    }

    public int getPoints()
    {
        return this.points;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setMovingLeft(boolean movingLeft)
    {
        this.isMovingLeft = movingLeft;
    }

    public void setMovingRight(boolean movingRight)
    {
        this.isMovingRight = movingRight;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }
}
