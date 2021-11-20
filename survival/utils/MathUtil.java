package mc.server.survival.utils;

import java.util.Random;

public class MathUtil
{
    public static boolean chanceOf(int chance)
    {
        Random r = new Random();
        int number = r.nextInt(100);

        return number <= chance;
    }

    public static boolean isInteger(String arg)
    {
        try
        {
            Integer.parseInt(arg);
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        return true;
    }
}