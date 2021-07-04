import java.util.*;

/*
 * We need to get all of the keys. We only need to open doors if they prevent
 * us from getting to a key.
 * 
 * - Order the keys and their locations first in the tunnels?
 * - The order of keys is always a to z? Not necessarily.
 * - If we get to a door and we don't have the key to get through it then backtrack
 *   and find the key? Or don't even go there in the first place?
 * - If another key is closer to our current location than going through the
 *   next door then collect the key for shortest path.
 * - Go for all keys in sae direction until we hit a door that
 *   we can't pass then change direction.
 *
 * BFS from current location to next key.
 */

public class Explorer
{
    public Explorer (Map theMap, boolean debug)
    {
        _theMap = theMap;
        _start = _theMap.getEntrance();
        _totalNumnberOfKeys = _theMap.numberOfKeys();
        _states = new ArrayDeque<State>();
        _allStates = new Vector<State>();
        _debug = debug;
    }

   /*
    * Algorithm:
    *
    * - initial starting point @
    * - move to find next key
    * - if we hit a door ...
    *      if we have the key, keep going
    *      if we do not have the key, we can't get through the door so we
    *      need to stop going in that direction and find the key.
    * - each time we find a key, reset the starting point and remember
    *   to reset the visited bit in each Node.
    * - keep going until we hit a door or find all keys
    */

    public int findAllKeys ()
    {
        if (_debug)
            System.out.println("Starting search at "+_start);

        _states.offer(new State(_start));

        while (_states.size() > 0)
        {
            System.out.println("states size "+_states.size());

            State theState = _states.pop();
            
            System.out.println("totalKeys "+_totalNumnberOfKeys);
            System.out.println("State keys "+theState.numberOfKeys());

            if (theState.numberOfKeys() == _totalNumnberOfKeys)
                return theState.numberOfSteps();

            for (Coordinate nextPosition : theState.getPosition().directions())
            {
                System.out.println("nextPosition "+nextPosition);

                if (_theMap.validPosition(nextPosition))
                {
                    System.out.println("valid");

                    char content = _theMap.getContent(nextPosition);

                    System.out.println("content "+content);

                    if (Util.isDoor(content) && !theState.hasKey(content))
                    {
                        if (_debug)
                            System.out.println("Is a door and we don't have the key!");
                    }
                    else
                    {
                        System.out.println("here");

                        State nextState = new State(theState, nextPosition, content);

                        System.out.println("nextState "+nextState);

                        if (!_allStates.contains(nextState))
                        {
                            _allStates.add(nextState);

                            System.out.println("there");

                            _states.add(nextState);

                            System.out.println("Adding state "+nextState);
                        }
                        else
                            System.out.println("already added");
                    }
                }
                else
                    System.out.println(nextPosition+" is invalid");
            }

            System.out.println("State sizes here "+ _states.size());
        }
        
        System.out.println("No route found!!");

        return -1;
    }

    private Map _theMap;
    private Coordinate _start;
    private int _totalNumnberOfKeys;
    private ArrayDeque<State> _states;
    private Vector<State> _allStates;
    private boolean _debug;
}