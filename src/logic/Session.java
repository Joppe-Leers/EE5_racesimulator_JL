package logic;

import data.*;
import data.elements.Era;
import data.elements.Weather;
import data.elements.WheelData;
import gui.mainInt.MainInterface;
import gui.scoreboardInt.ScoreboardInt;

public class Session {
    private MainInterface mainInt;
    private ScoreboardInt scoreInt;
    private DatabaseManager db;
    private RaspberryPiManager rp;

    private int lapNumber = 1;
    private String[] lapTimes = new String[]{"0:00", "0:00", "0:00","0:00"};;
    private int trackID = 0;;
    private int damageTreshold = 2;
    private Era era;
    private Weather weather;
    private int penalty;
    private float regenHarvested;
    private float regenDeployed;


    public Session(MainInterface mainInt, ScoreboardInt scoreInt, DatabaseManager db, RaspberryPiManager rp){
        this.mainInt = mainInt;
        this.scoreInt = scoreInt;
        this.db = db;
        this.rp = rp;
        this.mainInt.setTrack(trackID);
    }

    // ***************** handel packet methods ***************
    public void handleMotionData(PacketMotionData packet){
        //int z = (int)packet.getCarMotionDataList().get(packet.getHeader().getPlayerCarIndex()).getWorldVelocityZ();
        //int x = (int)packet.getCarMotionDataList().get(packet.getHeader().getPlayerCarIndex()).getWorldPositionX();
        //int y = (int)packet.getCarMotionDataList().get(packet.getHeader().getPlayerCarIndex()).getWorldPositionY();
        //mainInt.setMapPosition(z,x);
    }
    public void handleSessionData(PacketSessionData packet){
        int trackID = packet.getTrackId();
        if (this.trackID != trackID) {
            this.trackID = trackID;
            mainInt.setTrack(trackID);
        }

        if (scoreInt.isVisible()) {
            scoreInt.setTopThree(db.getTop3FromTrack(trackID+1)); // database ID starts from 1 and not from 0
        }

        era = packet.getEra();
        weather = packet.getWeather();
    }
    public void handleLapData(PacketLapData packet){
        int carIndex = packet.getHeader().getPlayerCarIndex();
        int currentLap = packet.getLapDataList().get(carIndex).getCurrentLapNum();
        float currentLapTime = packet.getLapDataList().get(carIndex).getCurrentLapTime();
        float lastLapTime = packet.getLapDataList().get(carIndex).getLastLapTime();
        penalty = packet.getLapDataList().get(carIndex).getPenalties();

        setLaptimes(currentLap, currentLapTime, lastLapTime);
    }
    public void handleCarSetupData(Packet packet){

    }
    public void handleCarStatusData(PacketCarStatusData packet){
        int carIndex = packet.getHeader().getPlayerCarIndex();
        float fuelCapacity = packet.getCarStatuses().get(carIndex).getFuelCapacity();
        float fuelInTank= packet.getCarStatuses().get(carIndex).getFuelInTank();
        mainInt.setSocIndicator((fuelInTank/fuelCapacity)*100);

        regenHarvested = packet.getCarStatuses().get(carIndex).getErsHarvestedThisLapMGUH()
                        + packet.getCarStatuses().get(carIndex).getErsHarvestedThisLapMGUK();
        regenDeployed = packet.getCarStatuses().get(carIndex).getErsDeployedThisLap();

        // data for error notification LEDs
        WheelData<Integer> tyresDamage = packet.getCarStatuses().get(carIndex).getTiresDamage();
        int frontLeftWingDamage = packet.getCarStatuses().get(carIndex).getFrontLeftWingDamage();
        int frontRightWingDamage = packet.getCarStatuses().get(carIndex).getFrontRightWingDamage();
        int rearWingDamage = packet.getCarStatuses().get(carIndex).getRearWingDamage();
        int engineDamage = packet.getCarStatuses().get(carIndex).getEngineDamage();
        int exhaustDamage = packet.getCarStatuses().get(carIndex).getExhaustDamage();
        int gearBoxDamage = packet.getCarStatuses().get(carIndex).getGearBoxDamage();

        if (tyresDamage.getRearRight() > damageTreshold) rp.setTyreBR(true);
        else rp.setTyreBR(false);
        if (tyresDamage.getRearLeft() > damageTreshold) rp.setTyreBL(true);
        else rp.setTyreBL(false);
        if (tyresDamage.getFrontRight() > damageTreshold) rp.setTyreFR(true);
        else rp.setTyreFR(false);
        if (tyresDamage.getFrontLeft() > damageTreshold) rp.setTyreFL(true);
        else rp.setTyreFL(false);
        if ( frontLeftWingDamage > damageTreshold) rp.setWingFL(true);
        else rp.setWingFL(false);
        if ( frontRightWingDamage > damageTreshold) rp.setWingFR(true);
        else rp.setWingFR(false);
        if (rearWingDamage > damageTreshold) rp.setRearWing(true);
        else rp.setRearWing(false);
        if (engineDamage > damageTreshold) rp.setEnging(true);
        else rp.setEnging(false);
        if (exhaustDamage > damageTreshold) rp.setExhaust(true);
        else rp.setExhaust(false);
        if ( gearBoxDamage> damageTreshold) rp.setGearBox(true);
        else rp.setGearBox(false);
    }
    public void handleCarTelemetryData(PacketCarTelemetryData packet){
        int carIndex = packet.getHeader().getPlayerCarIndex();
        float speed=packet.getCarTelemetryData().get(carIndex).getSpeed();
        mainInt.setSpeedIndicator(speed);
        rp.setHighVoltage(speed > 0);

        float brake=packet.getCarTelemetryData().get(carIndex).getBrake();
        mainInt.setIndicatorRegen(brake!=0 && speed >= 30);
        rp.setBrakeLED(brake > 0);

        boolean drs=packet.getCarTelemetryData().get(carIndex).isDrs();
        mainInt.setIndicatorDRS(drs);

        WheelData<Float> tire = packet.getCarTelemetryData().get(carIndex).getTirePressure();
        int fl = Math.round(tire.getFrontLeft());
        int fr = Math.round(tire.getFrontRight());
        int bl = Math.round(tire.getRearLeft());
        int br = Math.round(tire.getRearRight());
        mainInt.setTyrePressure(fr,fl,br,bl);
    }
    public void handleEventData(PacketEventData packet){

    }
    public void handleParticipantsData(Packet packet){

    }

    // ***************** extra methods *******************
    private void setLaptimes(int currentLap, float currentLapTime, float lastLapTime) {
        if(currentLap != lapNumber && lastLapTime != 0.0) {
            // add record to database
            int min = (int)Math.floor(lastLapTime/60);
            double sec = lastLapTime - 60*min;
            if (min < 10) {
                // database start from 1 and not from 0
                db.insertLapData(rp.getPlayerName(), trackID + 1, "00:0"+ min + ":" + String.format("%.2f",sec), weather.toString(), era.toString(), Integer.toString(penalty),Float.toString(regenHarvested),Float.toString(regenDeployed));
            }
            else {
                // database start from 1 and not from 0
                db.insertLapData(rp.getPlayerName(), trackID + 1, "00:"+ min + ":" + String.format("%.2f",sec), weather.toString(), era.toString(), Integer.toString(penalty),Float.toString(regenHarvested),Float.toString(regenDeployed));
            }

            lapNumber = currentLap;
            for(int i = 3; i>0;i--) {
                lapTimes[i] = lapTimes[i-1];
            }
        }
        int minuts = (int)Math.floor(currentLapTime/60);
        int seconds = (int)Math.floor(currentLapTime - 60*minuts);
        if (seconds < 10) {
            lapTimes[0] = minuts + ":0" + seconds;
        }
        else {
            lapTimes[0] = minuts + ":" + seconds;
        }
        mainInt.setLapLabel(lapTimes);
    }
}

