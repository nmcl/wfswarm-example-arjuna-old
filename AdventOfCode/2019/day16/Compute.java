public class Compute
{
    public static final int[] BASE_PATTERN = {0, 1, 0, -1};

    public Compute (boolean debug)
    {
        _debug = debug;
    }

    public int[] process (int[] input, int numberOfPhases)
    {
        int[] results = input;

        for (int i = 0; i < numberOfPhases; i++)
        {
            results = processPhase(input);

            //phaseInput = results;
        }

        return results;
    }

    /*
     * A reminder of the example ...
     * 
     * 10-1010-10
     * 01100-1-10
     * 00111000
     * 00011110
     * 00001111
     * 00000111
     * 00000011
     * 00000001
     * 
     * 10-1010-1010-1010-1  --> miss first digit [010-1010-1010-1]
     * 01100-1-1001100-1-1  --> miss first digit [001100-1-1]
     * 00111000-1-1-1000111000-1-1-1  --> miss first digit [000111000-1-1-1]
     * 00011110000-1-1-1-1000011110000-1-1-1-1  --> miss first digit [000011110000-1-1-1-1]
     * 00001111100000-1-1-1-1-1  --> miss first digit [000001111100000-1-1-1-1-1]
     */

    private int[] processPhase (int[] input)
    {
        int[] results = new int[input.length];
        int basePatternLowerBounds = 0;

        for (int i = 0; i < input.length; i++)
        {
            int value = 0;
            int basePatternIndex = 1;
            int basePatternLoop = 0;

            for (int j = 0; j < input.length; j++)
            {
                int[] work = new int[input.length];
                int pattern = BASE_PATTERN[basePatternIndex];


                work[j] = input[j] * BASE_PATTERN[basePatternIndex];

                System.out.println(input[j]+"*"+BASE_PATTERN[basePatternIndex]);

                basePatternLoop++;

                if (basePatternLoop == input.length)
                basePatternIndex++;

                if (basePatternIndex == BASE_PATTERN.length)
                    basePatternIndex = basePatternLowerBounds;

                value += work[j];
            }

            results[i] = value;
        }

        return results;
    }

    boolean _debug;
}