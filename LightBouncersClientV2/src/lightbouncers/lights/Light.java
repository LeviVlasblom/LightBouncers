package lightbouncers.lights;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import lightbouncers.game.Actor;
import lightbouncers.math.Vector2D;
import lightbouncers.tracing.RayMarch;
import lightbouncers.tracing.TraceResult2D;

import java.util.ArrayList;

public class Light
{
    private Vector2D position;
    private double rotation;
    private double angleVisiblity;
    private double range;
    private double brightness;
    private Color emissionColor;
    private Color castColor;

    public Light(Vector2D position, double rotation, double angleVisiblity, double range, double brightness, Color emissionColor, Color castColor)
    {
        this.position = position;
        this.rotation = 0;
        this.angleVisiblity = angleVisiblity;
        this.range = range;
        this.brightness = brightness;
        this.emissionColor = emissionColor;
        this.castColor = castColor;
    }

    public void draw(GraphicsContext graphicsContext, ArrayList<Actor> actors)
    {
        drawOld(graphicsContext, actors);
    }

    private void drawOld(GraphicsContext graphicsContext, ArrayList<Actor> actors)
    {
//        for(double i = this.rotation - (this.angleVisiblity / 2); i < this.rotation + (this.angleVisiblity / 2); i += Math.PI / 4000)
//        {
//            TraceResult2D traceResult = RayMarch.sphereTrace2D(this.position, i, actors, this.range, null);
//            graphicsContext.setStroke(this.emissionColor);
//            graphicsContext.strokeLine(this.position.x, this.position.y, traceResult.getHitPoint().x, traceResult.getHitPoint().y);
//
//            if(traceResult.getObjectHit() != null)
//            {
//                graphicsContext.setFill(this.castColor);
//                graphicsContext.fillOval(traceResult.getHitPoint().x - 1, traceResult.getHitPoint().y - 1, 2, 2);
//            }
//        }
        for(double i = 0; i < Math.PI * 2; i += Math.PI / 2000)
        {
            TraceResult2D traceResult = RayMarch.sphereTrace2D(this.position, i, actors, this.range, null);
            graphicsContext.setStroke(this.emissionColor);
            graphicsContext.strokeLine(this.position.x, this.position.y, traceResult.getHitPoint().x, traceResult.getHitPoint().y);

            if(traceResult.getObjectHit() != null)
            {
                graphicsContext.setFill(this.castColor);
                graphicsContext.fillOval(traceResult.getHitPoint().x - 1, traceResult.getHitPoint().y - 1, 2, 2);
            }
        }
    }

    public Vector2D getPosition()
    {
        return this.position;
    }

    public double getRotation()
    {
        return this.rotation;
    }

    public double getAngleVisiblity()
    {
        return this.angleVisiblity;
    }

    public double getRange()
    {
        return this.range;
    }

    public double getBrightness()
    {
        return this.brightness;
    }

    public Color getEmissionColor()
    {
        return this.emissionColor;
    }

    public Color getCastColor()
    {
        return this.castColor;
    }

    public void setPosition(Vector2D position)
    {
        this.position = position;
    }

    public void setRotation(double rotation)
    {
        this.rotation = rotation;
    }

    public void setAngleVisiblity(double angleVisiblity)
    {
        this.angleVisiblity = angleVisiblity;
    }

    public void setRange(double range)
    {
        this.range = range;
    }

    public void setBrightness(double brightness)
    {
        this.brightness = brightness;
    }

    public void setEmissionColor(Color emissionColor)
    {
        this.emissionColor = emissionColor;
    }

    public void setCastColor(Color castColor)
    {
        this.castColor = castColor;
    }
}
