package gui.scoreboardInt;

import javax.swing.*;
import java.awt.*;

public class ScoreboardComponent extends JComponent {
    private int width;
    private int height;
    private String[] topThree;

    public ScoreboardComponent(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        this.topThree = new String[]{"playerName,time","playerName,time","playerName,time"};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int fontBig = 80;
        int fontSmal = 18;
        g.setColor(this.getBackground());
        g.fillRect(0, 0, (int) width, (int) height);

        g.setColor(new Color(44,172,61));
        g.fillRect(0, (2*height/ 3),  width / 3,  height/ 3); //block 2
        g.fillRect((int) width / 3, height / 2,width / 3, height / 2); //block 1
        g.fillRect( (2*width/ 3), (7*height/ 9), width / 3,  (2*height/ 9)); //block 3

        g.setColor(new Color(42,108,51));
        g.drawRect(0, (2*height/ 3),  width / 3,  height/ 3);
        g.drawRect((int) width / 3, height / 2,width / 3, height / 2);
        g.drawRect( (2*width/ 3), (7*height/ 9), width / 3,  (2*height/ 9));

        //draw numbers on boxes
        g.setColor(new Color(224,239,88));
        g.setFont(new Font(Font.SERIF, Font.PLAIN, fontBig));
        g.drawString("2",2*width/15,7*height/8);
        g.drawString("1",7*width/15,3*height/4);
        g.drawString("3",4*width/5,19*height/20);

        //draw information
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SERIF, Font.PLAIN, fontSmal));
        //System.out.println(topThree[0] + "\t" + topThree[1] + "\t" + topThree[2]);
        String[] splitString;
        if(topThree[1] != null) {
            splitString = topThree[1].split(",");
            g.drawString(splitString[0],width/20,11*height/20);
            g.drawString(splitString[1],width/20,11*height/20+fontSmal);
        }

        if(topThree[0] != null) {
            splitString = topThree[0].split(",");
            g.drawString(splitString[0],23*width/60,3*height/8);
            g.drawString(splitString[1],23*width/60,3*height/8+fontSmal);
        }

        if(topThree[2] != null) {
            splitString = topThree[2].split(",");
            g.drawString(splitString[0],43*width/60,13*height/20);
            g.drawString(splitString[1],43*width/60,13*height/20+fontSmal);
        }

    }
    public void setTopThree(String[] topThree) {
        this.topThree = topThree;
        repaint();
    }
}
