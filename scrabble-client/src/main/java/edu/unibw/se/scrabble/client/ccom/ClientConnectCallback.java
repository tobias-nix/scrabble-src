package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlayerVote;

/**
 *
 *
 * @author Seegerer
 */
public interface ClientConnectCallback {
    /**
     * @param usernames
     */
    void usersInSession(String[] usernames); //Dialog "New user joined session"

    /**
     * @param rackTiles
     * @param swapTiles
     * @param gameData
     */
    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData);

    /**
     * @param placedWords
     * @return
     */
    ReturnPlayerVote vote(String[] placedWords);
}
