package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.GameData;

/**
 * Interface to give the server to opportunity to send game data back to the clients
 *
 * @author Seegerer
 */
public interface ServerConnectCallback {
    /**
     * Adds all users in the session.
     *
     * @param usernames the user's username
     */
    void usersInSession(String[] usernames);

    /**
     * Sends all necessary data to the user.
     *
     * @param username the user's username
     * @param rackTiles the user's tiles on his rack
     * @param swapTiles the user's tiles he wants to swap
     * @param gameData contains all required data of the game for the users
     */
    // if swapTiles is empty, send empty array instead of null-pointer
    // game state is different for every user -> rackTiles
    void sendGameState(String username, char[] rackTiles, char[] swapTiles, GameData gameData);

    /**
     * After a player placed tiles this method sends the formed words to the other players which must take the decision
     * if they accept those words or vote for a review.
     * <p>
     * Never returns {@code null}.
     *
     * @param username    the user's username
     * @param placedWords the user's placed words
     */
    void vote(String username, String[] placedWords);
}
