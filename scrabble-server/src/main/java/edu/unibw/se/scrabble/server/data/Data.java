package edu.unibw.se.scrabble.server.data;

public interface Data {
    /** Returns an interface for user management and user authentication.
     * Never returns {@code null}.
     *
     * @return the interface AuthData ({@link AuthData})
     */
    AuthData getAuthData();


    /** Returns an interface for scrabble data management.
     * Never returns {@code null}.
     *
     * @return the interface ChatData ({@link ScrabbleData})
     */
    ScrabbleData getScrabbleData();
}
