
/**
 * Abstract class BotFunctions - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class BotFunctions 
{
    /**
     * Takes the sum of an array
     */
    public static int sum1(int[]array)
    {
        int s=0;
        for (int i=0;i<array.length;i++)
        {
            s+=array[i];
        }
        return s;
    }
    public static boolean contains(int[]array, int x)
    {
        for (int i=0;i<array.length;i++)
        {
            if (array[i]==x)
            {
                return true;
            }
        }
        return false;
    }
}
