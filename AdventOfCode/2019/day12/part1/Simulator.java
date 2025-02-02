public class Simulator
{
    public static final String DATA_FILE = "data.txt";
    public static final int ITERATIONS = 1000;
    public static final void main (String[] args)
    {
        boolean debug = false;
        boolean verify = false;

        for (int i = 0; i < args.length; i++)
        {
            if ("-help".equals(args[i]))
            {
                System.out.println("Usage: [-debug] [-verify]");
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

            if (theVerifier.verify())
                System.out.println("Verified ok!");
            else
                System.out.println("Verify failed!");

            System.exit(0);
        }

        MoonSystem theMoons = new MoonSystem(DATA_FILE, debug);

        for (int i = 0; i < ITERATIONS; i++)
        {
            theMoons.applyGravity();
        }

        System.out.println("Total energy after "+ITERATIONS+" iteratiosn: "+theMoons.totalEnergy());
    }
}