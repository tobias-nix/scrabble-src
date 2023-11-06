package edu.unibw.se.scrabble.server.auth;

import edu.unibw.se.scrabble.server.data.AuthData;

public interface Authentication {

    /** Returns an interface for authentication of user login data
     * Never returns {@code null}.
     *
     * @return the interface Credentials ({@link Credentials})
     */
    Credentials getCredentials();

    /**
     * Add AuthData interface to Authentication so Authentication can make requests to database to perform
     * authentication checks.
     *
     * @param authData AuthData interface
     */
    void setAuthData(AuthData authData);
}
