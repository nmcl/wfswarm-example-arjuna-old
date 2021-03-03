import java.util.*;

public class RepairDroid
{
    public static final String INITIAL_INPUT = Integer.toString(DroidMovement.NORTH);

    public RepairDroid (Vector<String> instructions, boolean debug)
    {
        _debug = debug;
        _theComputer = new Intcode(instructions, INITIAL_INPUT, _debug);
        _location = new Coordinate(0, 0);  // starting location
        _theMap = new Maze();

        _theMap.addContent(_location, TileId.TRAVERSE);
    }

    public final int moveToOxygenStation ()
    {
        int numberOfSteps = 0;

        // create a map first!

        return numberOfSteps;
    }

    public void printGrid ()
    {

    }

    private boolean recursiveSearch (Coordinate from)
    {
        while (!_theComputer.hasHalted())
        {
            Coordinate to = new Coordinate(from.getX(), from.getY()+1);

            if (!tryToMove(String.valueOf(DroidMovement.NORTH), from, to))
            {
                to = new Coordinate(from.getX(), from.getY()-1);

                if (!tryToMove(String.valueOf(DroidMovement.SOUTH), from, to))
                {
                    to = new Coordinate(from.getX()+1, from.getY());

                    if (!tryToMove(String.valueOf(DroidMovement.EAST), from, to))
                    {
                        to = new Coordinate(from.getX()-1, from.getY());

                        return tryToMove(String.valueOf(DroidMovement.WEST), from, to);
                    }
                }
            }
        }

        return false;
    }

    private boolean tryToMove (String direction, Coordinate from, Coordinate to)
    {
        _theComputer.setInput(direction);

        _theComputer.singleStepExecution();

        if (_theComputer.hasOutput())
        {
            int response = Integer.parseInt(_theComputer.getOutput());

            switch (response)
            {
                case DroidStatus.ARRIVED:
                {
                    _theMap.addContent(to, TileId.OXYGEN_STATION);
                    
                    return true;
                }
                case DroidStatus.COLLISION:
                {
                    _theMap.addContent(from, TileId.WALL);  // didn't move as we hit a wall

                    return false;
                }
                case DroidStatus.MOVED:
                {
                    _theMap.addContent(to, TileId.TRAVERSE);

                    return recursiveSearch(to);
                }
                default:
                    System.out.println("Unknown response: "+response);
            }
        }
        else
            System.out.println("Error - no output after move instruction!");

        return false;
    }

    private boolean _debug;
    private Intcode _theComputer;
    private Coordinate _location;
    private Maze _theMap;
}