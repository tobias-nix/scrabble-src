package edu.unibw.se.scrabble.server.data;

/**
 * Interface to provide methods for user authentication.
 *
 * @author Bößendörfer
 */
public interface AuthData {

     /*
    Soll benutzt werden, um zu überprüfen, ob Username in der Datenbank vorhanden ist.
     */
    /**
     * Returns, if the given username exits
     *
     * @param username the user's username
     * @return true, if  username exits, otherwise false
     */
    boolean usernameExists(String username);

     /*
    Gibt passwort zurück
     */
    /**
     * Returns the saved password of a user.
     *
     * @param username the user's username
     * @return null, if username does not exist, otherwise the saved password
     */
    String getPassword(String username);

    /*
    Neuen User anlegen, Statistiken mit 0 initialisieren nicht vergessen
     */
    /**
     * Creates a user with the given username and password
     *
     * @param username the user's username
     * @param password the user's password
     * @return true, if user is successfully created otherwise false,
     * e.g. if the username or the password is empty or null or the length of the username is less than 4 or longer
     * than 8 characters or the username is already in database or the password does not meet the requirements
     */
    boolean createUser(String username, String password);
}
