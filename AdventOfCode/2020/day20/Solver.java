import java.util.*;

public class Solver
{
    public Solver (boolean debug)
    {
        _debug = debug;
    }

    public Vector<Tile> arrangement (Vector<Tile> tiles)
    {
        Vector<Tile> toReturn = new Vector<Tile>();
        int dimension = (int) Math.sqrt((double) tiles.size());
        int x = dimension / 2;
        int y = x;
        Tile[][] thePuzzle = new Tile[dimension][dimension];

        if (_debug)
            System.out.println("Dimensions "+dimension+" by "+dimension);

        System.out.println("Centre "+x+" "+y);
        
        for (int i = 0; i < tiles.size(); i++)
        {
            Tile t = tiles.elementAt(i);

            matchingTiles(t, tiles);
        }

        return toReturn;
    }

    /**
     * Loop through the tiles and find out how many of them have matching edges. Then,
     * based upon the number of shared edges we do the following:
     * 
     * 0: ERROR!!
     * 1: If a tile has only one matching edge, then it's easy to say where it needs
     * to be placed.
     */

    private Vector<Tile> matchingTiles (Tile toCheck, Vector<Tile> tiles)
    {
        Vector<Tile> matches = new Vector<Tile>();

        System.out.println("\nChecking edges for tile "+toCheck);

        for (int i = 0; i < tiles.size(); i++)
        {
            Tile t = tiles.elementAt(i);
            boolean matchedEdge = false;

            System.out.println("Comparing with tile "+t.getID());

            if (!t.equals(toCheck))  // don't compare with ourself!
            {
                for (int j = 0; (j < 4) && !matchedEdge; j++)
                {
                    for (int k = 0; k < 4; k++)
                    {
                        System.out.println("Comparing "+t.getEdges()[j]+" with "+toCheck.getEdges()[k]);

                        if (t.getEdges()[j].equals(toCheck.getEdges()[k]))
                        {
                            matchedEdge = true;
                            break;
                        }
                    }
                }

                if (matchedEdge)
                {
                    matches.add(t);

                    System.out.println("Tile "+t.getID()+" shares common edges with tile "+toCheck.getID());
                }
            }
        }

        System.out.println("\nTile "+toCheck.getID()+" shares edges with "+matches.size()+" other tiles.");

        return matches;
    }

    private boolean _debug;
}