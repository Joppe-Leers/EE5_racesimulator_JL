package gui.mainInt;

import javax.swing.*;
import java.awt.*;

public class MainInterface extends JFrame {

    private JPanel mainPanel;
    private JLabel radioIndicator;
    private JLabel drsIndicator;
    private JLabel regenIndicator;
    private JPanel speedPanel;
    private JPanel socPanel;
    private JLabel lapLabel1;
    private JLabel lapLabel2;
    private JLabel lapLabel3;
    private JPanel mapPanel; //panel must use a layout manager that doesn't ignore preferred, max and min dimension
    private JLabel currentLapLable;
    private JLabel carIconLabel;
    private JLabel tirePressureLF;
    private JLabel tirePressureRF;
    private JLabel tirePressureLB;
    private JLabel tirePressureRB;
    private AnalogIndicator speedIndicator;
    private AnalogIndicator socIndicator;
    private MapIndicator map;


    public MainInterface(){
        super();

        init();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //set the size to the maximum of the screen it is displayed on
        this.setUndecorated(true); //disable the bar on top
    }
    private void init() {
        radioIndicator.setOpaque(true);
        drsIndicator.setOpaque(true);
        regenIndicator.setOpaque(true);
        setIndicatorRadio(false); //dummy data
        setIndicatorDRS(false); //dummy data
        setIndicatorRegen(false); //dummy data

        //init speed indicator
        this.speedIndicator = new AnalogIndicator(0,400,40,4, 250);
        this.speedIndicator.setValue("0"); //dummy data
        this.speedIndicator.setUnit("km/h");
        this.speedPanel.add(this.speedIndicator);


        //init soc indicator
        this.socIndicator = new AnalogIndicator(0,100,20,2,180);
        this.socIndicator.setValue("0"); //dummy data
        this.socIndicator.setUnit("%");
        this.socPanel.add(this.socIndicator);

        //init lap labels
        currentLapLable.setText("0:00");//dummy data
        lapLabel1.setText("0:00");      //dummy data
        lapLabel2.setText("0:00");      //dummy data
        lapLabel3.setText("0:00");      //dummy data

        //init tire pressure
        setTyrePressure(0,0,0,0); //dummy data
    }

    public void setTyrePressure(int RFP,int LFP,int RBP, int LBP) {
        tirePressureLF.setText(Integer.toString(LFP));
        tirePressureLB.setText(Integer.toString(RFP));
        tirePressureRF.setText(Integer.toString(LBP));
        tirePressureRB.setText(Integer.toString(RBP));
    }
    public void setIndicatorDRS (boolean status) {
        if (status) {
            drsIndicator.setBackground(Color.GREEN);
            drsIndicator.setText("on");
        }
        else {
            drsIndicator.setBackground(Color.RED);
            drsIndicator.setText("off");
        }
    }
    public void setLapLabel(String[] labTimes) {
        currentLapLable.setText(labTimes[0]);
        lapLabel1.setText(labTimes[1]);
        lapLabel2.setText(labTimes[2]);
        lapLabel3.setText(labTimes[3]);
    }
    public void setIndicatorRegen (boolean status) {
        if (status) {
            regenIndicator.setBackground(Color.GREEN);
            regenIndicator.setText("on");
        }
        else {
            regenIndicator.setBackground(Color.RED);
            regenIndicator.setText("off");
        }
    }
    public void setIndicatorRadio (boolean status) {
        if (status) {
            radioIndicator.setBackground(Color.GREEN);
            radioIndicator.setText("on");
        }
        else {
            radioIndicator.setBackground(Color.RED);
            radioIndicator.setText("off");
        }
    }
    public void setSpeedIndicator(float speed) {
        speedIndicator.setValue(String.format("%.0f",speed));
    }
    public void setSocIndicator(float fuel) {
        socIndicator.setValue(String.format("%.0f",fuel));
    }

    public void setTrack(int trackID) {
        this.mapPanel.removeAll();
        map = new MapIndicator(trackID, new Dimension(150,220), 15); //140
        this.mapPanel.add(map);
        repaint();
    }
}
