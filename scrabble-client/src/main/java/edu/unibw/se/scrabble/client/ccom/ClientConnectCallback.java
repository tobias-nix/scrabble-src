package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlayerVote;

/**
 * Interface to give the opportunity to send data back to the clients
 *
 * @author Seegerer
 */
public interface ClientConnectCallback {
    /**
     * Sends all current users in the session as an array of strings.
     *
     * @param usernames array of all users in the session
     */
    void usersInSession(String[] usernames); //Dialog "New user joined session"

    /**
     * Sends the current game state to the client. This includes tiles on rack and bench and a {@link GameData} object
     *
     * @param rackTiles tiles, which are located on the player's rack
     * @param swapTiles tiles, which are located on the player's bench
     * @param gameData {@link GameData} object, which includes all information needed to show the scrabble board
     */
    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData);

    /**
     * After a player placed tiles this method sends the formed words to the other players which must take the decision
     * if they accept those words or vote for a review.
     * <p>
     * Never returns {@code null}.
     *
     * @param placedWords array of all the words, a player as placed during his turn
     * @return {@link ReturnPlayerVote} enum depending on if player votet to veto or to accept the placed words
     */
    ReturnPlayerVote vote(String[] placedWords);
}
