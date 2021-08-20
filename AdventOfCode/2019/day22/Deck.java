import java.util.*;

public class Deck
{
    public static final int SIZE_OF_DECK = 10007;

    public Deck (boolean debug)
    {
        this(debug, false);
    }

    public Deck (boolean debug, boolean populateDeck)
    {
        _theDeck = new Vector<Integer>(SIZE_OF_DECK);
        _debug = debug;

        if (populateDeck)
            initialise();
    }

    private void initialise ()
    {
        for (int i = 0; i < SIZE_OF_DECK; i++)
            _theDeck.add(i, i);
    }

    private Vector<Integer> _theDeck;
    private boolean _debug;
}