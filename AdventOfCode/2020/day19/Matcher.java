public class Matcher
{
    public Matcher (Rule[] rules, boolean debug)
    {
        _rules = rules;
        _debug = debug;
    }

    public String matchRule (int ruleNumber, Message[] messages)
    {
        Rule ruleToMatch = _rules[ruleNumber];

        if (_debug)
            System.out.println("Trying to match rule: "+ruleToMatch);

        String current = getMatchingString(ruleToMatch, "", true);

        System.out.println("Match using first rules: "+current);

        current = getMatchingString(ruleToMatch, "", false);

        System.out.println("Match using second rules: "+current);

        return "";
    }

    private String getMatchingString (Rule theRule, String current, boolean checkFirst)
    {
        String str = current;

        if (theRule.getMatch() == Rule.NO_MATCH)
        {
            if (checkFirst)
            {
                int[] firstRules = theRule.firstDependantRules();

                for (int i = 0; i < firstRules.length; i++)
                {
                    str = getMatchingString(_rules[firstRules[i]], str, true);

                    if (_debug)
                        System.out.println("First rules string: "+str);

                    str = getMatchingString(_rules[firstRules[i]], str, false);

                    if (_debug)
                        System.out.println("Then adding second rules string: "+str);
                }
            }
            else
            {
                int[] secondRules = theRule.secondDependantRules();

                if (secondRules != null)
                {
                    for (int i = 0; i < secondRules.length; i++)
                    {
                        str = getMatchingString(_rules[secondRules[i]], str, false);

                        if (_debug)
                            System.out.println("Second rules string: "+str);

                        str = getMatchingString(_rules[secondRules[i]], str, true);

                        if (_debug)
                            System.out.println("Then adding first rules string: "+str);
                    }
                }
            }
        }
        else
            str += theRule.getMatch();

        return str;
    }

    private Rule[] _rules;
    private boolean _debug;
}