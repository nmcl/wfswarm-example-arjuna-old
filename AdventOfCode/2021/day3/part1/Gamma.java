import java.util.*;

public class Gamma
{
    public static final String getGamma (Vector<String> data)
    {
        String theGamma = "";
        int index = 0;
        int numberOfBits = data.elementAt(0).length();

        for (int i = 0; i < numberOfBits; i++)
        {
            int ones = 0;
            int zeros = 0;

            for (int j = 0; j < data.size(); j++)
            {
                if (data.elementAt(j).charAt(index) == '1')
                    ones++;
                else
                    zeros++;
            }

            if (ones > zeros)
                theGamma += "1";
            else
                theGamma += "0";

            index++;
        }

        return theGamma;
    }
}