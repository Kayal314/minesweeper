import javax.swing.*;
import java.awt.*;

public class Board {
    JPanel panel;
    static final int SIZE=45;
    static final int ROWS=15;
    JFrame frame;
    JLabel gameStatus;
    JLabel scoreboard;
    int score;
    int total;
    Cell[][] minefield;
    Board()
    {
        frame=new JFrame("Stinky Ball's Minesweeper");
        frame.setSize(SIZE*ROWS+60,SIZE*ROWS+200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        panel=new JPanel();
        panel.setLayout(null);
        minefield=new Cell[ROWS][ROWS];
        frame.add(panel);
        total=0;
        initializeField(0.2); //set 0 < difficulty < 0.4 (recommended maximum difficulty)
        initializeStates();
        total+=ROWS*ROWS;
        JLabel title=new JLabel("MINESWEEPER",SwingConstants.CENTER);
        title.setFont(new Font("Algerian",Font.BOLD,25));
        title.setBounds(130,-5,SIZE*ROWS-200,100);
        panel.add(title);
        gameStatus=new JLabel("CLICK ON ANY CELL TO UNCOVER IT", SwingConstants.CENTER);
        gameStatus.setFont(new Font("Serif", Font.PLAIN, 20));
        gameStatus.setBounds(90,35,SIZE*ROWS-120, 100);
        panel.add(gameStatus);
        scoreboard=new JLabel();
        scoreboard.setBounds(30,15,100,50);
        scoreboard.setFont(new Font("Serif",Font.PLAIN,14));
        panel.add(scoreboard);
        score=0;
        frame.setVisible(true);
    }
    private void initializeField(double difficulty)
    {
        for(int i=0;i<ROWS;i++)
            for(int j=0;j<ROWS;j++)
            {
                JButton button=new JButton();
                button.setBounds(23+j*SIZE,127+i*SIZE,SIZE,SIZE);
                button.setBackground(Color.WHITE);
                panel.add(button);
                minefield[i][j]=new Cell(button,Math.random()<difficulty);
                if(!minefield[i][j].hasMine && j<ROWS-1 && j>0 && i<ROWS-1 && i>0)
                    minefield[i][j].canUseDynamite = Math.random() < 0.15;
                if(minefield[i][j].hasMine)
                    total-=1;
                int finalJ = j;
                int finalI = i;
                button.setBackground(new Color(158, 189, 217));
                button.addActionListener(e -> reveal(finalI,finalJ,false));
            }

    }

    private void reveal( int finalI, int finalJ, boolean usedDynamite)
    {
        if(!minefield[finalI][finalJ].hidden)
            return;
        minefield[finalI][finalJ].hidden=false;
        JButton button=minefield[finalI][finalJ].button;
        button.setBackground(new Color(219, 156, 177));
        if(minefield[finalI][finalJ].hasMine) {
            button.setText("M");
            button.setBackground(new Color(237, 92, 92));
            if(!usedDynamite) {
                gameStatus.setText("OOPS, GAME OVER! TRY AGAIN");
                JOptionPane.showMessageDialog(null,"Oops! Game Over! Try Again");
                frame.dispose();
                new Board();
            }
        }
        else {
            score++;
            if(minefield[finalI][finalJ].canUseDynamite)
            {
                button.setText("D");
                reveal(finalI-1,finalJ-1,true);
                reveal(finalI-1,finalJ,true);
                reveal(finalI-1,finalJ+1,true);
                reveal(finalI,finalJ-1,true);
                reveal(finalI,finalJ+1,true);
                reveal(finalI+1,finalJ-1,true);
                reveal(finalI+1,finalJ,true);
                reveal(finalI+1,finalJ+1,true);
                gameStatus.setText("BOOOOM! YOU JUST USED A DYNAMITE");
            }
            else {
                button.setText(String.valueOf(minefield[finalI][finalJ].state));
                gameStatus.setText("PHEW! PLAY NEXT MOVE");
            }
        }
        scoreboard.setText("Score: "+score);
        if(score==total) {
            gameStatus.setText("CONGRATULATIONS! YOU'VE WON!");
            JOptionPane.showMessageDialog(null,"You Won! Play Again");
            new Board();
        }
    }
    private int getIntState(int i, int j)
    {
        if(i>=0&&j>=0&&i<ROWS&&j<ROWS)
            return minefield[i][j].hasMine?1:0;
        return 0;
    }
    private void initializeStates()
    {
        int i,j;
        for(i=0;i<ROWS;i++)
            for(j=0;j<ROWS;j++)
                if(!minefield[i][j].hasMine)
                minefield[i][j].state=getIntState(i-1,j-1)+
                        getIntState(i-1,j)+
                        getIntState(i-1,j+1)+
                        getIntState(i,j-1)+
                        getIntState(i,j+1)+
                        getIntState(i+1,j-1)+
                        getIntState(i+1,j)+
                        getIntState(i+1,j+1);

    }

    public static void main(String[] args) {
        new Board();
    }
}
