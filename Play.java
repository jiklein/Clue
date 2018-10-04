import javax.swing.JOptionPane;
/**
 * Write a description of class PlayClue here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Play
{
    private static final String P1 = "How many players would you like to play against?";
    private static final String[] O1 = {"1","2","3","4","5"};
    public static Object lock;
    public static void main(String[]args)
    {
        lock = new Object();
        int winner = Game.play(0,1+input(P1,O1),1);
        if (winner<0)
        {
            System.out.println("You loose!!!!");
        }
    }
    private static int input(String prompt, String[]options)
    {
        String response = (String)JOptionPane.showInputDialog(null,prompt,"Input:",
                       JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
        if (response==null)
            System.exit(0);
        int index=0;
        for (int i=0;i<options.length;i++)
        {
            if (response.equals(options[i]))
                index=i;
        }
        return index;
    }
}