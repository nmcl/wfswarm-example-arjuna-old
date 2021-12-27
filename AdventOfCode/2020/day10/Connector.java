import java.util.*;

public class Connector
{
    public static final int SEAT_JOLTAGE = 0;
    public static final int JOLTAGE_RANGE = 3;

    public Connector (boolean debug)
    {
        _debug = debug;
    }

    public Vector<JoltageAdapter> connect (Vector<JoltageAdapter> adapters)
    {
        Vector<JoltageAdapter> toReturn = new Vector<JoltageAdapter>();
        int joltage = 0;

        while (adapters.size() > 0)
        {
            boolean found = false;
            JoltageAdapter adapter = null;

            if (_debug)
                System.out.println("Seaching for adapter joltage: "+joltage);
            
            for (int i = 0; (i < adapters.size()) && !found; i++)
            {
                int current = adapters.elementAt(i).outputJoltage();

                if (_debug)
                    System.out.println("Checking adapter: "+current);

                if (current == joltage)
                {
                    adapter = adapters.elementAt(i);
                    found = true;
                }

                if ((current > joltage) && (current <= joltage + JOLTAGE_RANGE))
                {
                    if (_debug)
                        System.out.println("Adapter "+current+" within range. Have "+adapter);

                    if (adapter == null)
                        adapter = adapters.elementAt(i);
                    else
                    {
                        if (current <= adapter.outputJoltage())
                            adapter = adapters.elementAt(i);
                    }

                    if (_debug)
                        System.out.println("Adapter: "+adapter);
                }
            }

            joltage = adapter.outputJoltage();

            toReturn.add(adapter);
            adapters.remove(adapter);
        }

        return toReturn;
    }

    private boolean _debug;
}