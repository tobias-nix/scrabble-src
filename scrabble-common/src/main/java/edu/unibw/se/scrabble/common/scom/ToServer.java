package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStatistics;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnCreateSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnJoinSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStartGame;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSelectAction;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlaceTiles;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnEndTurn;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface ToServer extends Remote {
    /**
     * Returns all statistics from the database for a given username.
     *
     * @return {@link ReturnStatistics} object containing player statistics
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnStatistics getUserStatistics() throws RemoteException;

    /**
     * User creates a new session if there are less than 10 active sessions ongoing.
     * <p>
     * Never returns {@code null}.
     *
     * @return {@link ReturnCreateSession} enum depending on success or error message.
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnCreateSession createSession() throws RemoteException;

    /**
     * User joins an already existing session via a given game ID, if ID is valid and there are less than 4 players
     * in the session.
     * <p>
     * Never returns {@code null}.
     *
     * @param gameID ID of a session
     * @return {@link ReturnJoinSession} enum depending on success or error message.
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnJoinSession joinSession(int gameID) throws RemoteException;

    /**
     * Starts a game if there are at least 2 players in the session.
     * <p>
     * Never returns {@code null}.
     *
     * @return {@link ReturnStartGame} enum depending on success or error message.
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnStartGame startGame() throws RemoteException;

    /**
     * Player selected an action for his turn and sends this information to server.
     * <p>
     * Never returns {@code null}.
     *
     * @param actionState {@link ActionState} enum of action the player chose.
     * @return {@link ReturnSelectAction} enum depending on success or error message.
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnSelectAction selectAction(ActionState actionState) throws RemoteException;

    /**
     * Player placed a tile in action mode place and sends this information to server.
     * <p>
     * Never returns {@code null}.
     *
     * @param tileWithPosition {@link TileWithPosition} containing tile location and tile letter
     * @return {@link ReturnPlaceTiles} enum depending on success or error message.
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition) throws RemoteException;

    /**
     * Player wants to end his turn.
     * <p>
     * Never returns {@code null}.
     *
     * @return {@link ReturnEndTurn} enum depending on success or error message.
     * @throws RemoteException throws exception if RMI connection fails
     */
    ReturnEndTurn endTurn() throws RemoteException;
}
