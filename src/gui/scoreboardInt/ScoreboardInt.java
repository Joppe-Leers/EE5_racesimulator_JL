package gui.scoreboardInt;

import javax.swing.*;

public class ScoreboardInt extends JFrame {
    private JPanel mainPanel;
    private ScoreboardComponent board;

    public ScoreboardInt () {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //set the size to the maximum of the screen it is displayed on
        this.setUndecorated(true); //disable the bar on top

        //init scoreboard
        board = new ScoreboardComponent(600,400);
        this.mainPanel.add(board);
    }

    public void setTopThree(String[] topThree) {
        board.setTopThree(topThree);
    }
}
