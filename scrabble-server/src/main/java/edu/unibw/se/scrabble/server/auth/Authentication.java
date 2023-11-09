package edu.unibw.se.scrabble.server.auth;

import edu.unibw.se.scrabble.server.data.AuthData;

/**
 * Interface for component Authentication which is used to authenticate user input against database values, for example
 * user login.
 */
public interface Authentication {

    /**
     * Returns an interface for authentication of user login data.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link Credentials}
     */
    Credentials getCredentials();

    /**
     * Add AuthData interface to Authentication so Authentication can make requests to database to perform
     * authentication checks.
     *
     * @param authData the interface {@link AuthData}
     */
    void setAuthData(AuthData authData);
}
