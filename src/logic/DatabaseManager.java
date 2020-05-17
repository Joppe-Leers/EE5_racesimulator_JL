package logic;

import java.sql.*;

public class DatabaseManager {

    private Connection conn;

    public DatabaseManager () {
        this.conn = getConnection();
    }

    public Connection getConnection() {
        try {
            // access database from raspberryPiUser
            String URL = "jdbc:mysql://10.0.0.1:3306/sys?serverTimezone=UTC"; // use this line when connecting from raspberry pi
            //String URL = "jdbc:mysql://localhost:3306/sys?serverTimezone=UTC"; // use this line when connecting from laptop
            String username = "raspberryPi";
            String password = "raspberryPi";

            Connection conn = DriverManager.getConnection(URL, username, password);
            System.out.println("connection to database made");

            return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertLapData(String playerName, int trackId, String laptime,String weather,String era,String penalty, String regenHarvested , String regenDeployed) { //laptime has to be in the correct format hh:mm:ss.ssssss
        try {
            // add record to race table
            Statement stmt = conn.createStatement();
            String templateQuery = "INSERT INTO sys.race(nameID, trackID) VALUES ('nameVar', 'trackVar');";
            String query = templateQuery.replace("nameVar",Integer.toString(getPlayerID(playerName))).replace("trackVar",Integer.toString(trackId));
            stmt.execute(query);
            System.out.println("inserted race in database");

            try {
                // add records to sensorValues table
                stmt = conn.createStatement();
                templateQuery = "INSERT INTO sys.sensorvalues(sensorID, raceID, sensorValue) VALUES ( '1','raceVar','lapTimeVar'),( '2','raceVar',current_timestamp()),( '3','raceVar','weatherVar'),( '4','raceVar','eraVar'),( '5','raceVar','penaltiesVar'),( '6','raceVar','regenHarvestedVar'),( '7','raceVar','regenDeployedVar');";
                query = templateQuery.replaceAll("raceVar",Integer.toString(getHighestRaceID()))
                        .replace("lapTimeVar", laptime)
                        .replace("weatherVar", weather)
                        .replace("eraVar", era)
                        .replace("penaltiesVar", penalty)
                        .replace("regenHarvestedVar", regenHarvested)
                        .replace("regenDeployedVar", regenDeployed);
                stmt.execute(query);    // execute for insert query, executeQuery for select query!
                System.out.println("inserted sensorValues in database");

            } catch (SQLException e) {
                System.out.println("insertLapData: could not insert sensorValue");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("could not insert race");
            e.printStackTrace();
        }
    }
    private Integer getHighestRaceID() {
        Integer id = null;
        try {
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(ID) AS ID FROM sys.race;";
            ResultSet rs = stmt.executeQuery(query);  // execute for insert query, executeQuery for select query!
            //read result set
            rs.next();
            id = rs.getInt("ID");
        } catch (SQLException e) {
            System.out.println("getHighestRaceID: could not read race id");
            e.printStackTrace();
        }
        return id;
    }
    private Integer getPlayerID(String playerName) {
        Integer id = null;
        try {
            Statement stmt = conn.createStatement();
            String templateQuery = "SELECT ID FROM sys.person WHERE fullName = 'nameVar'";
            String query = templateQuery.replace("nameVar",playerName);
            ResultSet rs = stmt.executeQuery(query);    // execute for insert query, executeQuery for select query!
            //read result set
            rs.next();
            id = rs.getInt("ID");
        } catch (SQLException e) {
            System.out.println("getPlayerID: could not read player ID");
            e.printStackTrace();
        }
        return id;
    }
    public boolean playerAlreadyExist(String playerName) {
        boolean result = false;
        try {
            Statement stmt = conn.createStatement();
            String templateQuery = "SELECT IF((select ID FROM sys.person where fullName = 'nameVar') IS NULL , 0, 1) AS result";
            String query = templateQuery.replace("nameVar",playerName);
            ResultSet rs = stmt.executeQuery(query); // execute for insert query, executeQuery for select query!

            //read result set
            rs.next();
            result = rs.getBoolean("result");
        } catch (SQLException e) {
            System.out.println("playerAlreadyExists: could not read player names");
            e.printStackTrace();
        }
        return result;
    }
    public void insertPlayer(String playerName) {
        try {
            Statement stmt = conn.createStatement();
            String templateQuery = "INSERT INTO sys.person(fullName) VALUES ('nameVar');";
            String query = templateQuery.replace("nameVar",playerName);
            stmt.execute(query);    // execute for insert query, executeQuery for select query!
            System.out.println("inserted playerName in database");
        } catch (SQLException e) {
            System.out.println("insertPlayer: could not insert playerName");
            e.printStackTrace();
        }
    }
    public String[] getTop3FromTrack(int trackId) {
        String[] topThree = new String[3];
        try {
            Statement stmt = conn.createStatement();
            String templateQuery = "CALL getTopThreeOfTrack(trackVar);";
            String query = templateQuery.replace("trackVar",Integer.toString(trackId));

            ResultSet rs = stmt.executeQuery(query); // execute for insert query, executeQuery for select query!
            // read result set
            int i = 0;
            while(rs.next()){
                String playerName = rs.getString("fullName");
                String lapTime = rs.getString("sensorvalue");

                topThree[i] = playerName + "," + "time " + lapTime;
                i++;
            }
            return topThree;

        } catch (SQLException e) {
            System.out.println("getTop3FromTrack: could not read top three");
            e.printStackTrace();
        }
        return null;
    }
}