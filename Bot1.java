import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Comparator;
/**
 * Write a description of class ClueBot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bot1 extends BotFunctions implements ClueBot
{
    private int[] cardsInHand;
    private int[] cardsRevealed;
    private int[] knownCards;
    private int[] knownSecretCards;
    private int[] tally;
    private int[][] playersTally;
    private int[][] knownPlayerCards;
    private ArrayList<Integer>[] falseCards;
    private ArrayList<ArrayList<Integer>>[] possibleCards;
    private final int[] SEP = {0,6,6,12,12,21}; // refers to seporation of who,what,where
    private Random rgen;
    private int[] check;
    private int closeness;
    public Bot1(int[]cardsInHand, int[]cardsRevealed, int numOfOtherPlayers)
    {
        this.cardsInHand = cardsInHand;
        this.cardsRevealed = cardsRevealed;
        knownCards = new int[21];
        knownSecretCards = new int[3];
        tally = new int[21];
        playersTally = new int[numOfOtherPlayers][21];
        knownPlayerCards = new int[numOfOtherPlayers][cardsInHand.length];
        falseCards = new ArrayList[3];
        possibleCards = new ArrayList[numOfOtherPlayers];
        check = new int[7];
        rgen = new Random();
        for (int i=0;i<falseCards.length;i++)   {falseCards[i]=new ArrayList<Integer>();}
        for (int i=0;i<possibleCards.length;i++) {possibleCards[i]=new ArrayList<ArrayList<Integer>>();}
        for (int i=0;i<cardsInHand.length;i++)
        {
            if (cardsInHand[i]<SEP[2])  {falseCards[0].add(cardsInHand[i]);}
            else if (cardsInHand[i]<SEP[3])  {falseCards[1].add(cardsInHand[i]);}
            else {falseCards[2].add(cardsInHand[i]);}
        }
        for (int i=0;i<cardsInHand.length;i++)  {knownCards[cardsInHand[i]]=1;}
        if (cardsRevealed!=null)    {for (int i=0;i<cardsRevealed.length;i++){knownCards[cardsRevealed[i]]=1;}}
        for (int i=0;i<tally.length;i++)
        {
            if (knownCards[i]==1) {tally[i]=0;}
            else {tally[i]=1;}
            for (int j=0;j<playersTally.length;j++) {playersTally[j][i]=tally[i];}
        }
        for (int i=0;i<knownSecretCards.length;i++) {knownSecretCards[i]=-1;}
        for (int i=0;i<knownPlayerCards.length;i++)
        {
            for (int j=0;j<knownPlayerCards[i].length;j++) {knownPlayerCards[i][j]=-1;}
        }
        //this.closeness=closeness;
    }
    public void see(int player, int card)
    {
        if (!contains(knownPlayerCards[player],card)&&contains(knownPlayerCards[player],-1))
        {
            int i=0;
            while (knownPlayerCards[player][i]!=-1) {i++;} // find an empty slot
            knownPlayerCards[player][i]=card;  // add the new card
        }
    }
    public int[] ask()
    {
        Check();
        int[] cardsToQuestion = new int[3];
        boolean close = false;
        for (int i=0;i<cardsToQuestion.length*2;i=i+2)
        {
            if ((knownSecretCards[i/2]!=-1||close)&&falseCards[i/2].size()>0)
            {
                cardsToQuestion[i/2] = falseCards[i/2].get(rgen.nextInt(falseCards[i/2].size()));
            }
            else
            {
                if (sum1(Arrays.copyOfRange(tally,SEP[i],SEP[i+1]))==closeness)
                {
                    close = true;
                    int j = SEP[i];
                    while (tally[j]!=1) {j++;}
                    cardsToQuestion[i/2] = j;
                }
                else
                {
                    ArrayList<int[]> unknownCards = new ArrayList<int[]>();
                    for (int j=SEP[i];j<SEP[i+1];j++)
                    {
                        if (tally[j]==1)
                        {
                            int s=0;
                            for (int[]x:playersTally) {s+=x[j];}
                            int[] info = {j,s};
                            unknownCards.add(info);
                        }
                    }
                    unknownCards.sort(Comparator.comparing(x -> x[1]));
                    cardsToQuestion[i/2] = unknownCards.get(0)[0];
                }
            }
        }
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
    public void listen(int turn, int playerInQuestion, int[]cardsInQuestion, boolean cardShown)
    {
        if (cardShown)
        {            
            ArrayList<Integer> newCards = new ArrayList<Integer>();
            for (int x:cardsInQuestion)
            {
                newCards.add(x);
            }
            possibleCards[playerInQuestion].add(newCards);
        }
        else
        {
            for (int i=0;i<cardsInQuestion.length;i++)
            {
                playersTally[playerInQuestion][cardsInQuestion[i]]=0;
            }
        }
        Check();
    }
    public int[] win()
    {
        Check();
        if (!contains(knownSecretCards,-1))    
        {
            //System.out.println(Arrays.toString(check));
            return knownSecretCards;
        }
        else    {return null;}
    }
    public void printTally()
    {
        System.out.println("Tally: "+Arrays.toString(tally));
        for (int i=0;i<playersTally.length;i++)
        {
            System.out.println("playerTally "+i+" :"+Arrays.toString(playersTally[i]));
            System.out.println(Arrays.toString(knownPlayerCards[i]));
        }
    }
    private void Check()    // preforms all of the different checks
    {
        while (check1()||check2()||check3()||check4()||check5()||check6()||check7()){}
    }
    private void checkCount(int i)
    {
        check[i]++;
    }
    //   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /   /  
    /**
     * This method checks the tally: 
     * If: the bot has isolated a single card in a section (who,what,where)
     * Then: makes that card a known secret card
     */
    private boolean check1()
    {
        boolean somethingHappened = false;
        for (int i=0;i<knownSecretCards.length*2;i=i+2)
        {
            if (knownSecretCards[i/2]==-1&&sum1(Arrays.copyOfRange(tally,SEP[i],SEP[i+1]))==1)
            {
                somethingHappened = true;
                //System.out.println("Check 1 worked");
                checkCount(0);
                int j=SEP[i];
                while (tally[j]!=1) {j++;}
                knownSecretCards[i/2]=j;
                falseCards[i/2].add(knownSecretCards[i/2]);
            }
        }
        return somethingHappened;
    }
    /**
     * This checks the known player cards:
     * If: a card is located in a players hand
     * Then: all slots of that card are set to 0
     */
    private boolean check2()
    {
        boolean somethingHappened = false;
        for (int i=0;i<knownPlayerCards.length;i++)
        {
            for (int j=0;j<knownPlayerCards[i].length;j++)
            {
                if (knownPlayerCards[i][j]!=-1)
                {
                    if (tally[knownPlayerCards[i][j]]!=0)
                    {
                        somethingHappened=true;
                        //System.out.println("Check 2 worked");
                        checkCount(1);
                        tally[knownPlayerCards[i][j]]=0;
                    }
                    for (int k=0;k<playersTally.length;k++)
                    {
                        if (k!=i&&playersTally[k][knownPlayerCards[i][j]]!=0)
                        {
                            somethingHappened=true;
                            //System.out.println("Check 2 worked");
                            checkCount(1);
                            playersTally[k][knownPlayerCards[i][j]]=0;
                        }
                    }
                }
            }
        }
        return somethingHappened;
    }
    /**
     * This checks each player's tally
     * If: A given player's tally is narrowed down to the exact number in each player's hand
     * Then: It can be assumed that what remains is in fact the exact hand of that player
     */
    private boolean check3()
    {
        boolean somethingHappened = false;
        for (int i=0;i<playersTally.length;i++)
        {
            if (contains(knownPlayerCards[i],-1)&&sum1(playersTally[i])==cardsInHand.length)
            {
                somethingHappened = true;
                //System.out.println("Check 3 worked");
                checkCount(2);
                for (int j=0,k=0;k<knownPlayerCards[i].length;k++,j++)
                {    
                    while (playersTally[i][j]!=1) {j++;}
                    knownPlayerCards[i][k]=j;
                }
            }
        }
        return somethingHappened;
    }
    /**
     * This checks all the places any one card could be
     * If: It can only be in one place
     * Then: It is confirmed that the card is in that place
     */
    private boolean check4()
    {
        boolean somethingHappened = false;
        for (int i=0;i<tally.length;i++)
        {
            boolean unknown = true;
            if (contains(knownSecretCards,i)||knownCards[i]==1)    {unknown=false;}
            for (int j=0;j<knownPlayerCards.length;j++) {if (contains(knownPlayerCards[j],i))  {unknown=false;}}
            if (unknown)
            {
                int s=0;
                s+=tally[i];
                for (int j=0;j<playersTally.length;j++) {s+=playersTally[j][i];}
                if (s==1)
                {
                    somethingHappened = true;
                    //System.out.println("Check 4 worked");
                    checkCount(3);
                    if (tally[i]==1)
                    {
                        if (i<SEP[2])    {knownSecretCards[0]=i;falseCards[0].add(i);}
                        else if (i<SEP[3])  {knownSecretCards[1]=i;falseCards[1].add(i);}
                        else    {knownSecretCards[2]=i;falseCards[2].add(i);}
                    }
                    else
                    {
                        int j=0,k=0;
                        while (playersTally[j][i]!=1) {j++;}
                        while (knownPlayerCards[j][k]!=-1){k++;}
                        knownPlayerCards[j][k]=i;
                    }
                }
            }
        }
        return somethingHappened;
    }
    /**
     * This checks all of the possible cards that each player could have
     * If: The number of possible cards is narrowed down to one
     * Then: That card is set as a known card for that player
     */
    private boolean check5()
    {
        boolean somethingHappened = false;
        for (int i=0;i<possibleCards.length;i++)
        {
            for (int j=possibleCards[i].size()-1;j>=0;j--)
            {
                for (int k=possibleCards[i].get(j).size()-1;k>=0;k--)
                {
                    if (playersTally[i][possibleCards[i].get(j).get(k)]==0)
                    {
                        possibleCards[i].get(j).remove(k);
                    }
                }
                if (possibleCards[i].get(j).size()==1)
                {
                    if (contains(knownPlayerCards[i],-1)&&!contains(knownPlayerCards[i],possibleCards[i].get(j).get(0)))
                    {
                        somethingHappened=true;
                        //System.out.println("Check 5 worked");
                        checkCount(4);
                        int k=0;
                        while (knownPlayerCards[i][k]!=-1) {k++;}
                        knownPlayerCards[i][k]=possibleCards[i].get(j).get(0);
                    }
                    possibleCards[i].remove(j);
                }
            }
        }
        return somethingHappened;
    }
    private boolean check6()
    {
        boolean somethingHappened = false;
        for (int i=0;i<knownSecretCards.length*2;i=i+2)
        {
            if (knownSecretCards[i/2]!=-1)
            {
                for (int j=SEP[i];j<SEP[i+1];j++)
                {
                    if (j!=knownSecretCards[i/2]&&tally[j]!=0)
                    {
                        somethingHappened = true;
                        //System.out.println("check6 worked");
                        checkCount(5);
                        tally[j]=0;
                    }
                }
            }
        }
        return somethingHappened;
    }
    private boolean check7()
    {
        boolean somethingHappened = false;
        for (int i=0;i<knownPlayerCards.length;i++)
        {
            for (int j=0;j<knownPlayerCards[i].length;j++)
            {
                if (knownPlayerCards[i][j]!=-1)
                {
                    for (int k=0;k<playersTally.length;k++)
                    {
                        if (k!=i&&playersTally[k][knownPlayerCards[i][j]]!=0)
                        {
                            somethingHappened=true;
                            //System.out.println("check7 worked");
                            checkCount(6);
                            playersTally[k][knownPlayerCards[i][j]]=0;
                        }
                    }
                }
            }
        }
        return somethingHappened;
    }
}