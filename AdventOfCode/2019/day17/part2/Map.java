import java.util.*;

/**
 * Take an image from the camera and turn it into a map
 * which can be used to move around.
 */

public class Map
{
    public Map (Image img, boolean debug)
    {
        _theMap = new Vector<Cell>();
        _maxX = 0;
        _maxY = 0;
        _originalImage = img;
        _debug = debug;

        createMapFromImage(img.scannedLines());
    }

    public Map (Map toCopy)
    {
        _theMap = new Vector<Cell>();
        _maxX = 0;
        _maxY = 0;
        _originalImage = toCopy._originalImage;
        _debug = toCopy._debug;

        createMapFromImage(_originalImage.scannedLines());
    }

    public Coordinate findStartingPoint ()
    {
        Enumeration<Cell> iter = _theMap.elements();

        while (iter.hasMoreElements())
        {
            Cell cur = iter.nextElement();

            if (cur.getContents().equals(CellId.ROBOT_FACING_UP))
                return cur.position();
        }

        return null;
    }

    public boolean isScaffold (Coordinate coord)
    {
        if (!valid(coord))
            return false;

        Cell temp = new Cell(coord);
        int index = _theMap.indexOf(temp);

        temp = _theMap.get(index);

        return (temp.getContents().equals(CellId.SCAFFOLDING));
    }

    @Override
    public String toString ()
    {
        Enumeration<Cell> iter = _theMap.elements();
        int index = 1;
        String str = "Map dimensions < "+_maxX+", "+_maxY+" >\n";

        while (iter.hasMoreElements())
        {
            str += iter.nextElement().getContents();

            if (index++ == _maxX)
            {
                str += "\n";

                index = 1;
            }
        }

        return str;
    }

    protected boolean valid (Coordinate coord)
    {
        if (coord != null)
        {
            if ((coord.getX() >= 0) && (coord.getY() >= 0))
            {
                if ((coord.getX() < _maxX) && (coord.getY() < _maxY))
                    return true;
            }
        }

        return false;
    }

    protected void createMapFromImage (String[] lines)
    {
        int lineLength = lines[0].length(); // all lines are the same length

        _maxY = lines.length;
        _maxX = lineLength;

        for (int y = 0; y < lines.length -1; y++)
        {
            for (int x = 0; x < lineLength; x++)
            {
                Cell theCell = new Cell(new Coordinate(x, y), String.valueOf(lines[y].charAt(x)), _debug);

                if (_debug)
                    System.out.println(theCell);

                _theMap.add(theCell);
            }
        }
    }

    protected Map ()
    {
        _theMap = new Vector<Cell>();
        _maxX = 0;
        _maxY = 0;
        _originalImage = null;
        _debug = false;
    }

    protected Vector<Cell> _theMap;
    protected int _maxX;
    protected int _maxY;
    protected Image _originalImage;
    protected boolean _debug;
}