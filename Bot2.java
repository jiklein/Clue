import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
/**
 * Write a description of class ClueBot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bot2 extends BotFunctions implements ClueBot
{
    private int[] cardsInHand;
    private int[] cardsRevealed;
    private int[] knownSecretCards;
    private int[] tally;
    private ArrayList<Integer>[] falseCards;
    private final int[] SEP = {0,6,6,12,12,21}; // refers to separation of who,what,where
    private Random rgen;
    private boolean myTurn;
    private int playersQuestioned;
    private int numOfOtherPlayers;
    public Bot2(int[]cardsInHand, int[]cardsRevealed, int numOfOtherPlayers)
    {
        this.cardsInHand = cardsInHand;
        this.cardsRevealed = cardsRevealed;
        knownSecretCards = new int[3];
        tally = new int[21];
        falseCards = new ArrayList[3];
        rgen = new Random();
        this.numOfOtherPlayers = numOfOtherPlayers;
        for (int i=0;i<falseCards.length;i++)   {falseCards[i]=new ArrayList<Integer>();}
        for (int i=0;i<cardsInHand.length;i++)
        {
            if (cardsInHand[i]<SEP[2])  {falseCards[0].add(cardsInHand[i]);}
            else if (cardsInHand[i]<SEP[3])  {falseCards[1].add(cardsInHand[i]);}
            else {falseCards[2].add(cardsInHand[i]);}
        }
        for (int i=0;i<tally.length;i++)    {tally[i]=1;}
        for (int i=0;i<cardsInHand.length;i++)  {tally[cardsInHand[i]]=0;}
        if (cardsRevealed!=null)    {for (int i=0;i<cardsRevealed.length;i++){tally[cardsRevealed[i]]=0;}}
        for (int i=0;i<knownSecretCards.length;i++) {knownSecretCards[i]=-1;}
        myTurn = false;
    }
    public void see(int player, int card)
    {
        tally[card]=0;
        myTurn = false;
    }
    public int[] ask()
    {
        int[] cardsToQuestion = new int[3];
        for (int i=0;i<cardsToQuestion.length*2;i=i+2)
        {
            if (knownSecretCards[i/2]>=0)
            {
                cardsToQuestion[i/2] = falseCards[i/2].get(rgen.nextInt(falseCards[i/2].size()));
            }
            else
            {
                ArrayList<Integer> unknownCards = new ArrayList<Integer>();
                for (int j=SEP[i];j<SEP[i+1];j++){if (tally[j]==1){unknownCards.add(j);}}
                cardsToQuestion[i/2] = unknownCards.get(rgen.nextInt(unknownCards.size()));
            }
        }
        myTurn = true;
        playersQuestioned = 0;
        return cardsToQuestion;
    }
    public int show(int[]cardsInQuestion)
    {
        for (int i=0;i<cardsInQuestion.length;i++)
        {
            for (int j=0;j<cardsInHand.length;j++)
            {
                if (cardsInQuestion[i]==cardsInHand[j])
                {
                    return cardsInQuestion[i];
                }
            }
        }
        return -1;
    }
    /**
     * This should get called each time another player gets questioned
     */
    public void listen(int turn,int playerInQuestion, int[]cardsInQuestion, boolean cardShown)
    {
        int knownCard = -1;
        if (myTurn)
        {
            playersQuestioned++;
            if (playersQuestioned==numOfOtherPlayers)
            {
                myTurn = false;
                for (int i=0;i<cardsInQuestion.length;i++)
                {
                    if (!(cardsInQuestion[i]==knownSecretCards[i]||contains(cardsInHand,cardsInQuestion[i])))
                    {
                        knownSecretCards[i]=cardsInQuestion[i];
                    }
                }
            }
        }
    }
    public int[] win()
    {
        if (!contains(knownSecretCards,-1))    {return knownSecretCards;}
        else    {return null;}
    }
}