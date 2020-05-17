package gui.mainInt;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MapIndicator extends JComponent {
    private static int i=0;
    private ArrayList<int[]> trackCoords = new ArrayList<>();
    private int trackId;
    private Dimension dimension;
    private int[] position;

    public MapIndicator(int trackId, Dimension dimension, double scale){
        super();
        this.trackId = trackId;
        this.dimension = dimension;

        position = new int[]{0,0};
        //this.setPreferredSize(new Dimension(140,300));
        this.setPreferredSize(dimension);
        this.setBackground(new Color(0,0,0,0));

        try {
            readTrackFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int scale = 15;
        if (trackId == 6 || trackId == 11) scale = (int)(scale * 1.33);
        g.translate((int)dimension.getWidth()/2, (int)dimension.getHeight()/2);
        g.setColor(Color.RED);

        for(int[] coord: trackCoords) {
            g.fillOval((int) (coord[1]/scale),(int)(coord[0]/scale), (int)(45/scale),(int)(45/scale) );
        }
    }

    public void readTrackFile() throws IOException {
        InputStream is = getClass().getResourceAsStream("tracks/" + trackId + ".track");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        int i = 0;
        while ((line = br.readLine()) != null)
        {
            if(i>1) {
                String[] values = line.split(",");

                int[] coord = new int[2];
                coord[0] = (int)Float.parseFloat(values[1]); //coord[0] = zPos
                coord[1] = (int)Float.parseFloat(values[2]); //coord[1] = xPos
                trackCoords.add(coord);
            }
            i++;
        }
        br.close();
        isr.close();
        is.close();
    }
}
