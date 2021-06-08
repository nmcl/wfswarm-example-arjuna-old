import java.util.*;

/**
 * A map of the tunnel system.
 */

public class Map
{
    public Map (Vector<String> input, boolean debug)
    {
        _theMap = new Vector<Cell>();
        _nodeMap = null;
        _keys = null;
        _doors = null;
        _debug = debug;

        createMap(input);
    }

    public Coordinate getEntrance ()
    {
        return _entrance;
    }

    public Node getStartingNode ()
    {
        return createGraph();
    }

    public int numberOfDoors ()
    {
        return ((_doors == null) ? 0 : _doors.length);
    }

    public int numberOfKeys ()
    {
        return ((_keys == null) ? 0 : _keys.length);
    }

    @Override
    public String toString ()
    {
        Enumeration<Cell> iter = _theMap.elements();
        int index = 1;
        String str = "Map <"+_maxX+", "+_maxY+">:\n\n";

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

    // create the map from input
    
    private void createMap (Vector<String> input)
    {
        Enumeration<String> iter = input.elements();
        Vector<char> doors = new Vector<char>();
        Vector<char> keys = new Vector<char>();
        int y = -1;

        _maxX = input.get(0).length(); // all lines are the same length

        while (iter.hasMoreElements())
        {
            y++;

            String line = iter.nextElement();

            for (int x = 0; x < _maxX; x++)
            {
                Cell theCell = new Cell(new Coordinate(x, y), line.charAt(x), _debug);

                if (theCell.isEntrance())
                    _entrance = theCell.position();  // should be only one!
                else
                {
                    if (theCell.isKey())
                        _numberOfKeys++;
                    else
                    {
                        if (theCell.isDoor())
                            _numberOfDoors++;
                    }
                }

                if (_debug)
                    System.out.println(theCell);

                _theMap.add(theCell);
            }
        }

        _maxY = y+1;
    }

     // from the map create a graph

     private Node createGraph ()  // temporary public for testing!
     {
         if (_nodeMap != null)
            return _nodeMap[_entrance.getX()][_entrance.getY()];

         Enumeration<Cell> iter = _theMap.elements();
         int x = 0;
         int y = 0;
 
         _nodeMap = new Node[_maxX][_maxY];
 
         while (iter.hasMoreElements())
         {
             _nodeMap[x][y] = new Node(iter.nextElement());
 
             x++;
 
             if (x == _maxX)
             {
                 x = 0;
                 y++;
             }
         }
 
         for (int i = 0; i < _maxX; i++)
         {
             for (int j = 0; j < _maxY; j++)
             {
                 Node up = null;
                 Node down = null;
                 Node left = null;
                 Node right = null;
 
                 if (i -1 >= 0)
                     left = _nodeMap[i-1][j];
                 
                 if (i +1 < _maxX)
                     right = _nodeMap[i +1][j];
 
                 if (j -1 >= 0)
                     up = _nodeMap[i][j -1];
 
                 if (j +1 < _maxY)
                     down = _nodeMap[i][j +1];
 
                 _nodeMap[i][j].setLinks(up, down, left, right);
             }
         }
 
         if (_debug)
         {
             for (x = 0; x < _maxX; x++)
             {
                 for (y = 0; y < _maxY; y++)
                 {
                     System.out.println("\n"+_nodeMap[x][y]);
                 }
             }
         }
 
         return _nodeMap[_entrance.getX()][_entrance.getY()];
     }

    private Vector<Cell> _theMap;
    private Node[][] _nodeMap;
    private int _maxX;
    private int _maxY;
    private Coordinate _entrance;
    private char[] _keys;
    private char[] _doors;
    private int _numberOfKeys;
    private int _numberOfDoors;
    private boolean _debug;
}