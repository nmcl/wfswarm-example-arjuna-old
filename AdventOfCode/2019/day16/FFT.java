import java.util.*;
import java.io.*;

public class FFT
{
    public static final String DATA_FILE = "data.txt";

    public static void main (String[] args)
    {
        boolean debug = false;
        boolean verify = false;

        for (int i = 0; i < args.length; i++)
        {
            if ("-help".equals(args[i]))
            {
                System.out.println("Usage: [-verify] [-debug] [-help]");
                System.exit(0);
            }

            if ("-debug".equals(args[i]))
                debug = true;

            if ("-verify".equals(args[i]))
                verify = true;
        }

        if (verify)
        {
            Verifier theVerifier = new Verifier(debug);
            boolean verified = theVerifier.verify();

            if (verified)
                System.out.println("Verified ok.");
            else
                System.out.println("Verify failed.");
        }
        else
        {
            /*
             * Open the data file and read it in.
             */

            BufferedReader reader = null;
            String line = null;

            try
            {
                reader = new BufferedReader(new FileReader(DATA_FILE));

                line = reader.readLine();
            }
            catch (Throwable ex)
            {
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

            Compute fft = new Compute(debug);
            int[] input = Util.convert(line);
            int[] data = fft.process(input, 100);

            System.out.print("First 8 digits: ");

            for (int i = 0; i < 8; i++)
            {
                System.out.print(data[i]);
            }

            System.out.println();
        }
    }
}