package lightbouncers.objects.lights;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import lightbouncers.math.Vector2D;
import lightbouncers.objects.Actor;
import lightbouncers.objects.environment.WallBox;
import lightbouncers.objects.environment.WallCircle;
import lightbouncers.objects.pawns.characters.PlayerCharacter;
import lightbouncers.tracing.RayMarch;
import lightbouncers.tracing.TraceResult2D;

import java.util.ArrayList;

public class Light
{
    private Vector2D position;
    private double range;
    private double brightness;
    private Color emissionColor;
    private Color castColor;

    public Light(Vector2D position, double range, double brightness, Color emissionColor, Color castColor)
    {
        this.position = position;
        this.range = range;
        this.brightness = brightness;
        this.emissionColor = emissionColor;
        this.castColor = castColor;
//        this.emissionColor = Color.rgb((int)emissionColor.getRed(), (int)emissionColor.getGreen(), (int)emissionColor.getBlue(), brightness);
//        this.castColor = Color.rgb((int)castColor.getRed(), (int)castColor.getGreen(), (int)castColor.getBlue(), brightness);
    }

    public void draw(GraphicsContext graphicsContext, ArrayList<Actor> actors)
    {
        drawNew(graphicsContext, actors);
    }

    private void drawNew(GraphicsContext graphicsContext, ArrayList<Actor> actors)
    {
        Actor lasttraced = null;
        Vector2D sweepStartPosition = null;
        Vector2D lastSweepPosition = null;

        double startAngle = 0;
        double angleCounter = 0;

        for(double i = 0; i < Math.PI * 2; i += Math.PI / 1000)
        {
            TraceResult2D traceResult = RayMarch.sphereTrace2D(this.position, i, actors, this.range, null);

            if(lasttraced != traceResult.getObjectHit())
            {
                if(sweepStartPosition != null)
                {
                    graphicsContext.setFill(this.emissionColor);

                    if(lasttraced != null)
                    {
                        double[] xPoints = {this.position.x, sweepStartPosition.x, lastSweepPosition.x};
                        double[] yPoints = {this.position.y, sweepStartPosition.y, lastSweepPosition.y};
                        graphicsContext.fillPolygon(xPoints, yPoints, 3);

                        graphicsContext.setFill(this.castColor);
                        graphicsContext.strokeLine(sweepStartPosition.x, sweepStartPosition.y, lastSweepPosition.x, lastSweepPosition.y);
                    }
                    else
                    {
                        graphicsContext.fillArc(this.position.x, this.position.y, this.range, this.range, Math.toDegrees(startAngle), Math.toDegrees(angleCounter), null);
                    }
                }
                sweepStartPosition = traceResult.getHitPoint();
                lasttraced = traceResult.getObjectHit();
                startAngle = i;
                angleCounter = 0;
            }
            lastSweepPosition = traceResult.getHitPoint();
            angleCounter += Math.PI / 1000;
        }
    }

    private void drawOld(GraphicsContext graphicsContext, ArrayList<Actor> actors)
    {
        for(double i = 0; i < Math.PI * 2; i += Math.PI / 5000)
        {
            TraceResult2D traceResult = RayMarch.sphereTrace2D(this.position, i, actors, this.range, null);
            graphicsContext.setStroke(this.emissionColor);
            graphicsContext.strokeLine(this.position.x, this.position.y, traceResult.getHitPoint().x, traceResult.getHitPoint().y);

            if(traceResult.getObjectHit() != null)
            {
                if(traceResult.getObjectHit() instanceof WallBox || traceResult.getObjectHit() instanceof WallCircle)
                {
                    graphicsContext.setFill(this.castColor);
                    graphicsContext.fillOval(traceResult.getHitPoint().x - 1, traceResult.getHitPoint().y - 1, 2, 2);
                }
                else if(traceResult.getObjectHit() instanceof PlayerCharacter)
                {
                    //graphicsContext.setFill(((PlayerCharacter)traceResult.getObjectHit()).getColor());
                    //graphicsContext.fillOval(traceResult.getHitPoint().x - 2, traceResult.getHitPoint().y - 2, 4, 4);
                }
            }
        }
    }

    public Vector2D getPosition()
    {
        return this.position;
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
