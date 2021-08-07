/*
 * A Portal is two letters but as we scan in the information about
 * the Donut and Portals we only get to read one letter at a time.
 * 
 * Assume there are always 3 spaces around the first letter in the Portal
 * name. Assume there are always 2 spaces around the second letter, then
 * the first letter and a passage which represents the real location of the
 * Portal.
 */

 /*
  * When printing, leep a reference to each latter of the Portal for each
  * coordinate. In fact the Portal print method should take this coordinate
  * and print our accordingly.
  */

public class Wormhole
{
    public static final String START = "AA";
    public static final String EXIT = "ZZ";

    public Wormhole (Coordinate first, Coordinate second, Tile passage, String name)
    {
        _secondPosition = second;
        _passage = passage;
        _portalName = name;
    }

    public final String getFullName ()
    {
        return _portalName;
    }
/*
    public final char getName (Coordinate coord)
    {
        if (_position.equals(coord))
            return _portalName.charAt(0);
        else
            return _portalName.charAt(1);
    }*/
    
    private Coordinate _secondPosition;
    private Tile _passage;  // the passage we're actually "attached" to.`
    private String _portalName;
}
