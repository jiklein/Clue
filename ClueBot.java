
/**
 * Write a description of interface ClueBot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface ClueBot 
{
    void see(int player, int card);
    int[] ask();
    int show(int[]cardsInQuestion);
    void listen(int turn,int playerInQuestion, int[]cardsInQuestion, boolean cardShown);
    int[] win();
}
