package edu.unibw.se.scrabble.server.data.impl.spring;

import edu.unibw.se.scrabble.common.base.Statistics;
import edu.unibw.se.scrabble.server.data.AuthData;
import edu.unibw.se.scrabble.server.data.ScrabbleData;
import edu.unibw.se.scrabble.server.data.impl.spring.jpa.User;
import edu.unibw.se.scrabble.server.data.impl.spring.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Bößendörfer
 */

@Component
public class SpringScrabbleDataWorker implements ScrabbleData, AuthData {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean usernameExists(String username) {
        try {
            return userRepository.existsById(username);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    @Override
    public String getPassword(String username) {
        try {
            User user = userRepository.findUserByUsername(username);
            return user.getPassword();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    @Override
    public boolean createUser(String username, String password) {
        try {
            User user = new User(username, password, 0, 0, 0, 0);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    @Override
    public Statistics getUserStatistics(String username) {
        try {
            User user = userRepository.findUserByUsername(username);
            return user.getStatistics();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    @Override
    public boolean saveUserStatistics(String username, Statistics statistics) {
        try {
            User user = userRepository.findUserByUsername(username);
            user.setStatistics(statistics);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    public boolean clear() {
        try{
            userRepository.deleteAll();
            return true;
        }catch(Exception e){
            e.printStackTrace(System.err);
            return false;
        }
    }

    public boolean fill() {
        String filePath = getClass().getResource("/userdata.csv").getFile();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            bufferedReader.readLine(); //Erste Formatzeile
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                try {
                    User user = new User(lineAsArray[0], lineAsArray[1],
                            Integer.parseInt(lineAsArray[2]), Integer.parseInt(lineAsArray[3]),
                            Integer.parseInt(lineAsArray[4]), Integer.parseInt(lineAsArray[5]));
                    userRepository.save(user);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.err);
            return false;
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return false;
    }

    public boolean deleteUser(String username){
        try {
            User user = userRepository.findUserByUsername(username);
            userRepository.delete(user);
            return true;
        } catch(Exception e){
            e.printStackTrace(System.err);
            return false;
        }
    }
}
