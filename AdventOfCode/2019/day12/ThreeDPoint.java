import java.util.Objects;

/*
 * Represents a <x, y, z> point.
 */

public class ThreeDPoint
{
    public ThreeDPoint (int x, int y, int z)
    {
        _x = x;
        _y = y;
        _z = z;
    }

    public final int getX ()
    {
        return _x;
    }

    public final void setX (int x)
    {
        System.out.println("**changing "+_x+" to "+x);

        _x = x;
    }

    public final int getY ()
    {
        return _y;
    }

    public final void setY (int y)
    {
        System.out.println("**changing "+_y+" to "+y);

        _y = y;
    }

    public final int getZ ()
    {
        return _z;
    }

    public final void setZ (int z)
    {
        System.out.println("**changing "+_z+" to "+z);

        _z = z;
    }

    @Override
    public int hashCode ()
    {
        return Objects.hash(_x, _y, _z);
    }

    @Override
    public boolean equals (Object obj)
    {
        if (obj == null)
            return false;

        if (this == obj)
            return true;
        
        if (getClass() == obj.getClass())
        {
            ThreeDPoint temp = (ThreeDPoint) obj;

            return ((_x == temp._x) && (_y == temp._y) && (_z == temp._z));
        }

        return false;
    }

    protected int _x;
    protected int _y;
    protected int _z;
}