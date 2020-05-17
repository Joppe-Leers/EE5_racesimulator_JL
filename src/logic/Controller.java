package logic;

import data.*;
import gui.mainInt.MainInterface;
import gui.scoreboardInt.ScoreboardInt;
import logic.receiver.Receiver;

import java.io.IOException;

public class Controller {
    private Receiver receiver;
    private Session session;
    private MainInterface mainInt;
    private ScoreboardInt scoreInt;
    private DatabaseManager db;
    private RaspberryPiManager rp;

    public Controller() throws IOException {
        db = new DatabaseManager();
        System.out.println("database manager created");
        receiver=new Receiver(this);
        mainInt = new MainInterface();
        scoreInt = new ScoreboardInt();
        rp= new RaspberryPiManager(mainInt, scoreInt, db);
        System.out.println("raspberry pi manager created");
        session=new Session(mainInt, scoreInt, db,rp);
        mainInt.setVisible(true);
        scoreInt.setVisible(false);

        receiver.receivePacket();
    }

    public void newPacket(Packet packet){
        if(packet!=null){
            switch (packet.getHeader().getPacketId()){

                case 0:
                    session.handleMotionData((PacketMotionData) packet);
                    break;
                case 1:
                    session.handleSessionData((PacketSessionData) packet);
                    break;
                case 2:
                    session.handleLapData((PacketLapData) packet);
                    break;
                case 3:
                    session.handleEventData((PacketEventData) packet);
                    break;
                case 4:
                    session.handleParticipantsData((PacketParticipantsData) packet);
                    break;
                case 5:
                    session.handleCarSetupData((PacketCarSetupData) packet);
                    break;
                case 6:
                    session.handleCarTelemetryData((PacketCarTelemetryData) packet);
                    break;
                case 7:
                    session.handleCarStatusData((PacketCarStatusData) packet);
                    break;

            }

        }
    }

    public static void main(String[] args) throws IOException {
        Controller controller=new Controller();
    }

}
