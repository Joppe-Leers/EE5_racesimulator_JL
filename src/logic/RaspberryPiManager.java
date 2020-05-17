package logic;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import gui.mainInt.MainInterface;
import gui.scoreboardInt.ScoreboardInt;

import javax.swing.*;

public class RaspberryPiManager {

    private MainInterface mainInt;
    private ScoreboardInt scoreInt;
    private DatabaseManager db;

    // create input pins
    final GpioController gpiocontroller = GpioFactory.getInstance();
    final GpioPinDigitalInput radioPB = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_00, PinPullResistance.PULL_DOWN);
    final GpioPinDigitalInput changIntPB = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
    final GpioPinDigitalInput setNamePB = gpiocontroller.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);

    // create output pins
    final GpioPinDigitalOutput tyreFR = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_01);
    final GpioPinDigitalOutput tyreFL = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_04);
    final GpioPinDigitalOutput tyreBR = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_05);
    final GpioPinDigitalOutput tyreBL = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_06);
    final GpioPinDigitalOutput wingFR = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_12);
    final GpioPinDigitalOutput wingFL = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_13);
    final GpioPinDigitalOutput rearWing = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_14);
    final GpioPinDigitalOutput enging = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_21);
    final GpioPinDigitalOutput exhaust = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_22);
    final GpioPinDigitalOutput gearBox = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_23);

    final GpioPinDigitalOutput redLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_11);
    final GpioPinDigitalOutput greenLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_10);

    final GpioPinDigitalOutput brakeLED = gpiocontroller.provisionDigitalOutputPin(RaspiPin.GPIO_15);

    GpioPinDigitalInput[] inputPins;

    private String playerName = "unknown";;
    private Boolean radioStatus = false;;

    public RaspberryPiManager(MainInterface mainInt, ScoreboardInt scoreInt, DatabaseManager db) {
        this.inputPins = new GpioPinDigitalInput[]{changIntPB, radioPB, setNamePB};
        this.mainInt = mainInt;
        this.scoreInt = scoreInt;
        this.db = db;

        GpioPinListenerDigital listener  = new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                if(event.getPin().getName().equals("GPIO 0") && event.getState() == PinState.HIGH) {
                    //radio button pressed
                    radioPBpressed();
                }
                else if(event.getPin().getName().equals("GPIO 2") && event.getState() == PinState.HIGH) {
                    //interface button pressed
                    changIntPBpressed();
                }
                else if(event.getPin().getName().equals("GPIO 3") && event.getState() == PinState.HIGH) {
                    //setName button pressed
                    setNamePBpressed();
                }
            }
        };
        gpiocontroller.addListener(listener, inputPins);
    }
    public void setBrakeLED (boolean state) {
        brakeLED.setState(state);
    }
    public void setNamePBpressed() {
        boolean skipInsert = false;
        String playerName = JOptionPane.showInputDialog("enter your name");
        while(db.playerAlreadyExist(playerName)) {
             // there already is a record in the database for this name -> ask if it is him/her
            int optionPicked = JOptionPane.showConfirmDialog(null,"Is this te first time you logged in with this name?","Answer honestly", JOptionPane.YES_NO_OPTION);
            if(optionPicked == JOptionPane.YES_OPTION) {
                playerName = JOptionPane.showInputDialog("sorry this name is already picked, enter a new name.");
            }
            else {
                JOptionPane.showMessageDialog(null, "welcome back namevar and good luck".replace("namevar", playerName));
                skipInsert = true;
                break;
            }
        }
        if(!skipInsert) db.insertPlayer(playerName);
        this.playerName = playerName;
    }
    public void changIntPBpressed() {
        if (mainInt.isVisible()) {
            mainInt.setVisible(false);
            scoreInt.setVisible(true);
        }
        else {
            mainInt.setVisible(true);
            scoreInt.setVisible(false);
        }
    }
    public void radioPBpressed() {
        radioStatus = !radioStatus;
        mainInt.setIndicatorRadio(radioStatus);
    }
    public String getPlayerName() {
        return playerName;
    }
    public void setHighVoltage(boolean state) {
        if (state) {
            greenLED.low();
            redLED.high();
        }
        else {
            greenLED.high();
            redLED.low();
        }
    }
    public void setTyreFR (boolean state) {
        tyreFR.setState(state);
    }
    public void setTyreFL (boolean state) {
        tyreFL.setState(state);
    }
    public void setTyreBR (boolean state) {
        tyreBR.setState(state);
    }
    public void setTyreBL (boolean state) {
        tyreBL.setState(state);
    }
    public void setWingFR (boolean state) {
        wingFR.setState(state);
    }
    public void setWingFL (boolean state) {
        wingFL.setState(state);
    }
    public void setRearWing (boolean state) {
        rearWing.setState(state);
    }
    public void setEnging (boolean state) {
        enging.setState(state);
    }
    public void setExhaust (boolean state) {
        exhaust.setState(state);
    }
    public void setGearBox (boolean state) {
        gearBox.setState(state);
    }
}
