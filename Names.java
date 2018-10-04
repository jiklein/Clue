import java.util.ArrayList;
/**
 * Write a description of class Data here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Names
{
    private static final String[] names = {
      "Green",
      "Mustard",
      "Peacock",
      "Plum",
      "Scarlet",
      "White",
      "Wrench",
      "Candlestick",
      "Dagger",
      "Pistol",
      "Lead Pipe",
      "Rope",
      "Bathroom",
      "Study",
      "Dining Room",
      "Game Room",
      "Garage",
      "Bedroom",
      "Living Room",
      "Kitchen",
      "Courtyard"
    };
    public static String[] get()
    {
        return names;
    }
    public static String[] toString(int[]x)
    {
        String[] s = new String[x.length];
        for (int i=0;i<s.length;i++)
        {
            s[i]=names[x[i]];
        }
        return s;
    }
    public static ArrayList getArrayList()
    {
        ArrayList<String> a = new ArrayList<String>();
        for (String s:names)
            a.add(s);
        return a;
    }
}
