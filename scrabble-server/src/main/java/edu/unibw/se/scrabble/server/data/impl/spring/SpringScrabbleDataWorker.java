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


    //TODO: Implementieren der Methoden

    @Override
    public boolean usernameExists(String username) {
        try {
            return userRepository.existsById(username);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getPassword(String username) {
        try {
            User user = userRepository.findUserByUsername(username);
            //User ist id und kann nicht null sein?
            return user.getPassword();
        } catch (Exception e) {
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
            return false;
        }
    }

    @Override
    public Statistics getUserStatistics(String username) {
        try {
            User user = userRepository.findUserByUsername(username);
            //User ist id und kann nicht null sein?
            //System.out.println("get: " + user.toString());
            return user.getStatistics();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean saveUserStatistics(String username, Statistics statistics) {
        try {
            User user = userRepository.findUserByUsername(username);
            //User ist id und kann nicht null sein?
            //System.out.println("before set: " + user.toString());
            user.setStatistics(statistics);
            //System.out.println("after set: " + user.toString());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clear() {
        userRepository.deleteAll();
    }

    public void fill() {
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
                } catch (Exception e) {
                    throw new UnsupportedOperationException();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }
}
