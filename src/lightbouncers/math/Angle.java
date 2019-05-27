package lightbouncers.math;

public class Angle
{
    public static double flipAngle(double angle)
    {
        angle = flipAngleX(angle);
        angle = flipAngleY(angle);

        return angle;
    }

    public static double flipAngleX(double angle)
    {
        angle = normalizeAngle(angle);
        angle = (Math.PI * 2) - angle;

        return angle;
    }

    public static double flipAngleY(double angle)
    {
        angle = normalizeAngle(angle);

        if (angle < Math.PI)
        {
            angle = Math.PI - angle;
        }
        else
        {
            angle = (Math.PI * 2) - angle + Math.PI;
        }
        return angle;
    }

    public static double normalizeAngle(double angle)
    {
        if (angle < 0)
        {
            int backRevolutions = (int)(-angle / (Math.PI * 2));
            return angle + (Math.PI * 2) * (backRevolutions + 1);
        }
        else
        {
            return angle % (Math.PI * 2);
        }
    }
}
