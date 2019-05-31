package lightbouncers.math;

import java.io.Serializable;

public class Vector2D implements Serializable
{
	public double magnitude;
	public double x;
	public double y;
	
	public Vector2D normalized;
	
	public Vector2D()
	{
		this.x = 0.0f;
		this.y = 0.0f;
		this.magnitude = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
		this.magnitude = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
	}
	
	public Vector2D(double x, double y, double magnitude)
	{
		this.x = x;
		this.y = y;
		this.magnitude = magnitude;
	}
	
	public Vector2D normalized()
	{
		this.normalized = new Vector2D((this.x / this.magnitude), (this.y / this.magnitude), 1.0f);
		return this.normalized;
	}
	
	public void normalize()
	{
		this.x = (this.x / magnitude);
		this.y = (this.y / magnitude);
		this.magnitude = 1.0f;
	}

	public double magnitute()
	{
		this.magnitude = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
		return this.magnitude;
	}

	public void flip()
	{
		flipX();
		flipY();
	}

	public void flipX()
	{
		this.x = -this.x;
	}

	public void flipY()
	{
		this.y = -this.y;
	}

	public javafx.geometry.Point2D toPoint2DJavaFX()
	{
		return new javafx.geometry.Point2D(this.x, this.y);
	}

	public java.awt.geom.Point2D toPoint2DAWT()
	{
		return new java.awt.geom.Point2D.Double(this.x, this.y);
	}
	
	public static Vector2D normalize(Vector2D vector2D)
	{
		vector2D.x = (vector2D.x / vector2D.magnitude);
		vector2D.y = (vector2D.y / vector2D.magnitude);
		vector2D.magnitude = 1.0f;
		
		return vector2D;
	}
	
	public static double dot(Vector2D a, Vector2D b)
	{
		return (a.x * b.x) + (a.y * b.y);
	}

	public static double cross(Vector2D a, Vector2D b)
	{
		return (a.x * b.y) - (a.y * b.x);
	}
	
	public static double distance(Vector2D a, Vector2D b)
	{
		return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
	}

	public static Vector2D lerp(Vector2D a, Vector2D b, double t)
	{
		return Vector2D.add(a, Vector2D.multiply(Vector2D.subtract(b, a), t));
	}

	public static Vector2D fromAngle(Vector2D a, double angle, double length)
	{
		return new Vector2D(a.normalized().x * length * Math.cos(angle), a.normalized().y * length * Math.sin(angle));
	}

	public static Vector2D fromAngleWithPosition(Vector2D a, double angle, double length)
	{
		return new Vector2D(a.x + Math.cos(angle) * length, a.y + Math.sin(angle) * length);
	}

	public static double getAngle(Vector2D a)
	{
		return Math.atan2(a.y, a.x);
	}

	public static Vector2D direction(Vector2D a, Vector2D b)
	{
		return Vector2D.normalize(Vector2D.subtract(b, a));
	}
	
	public static Vector2D up()
	{
		return new Vector2D(0.0f, 1.0f, 1.0f);
	}
	
	public static Vector2D down()
	{
		return new Vector2D(0.0f, -1.0f, 1.0f);
	}
	
	public static Vector2D right()
	{
		return new Vector2D(1.0f, 0.0f, 1.0f);
	}
	
	public static Vector2D left()
	{
		return new Vector2D(-1.0f, 0.0f, 1.0f);
	}
	
	public static Vector2D zero()
	{
		return new Vector2D(0.0f, 0.0f, 0.0f);
	}
	
	public static Vector2D add(Vector2D a, Vector2D b)
	{
		return new Vector2D((a.x + b.x), (a.y + b.y));
	}

	public static Vector2D add(Vector2D a, double b)
	{
		return new Vector2D((a.x + b), (a.y + b));
	}
	
	public static Vector2D subtract(Vector2D a, Vector2D b)
	{
		return new Vector2D((a.x - b.x), (a.y - b.y));
	}
	
	public static Vector2D divide(Vector2D a, Vector2D b)
	{
		return new Vector2D((a.x / b.x), (a.y / b.y));
	}
	
	public static Vector2D divide(Vector2D a, double b)
	{
		return new Vector2D((a.x / b), (a.y / b));
	}
	
	public static Vector2D divide(double a, Vector2D b)
	{
		return new Vector2D((a / b.x), (a / b.y));
	}
	
	public static Vector2D multiply(Vector2D a, Vector2D b)
	{
		return new Vector2D((a.x * b.x), (a.y * b.y));
	}
	
	public static Vector2D multiply(Vector2D a, double b)
	{
		return new Vector2D((a.x * b), (a.y * b));
	}
	
	public static Vector2D multiply(double a, Vector2D b)
	{
		return new Vector2D((a * b.x), (a * b.y));
	}

	public boolean equals(Vector2D vector2D)
	{
		return (this.x == vector2D.x && this.y == vector2D.y);
	}

	public Vector2D clone()
	{
		return new Vector2D(this.x, this.y);
	}
}
