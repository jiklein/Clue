import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;
/**
 * Write a description of class Tally here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tally extends JFrame
{
    private final String[] SEC = {"Who?","What?","Where?"};
    public Tally()
    {
        setBounds(1100,100,500,800);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel title = new JLabel("Tally");
        title.setBounds(205,5,110,45);
        title.setFont(new Font(null,1,40));
        JLabel[] cards = new JLabel[24];
        JButton[] tallies = new JButton[21];
        JButton[] clear = new JButton[3];
        for (int i=0;i<SEC.length;i++)
        {
            cards[i*7] = new JLabel(SEC[i],SwingConstants.RIGHT);
            cards[i*7].setForeground(Color.red);
        }
        for (int i=0;i<Names.get().length;i++)
        {
            cards[i+1+i/6-i/18] = new JLabel(Names.get()[i],SwingConstants.RIGHT);
        }
        for (int i=0;i<cards.length;i++)
        {
            cards[i].setBounds(60,45+i*30,165,30);
            cards[i].setFont(new Font(null,1,25));
        }
        for (int i=0;i<tallies.length;i++)
        {
            tallies[i] = new JButton("");
            tallies[i].setBounds(240,45+(i+1+i/6-i/18)*30,100,30);
            tallies[i].setFont(new Font(null,1,30));
            tallies[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    int index = 0;
                    for (int i=0;i<tallies.length;i++)
                        if (e.getSource().equals(tallies[i]))
                            index = i;
                    if (tallies[index].getText().equals("X"))
                    {
                        if (index<6)
                        {
                            for (int i=0;i<6;i++)
                                if (i!=index)
                                    tallies[i].setText("X");
                        }
                        else if (index<12)
                        {
                            for (int i=6;i<12;i++)
                                if (i!=index)
                                    tallies[i].setText("X");
                        }
                        else
                        {
                            for (int i=12;i<21;i++)
                                if (i!=index)
                                    tallies[i].setText("X");
                        }
                        tallies[index].setText("O");
                    }
                    else if (tallies[index].getText().equals("O"))
                    {
                        tallies[index].setText("");
                    }
                    else
                    {
                        tallies[index].setText("X");
                    }
                }
            });
        }
        for (int i=0;i<clear.length;i++)
        {
            clear[i] = new JButton("clear");
            clear[i].setFont(new Font(null,1,13));
            clear[i].setBounds(250,55+210*i,70,15);
            clear[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    int index = 0;
                    for (int i=0;i<clear.length;i++)
                        if (e.getSource().equals(clear[i]))
                            index = i;
                    if (index==0)
                        for (int i=0;i<6;i++)
                            tallies[i].setText("");
                    else if (index==1)
                        for (int i=6;i<12;i++)
                            tallies[i].setText("");
                    else
                        for (int i=12;i<21;i++)
                            tallies[i].setText("");
                }
            });
        }
        for (JLabel lab:cards)
            panel.add(lab);
        for (JButton but:tallies)
            panel.add(but);
        for (JButton but:clear)
            panel.add(but);
        panel.add(title);
        add(panel);
        validate();
    }
}