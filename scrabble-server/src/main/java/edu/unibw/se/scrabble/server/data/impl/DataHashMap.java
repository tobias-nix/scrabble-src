package edu.unibw.se.scrabble.server.data.impl;

import edu.unibw.se.scrabble.common.base.Statistics;
import edu.unibw.se.scrabble.server.data.AuthData;
import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.ScrabbleData;

import java.util.HashMap;

public class DataHashMap implements Data, ScrabbleData, AuthData {

    //TODO: HashMap-Implementierung
    private record UserData (String password, Statistics statistics){}
    private final HashMap<String, UserData> usernameToUserData = new HashMap<>();

    public DataHashMap(){
        this.fill();

    }

    private void fill(){
         usernameToUserData.put("paul",new UserData("1234",new Statistics(0,0,0,0)));
    }

    @Override
    public boolean usernameExists(String username) {
        return false;
    }

    @Override
    public String getPassword(String username) {
        return usernameToUserData.get(username).password;
    }

    @Override
    public boolean createUser(String username, String password) {
        return false;
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
        return null;
    }

    @Override
    public boolean saveUserStatistics(String username, Statistics statistics) {
        return false;
    }
}
