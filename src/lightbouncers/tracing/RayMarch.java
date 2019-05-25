package lightbouncers.tracing;

import javafx.scene.canvas.GraphicsContext;
import lightbouncers.objects.ISceneObject;
import lightbouncers.objects.SceneObject;
import lightbouncers.vectormath.Vector2D;

import java.util.ArrayList;

public class RayMarch
{
    public static TraceResult2D sphereTrace2D(Vector2D origin, double direction, ArrayList<SceneObject> objects, double maxDistance, GraphicsContext graphicsContext)
    {
        Vector2D currentPoint = origin.clone();
        ISceneObject closestObject;
        double totalDistance = 0;
        TraceResult2D traceResult2D = new TraceResult2D();

        while(true)
        {
            if(objects != null)
            {
                closestObject = getClosestObject(currentPoint, objects);
                double distanceToObject = closestObject.getDistanceFromPoint(currentPoint);
                totalDistance += distanceToObject;

                if(distanceToObject > 0.01)
                {
                    if(totalDistance <= maxDistance)
                    {
                        currentPoint = Vector2D.fromAngleWithPosition(currentPoint, direction, distanceToObject);
//                        graphicsContext.setFill(Color.WHITE);
//                        graphicsContext.fillOval(currentPoint.x - 2, currentPoint.y - 2, 4, 4);
//                        graphicsContext.setStroke(Color.WHITE);
//                        graphicsContext.strokeOval(currentPoint.x - distanceToObject, currentPoint.y - distanceToObject, distanceToObject * 2, distanceToObject * 2);
                    }
                    else
                    {
                        currentPoint = Vector2D.fromAngleWithPosition(origin, direction, maxDistance);
                        traceResult2D.setHitPoint(currentPoint);
                        break;
                    }
                }
                else
                {
                    traceResult2D.setObjectHit(closestObject);
                    traceResult2D.setHitPoint(currentPoint);
                    break;
                }
            }
            else
            {
                currentPoint = Vector2D.fromAngleWithPosition(origin, direction, maxDistance);
                traceResult2D.setHitPoint(currentPoint);
                break;
            }
        }

        return traceResult2D;
    }

    private static ISceneObject getClosestObject(Vector2D point, ArrayList<SceneObject> objects)
    {
        ISceneObject closestObject = objects.get(0);
        double closestDistance = closestObject.getDistanceFromPoint(point);

        for(ISceneObject object : objects)
        {
            double distance = object.getDistanceFromPoint(point);
            if(distance < closestDistance)
            {
                closestObject = object;
                closestDistance = distance;
            }
        }

        return closestObject;
    }
}
