import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
/**
 * Write a description of class Game here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game
{
    public static int play(int numBot1, int numBot2, int numPlayer)
    {
        ClueBot[] bot = new ClueBot[numBot1+numBot2+numPlayer];
        int[][] hand = new int[bot.length][18/bot.length];
        ArrayList<Integer> cards = new ArrayList<Integer>();
        Random rgen = new Random();
        int random = -1;
        for (int i=0;i<21;i++)  {cards.add(i);}
        int[] secretCards = new int[3];
        for (int i=0;i<secretCards.length;i++)
        {
            random=i*5+rgen.nextInt(6+(i/2*3));
            secretCards[i]=cards.get(random);
            cards.remove(random);
        }
        for (int i=0;i<hand[0].length;i++)
        {
            for (int j=0;j<hand.length;j++)
            {
                random=rgen.nextInt(cards.size());
                hand[j][i]=cards.get(random);
                cards.remove(random);
            }
        }
        int[] cardsRevealed = new int[cards.size()];
        if (cards.size()>0)     {for (int i=0;i<cardsRevealed.length;i++) {cardsRevealed[i]=cards.get(i);}}
        else    {cardsRevealed=null;}
        for (int i=0;i<numBot2;i++) {bot[i] = new Bot2(hand[i],cardsRevealed,bot.length-1);}
        if (numBot1!=0)
        {
            for (int i=numBot2;i<bot.length;i++) {bot[i] = new Bot1(hand[i],cardsRevealed,bot.length-1);}
        }
        else if (numPlayer!=0)
        {
            for (int i=numBot2;i<bot.length;i++) {bot[i] = new Player(hand[i],cardsRevealed,bot.length-1);}
        }
        int turn = -1;
        int[] cardsInQuestion;
        int playerInQuestion;
        boolean cardShown;
        int card;
        int its=0;
        int[] win=null;
        do
        {
            turn++;
            if (turn==bot.length)   {turn=0;}
            cardShown = false;
            cardsInQuestion = bot[turn].ask();
            if (turn==0) {playerInQuestion=bot.length-1;}
            else    {playerInQuestion=turn-1;}
            do
            {
                card = bot[playerInQuestion].show(cardsInQuestion);
                if (card!=-1)
                {
                    cardShown = true;
                    if (playerInQuestion==bot.length-1) {bot[turn].see(turn,card);}
                    else    {bot[turn].see(playerInQuestion,card);}
                }
                for (int i=0;i<bot.length;i++)  
                {
                    if (!(i==playerInQuestion||(i==turn&&cardShown)))
                    {
                        if (playerInQuestion==bot.length-1)     {bot[i].listen(turn,turn,cardsInQuestion,cardShown);}
                        else    {bot[i].listen(turn,playerInQuestion,cardsInQuestion,cardShown);}
                    }
                }
                playerInQuestion--;
                if (playerInQuestion<0)  {playerInQuestion=bot.length-1;}
            }
            while (!cardShown&&playerInQuestion!=turn);
            win=bot[turn].win();
            its++;
        }
        while (win==null);
        //System.out.println("Iterations: "+its/6.0);
        if (Arrays.equals(win,secretCards)) 
        {
            return turn;
        }
        else 
        {
            return -1;
        }
    }
}
