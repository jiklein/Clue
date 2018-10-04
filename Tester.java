import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
/**
 * Write a description of class Game here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tester
{
    static final int TIMES = 1000;
    static final int BOT1 = 1;
    static final int BOT2 = 5;
    public static void main(String[] args)
    {
        int winner = -1;
        int[] wins = new int[BOT1+BOT2];
        int[] b1wins = new int[50];
        //System.out.println("S");
        for (int j=0;j<50;j++)
       {
        for (int i=0;i<TIMES;i++)
        {
            wins[Game.play(BOT1,BOT2,0)]++;
        }
        System.out.println(Arrays.toString(wins));
        b1wins[j]=wins[5];
        for (int i=0;i<wins.length;i++)
        {wins[i]=0;}
    }
    
    
        int s = 0;
        for (int x:b1wins)
        {
            s+=x;
        }
        System.out.println(s/50);
    
    }
    
}
