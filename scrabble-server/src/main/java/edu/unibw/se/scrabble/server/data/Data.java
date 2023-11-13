package edu.unibw.se.scrabble.server.data;

/**
 * Interface for component Data which is used for user authentication and game data management.
 *
 * @author Bößendörfer
 */
public interface Data {

    /**
     * Returns an interface for user management and user authentication.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link AuthData}
     */
    AuthData getAuthData();


    /**
     * Returns an interface for scrabble data management.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link ScrabbleData}
     */
    ScrabbleData getScrabbleData();
}
