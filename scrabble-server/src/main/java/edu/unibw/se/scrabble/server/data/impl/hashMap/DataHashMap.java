package edu.unibw.se.scrabble.server.data.impl.hashMap;

import edu.unibw.se.scrabble.common.base.Statistics;
import edu.unibw.se.scrabble.server.data.AuthData;
import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.ScrabbleData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Bößendörfer
 */
public class DataHashMap implements Data, ScrabbleData, AuthData {

    private record UserData(String password, Statistics statistics) {
    }

    private final HashMap<String, UserData> usernameToUserData = new HashMap<>();

    public DataHashMap() {
        this.fill();
    }

    public void fill() {
        String filePath = Objects.requireNonNull(getClass().getResource("/userdata.csv")).getFile();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine(); //Erste Format LINE
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                usernameToUserData.put(lineAsArray[0], // username
                        new UserData(lineAsArray[1], // password
                                new Statistics(Integer.parseInt(lineAsArray[2]), // gamesPlayed
                                        Integer.parseInt(lineAsArray[3]), // gamesWon
                                        Integer.parseInt(lineAsArray[4]), // highestScore
                                        Integer.parseInt(lineAsArray[5])  //totalScore
                                )));
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    public boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }
        return usernameToUserData.get(username) != null;
    }

    @Override
    public String getPassword(String username) {
        if (username == null || !(usernameToUserData.containsKey(username))) {
            return null;
        }
        return usernameToUserData.get(username).password;
    }

    @Override
    public boolean createUser(String username, String password) {
        if (username == null || (username.length() < 4) || (username.length() > 8) ||
                password == null || (password.length() < 8) || (password.length() > 20)) {
            return false;
        }
        usernameToUserData.put(username, new UserData(password,
                new Statistics(0, 0, 0, 0)));
        return true;
    }

    @Override
    public AuthData getAuthData() {
        return this;
    }

    @Override
    public ScrabbleData getScrabbleData() {
        return this;
    }

    @Override
    public Statistics getUserStatistics(String username) {
        if (username == null || !(usernameToUserData.containsKey(username))) {
            return null;
        }
        return usernameToUserData.get(username).statistics();
    }

    @Override
    public boolean saveUserStatistics(String username, Statistics statistics) {
        if (username == null || !(usernameToUserData.containsKey(username)) || statistics == null) {
            return false;
        }
        UserData userDataUpdate = new UserData(usernameToUserData.get(username).password, statistics);
        usernameToUserData.put(username, userDataUpdate);
        return true;
    }
}
