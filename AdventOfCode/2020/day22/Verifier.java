public class Verifier
{
    public static final String EXAMPLE_FILE = "example.txt";

    public Verifier (boolean debug)
    {
        _debug = debug;
    }

    public boolean verify ()
    {
        Deck[] decks = Util.loadRules(EXAMPLE_FILE, _debug);

        System.out.println(decks[0]);
        System.out.println(decks[1]);
        
        return false;
    }

    private boolean _debug;
}