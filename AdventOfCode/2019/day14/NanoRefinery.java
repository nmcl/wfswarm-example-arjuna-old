import java.util.*;
import java.io.*;

public class NanoRefinery
{
    public NanoRefinery (Vector<Reaction> reactions, boolean debug)
    {
        _debug = debug;
        _reactions = reactions;
        _storage = new Vector<ChemicalQuantity>();  // where we will store excess ChemicalQuantities

        if (_debug)
        {
            Enumeration<Reaction> iter = reactions.elements();

            while (iter.hasMoreElements())
                System.out.println(iter.nextElement());
        }
    }

    /*
     * 10 ORE => 10 A
     * 1 ORE => 1 B
     * 7 A, 1 B => 1 C
     * 7 A, 1 C => 1 D
     * 7 A, 1 D => 1 E
     * 7 A, 1 E => 1 FUEL
     */

    public final int oreNeeded ()
    {
        int amountOfOre = 0;
        Reaction fuel = findReaction(Chemical.FUEL);

        /*
         * Look at the reactions needed to create the amount
         * of fuel. Work backwards from there.
         */

        if (fuel != null)
        {
            if (_debug)
                System.out.println("\nFuel equation: "+fuel);

            int fuelNeeded = fuel.chemicalCreated().getAmount();
            Vector<ChemicalQuantity> fuelChemicalQuantities = fuel.getChemicalQuantities();    // maybe not all reactions loaded are needed
            Enumeration<ChemicalQuantity> iter = fuelChemicalQuantities.elements();

            while (iter.hasMoreElements())
            {
                ChemicalQuantity reaction = iter.nextElement();
                System.out.println("**Working on: "+reaction);
                
                amountOfOre += createNeededAmount(reaction);
            }
        }
        else
            System.out.println("Error! No fuel required?!");
        
        return amountOfOre;
    }

    /*
     * Go through each ChemicalQuantity and try to create the
     * required amount, storing excess in the inventory.
     * So check the inventory first, of course.
     */

    private int createNeededAmount (ChemicalQuantity theReaction)
    {
        int amountOfOre = 0;
        Reaction r = findReaction(theReaction.getChemical().getName());  // the reaction for the chemical needed
        int needed = theReaction.getAmount();  // the amount of chemical needed
        int numberOfTimesReactionNeedsToRun = theReaction.getAmount() / r.chemicalCreated().getAmount();

        if ((theReaction.getAmount() % r.chemicalCreated().getAmount()) != 0)
            numberOfTimesReactionNeedsToRun++;

        if (_debug)
        {
            System.out.println("\nWorking on reaction "+theReaction.getChemical());
            System.out.println("Needed reaction: "+r);
            System.out.println("Quantity of "+theReaction.getChemical()+" needed: "+needed);
            System.out.println("Quantity which would be created from reaction: "+r.chemicalCreated().getAmount());
        }

        if (_debug)
                System.out.println("**Reaction needs to run "+numberOfTimesReactionNeedsToRun+" times.");

        for (int i = 0; i < numberOfTimesReactionNeedsToRun; i++)
        {
            if (r.isOre())
            {
                /*
                 * If the chemical needed is ORE then we don't need
                 * to do anything special as it's always available at
                 * whatever quantity.
                 */

                if (_debug)
                    System.out.println("Reaction uses ORE");

                int amountCreated = 0;
                
                do
                {
                    System.out.println("**amount created so far: "+amountCreated);

                    int storage = checkInventory(theReaction);

                    System.out.println("**amount from  inventory: "+storage);

                    if (storage >= theReaction.getAmount())
                    {
                        consumeFromInventory(theReaction);

                        amountCreated += theReaction.getAmount();
                    }
                    else
                    {
                        amountOfOre += r.getChemicalQuantities().elementAt(0).getAmount();
                        
                        amountCreated += r.chemicalCreated().getAmount();

                        if (amountCreated > needed)
                        {
                            if (_debug)
                                System.out.println("Created "+(amountCreated - needed)+" more "+r.chemicalCreated()+" than needed");

                            storeExcessChemical(theReaction.getChemical(), amountCreated - needed);

                            amountCreated = needed;
                        }
                        else
                            System.out.println("**Not enough created.");
                    }

                    System.out.println("**after all that: "+amountCreated);
                    System.out.println("**and ore used: "+amountOfOre);
                    
                } while (amountCreated != needed);
            }
            else
            {
                /*
                 * Not ORE so we need to create the chemical using
                 * the reaction. Check the inventory first and update
                 * if we have excess.
                 */

                if (_debug)
                    System.out.println("Reaction "+r+" does NOT use ORE.");

                int amountStored = checkInventory(theReaction);

                if (amountStored >= theReaction.getAmount())
                {
                    System.out.println("**Can get "+theReaction+" from storage");

                    /*
                     * There's enough of the chemical in storage so just
                     * use that and we're done!
                     */

                    consumeFromInventory(theReaction);
                }
                else
                {
                    System.out.println("**Not enough "+theReaction+" in storage");

                    Vector<ChemicalQuantity> chemicalQuantities = r.getChemicalQuantities();
                    Enumeration<ChemicalQuantity> iter = chemicalQuantities.elements();

                    while (iter.hasMoreElements())
                    {
                        ChemicalQuantity reaction = iter.nextElement();
                        System.out.println("**Working on: "+reaction);
                        
                        amountOfOre += createNeededAmount(reaction);
                    }
                }
            }

            System.out.println("\nFINISHED WITH "+theReaction.getChemical());
        }

        System.out.println("**Ore used for reaction: "+amountOfOre);
        
        return amountOfOre;
    }

    private void storeExcessChemical (Chemical chem, int amount)
    {
        if (_debug)
            System.out.println("Storing excess "+chem+" in the inventory.");

        ChemicalQuantity toStore = new ChemicalQuantity(chem, amount);
        int index = _storage.indexOf(toStore);

        if (index != -1)
        {
            ChemicalQuantity chemQ = _storage.elementAt(index);
            int currentQuantityInInventory = chemQ.getAmount();

            chemQ.setAmount(currentQuantityInInventory + amount);

            if (_debug)
                System.out.println("Inventory now storing: "+chemQ);
        }
        else
        {
            if (_debug)
                System.out.println("Adding to storage: "+toStore);

            _storage.add(toStore);
        }
    }

    /*
     * Check to see if the chemical is present in the inventory with at least the amount
     * needed.
     */

    private int checkInventory (ChemicalQuantity needed)
    {
        int amountPresent = 0;
        int index = _storage.indexOf(needed);

        if (index != -1)
            return _storage.elementAt(index).getAmount();

        return amountPresent;
    }

    /*
     * Consume the chemical needed and the amount from the inventory.
     * We've already determined presence of the chemical and the amount
     * but double check!
     */

    private boolean consumeFromInventory (ChemicalQuantity needed)
    {
        if (_debug)
            System.out.println("Consuming chemical from storage.");

        boolean quantityPresent = false;
        int index = _storage.indexOf(needed);

        if (index != -1)
        {
            ChemicalQuantity stored = _storage.elementAt(index);
            int amountStored = stored.getAmount();

            if (amountStored >= needed.getAmount())
            {
                stored.setAmount(amountStored - needed.getAmount());

                quantityPresent = true;
            }
            else
                System.out.println("ERROR - Chemical suddenly no longer present at the required amount in the inventory!");
        }
        else
            System.out.println("ERROR - Chemical suddenly no longer present in the inventory!");

        return quantityPresent;
    }

    private Reaction findReaction (String name)
    {
        Enumeration<Reaction> iter = _reactions.elements();

        while (iter.hasMoreElements())
        {
            Reaction r = iter.nextElement();

            if (r.chemicalCreated().getChemical().getName().equals(name))
                return r;
        }

        return null;
    }

    private boolean _debug;
    private Vector<Reaction> _reactions;
    private Vector<ChemicalQuantity> _storage;
}
