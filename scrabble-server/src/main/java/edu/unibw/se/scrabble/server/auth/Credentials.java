package edu.unibw.se.scrabble.server.auth;

import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnLoginUser;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnRegisterUser;

/**
 * Interface to provide methods for user login and register requests.
 *
 * @author Nerb
 */
public interface Credentials {
    /*
    Muss überprüfen, ob username existiert. Falls ja, dann ob passwort zum username passt.
     */
    /**
     * Authenticates the users login credentials by checking with userdata in the database.
     * <p>
     * Never returns {@code null}.
     *
     *
     * @param username the user's username
     * @param password the user's password
     * @return {@link ReturnLoginUser} enum type, depending on error type or success.
     */
    ReturnLoginUser loginUser(String username, String password);

    /*
    Muss überprüfen, ob username schon in Datenbank existiert, falls nein, dann neuen user anlegen.
     */
    /**
     * Creates new user dataset in database if username is not already taken.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @param password the user's password
     * @return {@link ReturnRegisterUser} enum type, depending on error type or success.
     */
    ReturnRegisterUser registerUser(String username, String password);
}
