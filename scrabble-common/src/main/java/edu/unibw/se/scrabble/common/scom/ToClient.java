package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlayerVote;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * ToClient Interface as a way for the server to communicate back to the client
 *
 * @author Seegerer
 */
public interface ToClient extends Remote {
    /**
     * Server sends the client a list of usernames which are member of the same session.
     *
     * @param usernames a list of users, which are in the same session.
     * @throws RemoteException throws exception if RMI connection fails
     */
    void usersInSession(String[] usernames) throws RemoteException;

    /**
     * Method sends the current game state to the client so the client always shows the correct current game state.
     *
     * @param rackTiles contains the tiles the player has on his rack
     * @param swapTiles contains the tiles the player has on the swap bench
     * @param gameData contains all current information about tiles on the board or for example the current bag size
     * @throws RemoteException throws exception if RMI connection fails
     */
    void sendGameState(char[] rackTiles, char[] swapTiles, GameData gameData) throws RemoteException;

    /**
     * After a player placed tiles this method sends the formed words to the other players which must take the decision
     * if they accept those words or vote for a review.
     * <p>
     * Never returns {@code null}.
     *
     * @param placedWords a list of all placed words the user must validate
     * @return {@link ReturnPlayerVote} enum depending on success or error message
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnPlayerVote vote(String[] placedWords) throws RemoteException;
}
