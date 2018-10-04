import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.event.*;
/**
 * Write a description of class ClueBot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends BotFunctions implements ClueBot
{
    private int[] cardsInHand;
    private final int[] SEP = {0,6,6,12,12,21};
    private final String[] O1 = {"yes","no"};
    private boolean myTurn;
    private int playersQuestioned;
    private int numOfOtherPlayers;
    public Player(int[]cardsInHand, int[]cardsRevealed, int numOfOtherPlayers)
    {
        Tally t = new Tally();
        this.cardsInHand = cardsInHand;
        String message = "<html>Your cards are: "+Arrays.toString(Names.toString(cardsInHand))+"<br>";
        if (cardsRevealed!=null)
            message += "The cards revealed are: "+Arrays.toString(Names.toString(cardsRevealed));
        else
            message += "There are no cards revealed";
        myTurn = false;
        interact(null,message,null);
    }
    public void see(int player, int card)
    {
        interact(null,"Player "+(player+1)+" is showing you: "+Names.get()[card],null);
        myTurn = false;
    }
    public int[] ask()
    {
        int[] cardsToQuestion = new int[3];
        String message = "<html>It is your turn: <br>Which cards would you like to question?";
        for (int i=0;i<cardsToQuestion.length*2;i=i+2)
        {
            cardsToQuestion[i/2]=SEP[i]+interact("",message,Arrays.copyOfRange(Names.get(),SEP[i],SEP[i+1]));
        }
        myTurn = true;
        return cardsToQuestion;
    }
    public int show(int[]cardsInQuestion)
    {
        boolean hasCard = false;
        for (int i=0;i<cardsInQuestion.length;i++)
            for (int j=0;j<cardsInHand.length;j++)
                if (cardsInQuestion[i]==cardsInHand[j])
                    hasCard = true;
        String message = "<html>The cards in question are: "+Arrays.toString(Names.toString(cardsInQuestion));
        message += "<br>Do you have a card to show?";
        int answer=-1;
        do 
        {
            answer = interact("",message,O1);
        }
        while ((!hasCard&&answer!=1)||(hasCard&&answer!=0));
        int card = -1;
        message = "<html>The cards in question are: "+Arrays.toString(Names.toString(cardsInQuestion));
        message += "<br>Which card would you like to show?";
        while (answer==0)
        {
            card = cardsInHand[interact("",message,Names.toString(cardsInHand))];
            if ((hasCard&&contains(cardsInQuestion,card))||!hasCard)
            {
                return card;
            }
        }
        return -1;
    }
    /**
     * This should get called each time another player gets questioned
     */
    public void listen(int turn,int playerInQuestion, int[]cardsInQuestion, boolean cardShown)
    {
        String message = "";
        if (myTurn)
        {
            playersQuestioned++;
            if (playersQuestioned==numOfOtherPlayers)
            {
                myTurn = false;
            }
            message = "You are asking player "+(playerInQuestion+1)+": ";
            if (!cardShown)
            {
                message += "They have nothing to show";
            }
        }
        else
        {
            message = "<html>It is player "+(turn+1)+"'s turn:<br>";
            message += "They are asking player "+(playerInQuestion+1)+": ";
            if (cardShown)
            {
                message += "A card was shown";
            }
            else
            {
                message += "No card was shown";
            }
        }
        interact(null,message,null);
    }
    public int[] win()
    {
        int answer = interact("","Would you like to make a guess at the secret cards?",O1);
        if (answer==0)
        {
            int[] guess = new int[3];
            String message = "<html>It is your turn: <br>What do you think were the secret cards?";
            for (int i=0;i<guess.length*2;i=i+2)
            {
                guess[i/2]=SEP[i]+interact("",message,Arrays.copyOfRange(Names.get(),SEP[i],SEP[i+1]));
            }
            return guess;
        }
        else
        {
            return null;
        }
    }
    private int interact(String title, String message, String[]options)
    {
        JOptionPane pane;
        if (options==null)
        {
            pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE);
            pane.setWantsInput(true);
            pane.setIcon(null);
            pane.setSelectionValues(options);
            pane.setInitialSelectionValue(options[0]);
        }
        JDialog dialog = pane.createDialog(null, title);
        dialog.setModal(false);
        dialog.show();
        dialog.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) 
            {
                synchronized(Play.lock) {
                    Play.lock.notify();
                }
            }
        });
        dialog.addWindowListener(new WindowAdapter(){
           public void windowClosing(WindowEvent e)
           {
               System.exit(0);
           }
        });
        try 
        {
            synchronized(Play.lock) {
                Play.lock.wait();
            }
        }
        catch (InterruptedException e){}
        if (options!=null)
        {
            String response = (String)pane.getInputValue();
            dialog.dispose();
            int index=0;
            for (int i=0;i<options.length;i++)
            {
                if (response.equals(options[i]))
                    index=i;
            }
            return index;
        }
        else
        {
            dialog.dispose();
            return -1;
        }
    }
}