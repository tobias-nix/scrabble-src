package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlayerVote;

/**
 * ...
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
    void sendGameData(String username, char[] rackTiles, char[] swapTiles, GameData gameData);

    /**
     * Checks if the places words are correct.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @param placedWords the user's placed words
     * @return {@link ReturnPlayerVote} enum type, depending on error type or success.
     */
    ReturnPlayerVote vote(String username, String[] placedWords);
}
