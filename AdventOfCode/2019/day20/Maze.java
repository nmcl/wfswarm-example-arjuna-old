import java.util.*;
import java.io.*;

public class Maze
{
    public Maze (String data, boolean debug)
    {
        _theMaze = new Vector<Tile>();

        if (!loadData(data))
            System.out.println("Error in loading data file: "+data);

        _debug = debug;
    }

    public String printWithPortals ()
    {
        return createRepresentation(false);
    }

    public String toString ()
    {
        return createRepresentation(true);
    }

    public final int[] getXDimensions ()
    {
        return new int[] { _minX, _maxX };
    }

    public final int[] getYDimensions ()
    {
        return new int[] { _minY, _maxY };
    }

    private String createRepresentation (boolean ignorePortals)
    {
        Enumeration<Tile> iter = _theMaze.elements();
        String str = "Maze < "+_minX+", "+_maxX+", "+_minY+", "+_maxY+" >\n";
        int y = 0;

        while (iter.hasMoreElements())
        {
            Tile theEntry = iter.nextElement();

            /*
             * Print without portals?
             */

            if (ignorePortals)
            {
                if (TileId.PORTAL != theEntry.content())
                    str += theEntry.toString();
                else
                    str += TileId.SPACE;
            }
            else
            {
                if (TileId.PORTAL == theEntry.content())
                {
                    Portal p = (Portal) theEntry;

                    str += p.getId();
                }
                else
                    str += theEntry.toString();
            }

            y++;

            if (y == _width)
            {
                y = 0;
                str += "\n";
            }
        }

        return str;
    }
    
    private final boolean loadData (String file)
    {
        BufferedReader reader = null;
        boolean valid = true;

        try
        {
            reader = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                char[] asChar = line.toCharArray();  // all lines are the same length
                
                if (_width == 0)
                    _width = asChar.length;

                for (int i = 0; i < _width; i++)
                {
                    switch (asChar[i])
                    {
                        case TileId.WALL:
                        case TileId.PASSAGE:
                        {
                            _theMaze.add(new Tile(new Coordinate(i, _height), asChar[i]));
                        }
                        break;
                        case TileId.SPACE:
                        {
                            _theMaze.add(new Tile(new Coordinate(i, _height), asChar[i]));

                            _minX = Math.min(_minX, i);
                            _maxX = Math.max(_maxX, i);
                            _minY = Math.min(_minY, _height);
                            _maxY = Math.max(_maxY, _height);
                        }
                        break;
                        default:  // add to Portal list
                        {
                            // have portal at the PASSAGE and have that instance print it?

                            Portal p = new Portal(new Coordinate(i, _height), asChar[i]);

                            _theMaze.add(p);
                        }
                        break;
                    }
                }
            }

            _height++;
        }
        catch (Throwable ex)
        {
            valid = false;

            ex.printStackTrace();
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (Throwable ex)
            {
            }
        }

        if (valid)
            reparsePortals();

        return valid;
    }

    /*
     * We have two Portal instances per real Portal. Need to create one
     * instance.
     * 
     * To find the Portal, seach for the first letter (or any letter) and then
     * use the Coordinate of its location to look at the 4 squares around it.
     * Assume there are always 3 spaces around the first letter in the Portal
     * name. Assume there are always 2 spaces around the second letter, then
     * the first letter and a passage which represents the real location of the
     * Portal.
     *
     * Based upon the algortihm, we can then find the full Portal name and the Passage
     * to which it is tied.
     */

    private void reparsePortals ()
    {

    }

    private Vector<Tile> _theMaze;
    private int _minX = Integer.MAX_VALUE;
    private int _maxX = 0;
    private int _minY = Integer.MAX_VALUE;
    private int _maxY = 0;
    private int _width = 0;
    private int _height = 0;
    private boolean _debug;
}