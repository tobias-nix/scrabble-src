package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnLoginUser;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnRegisterUser;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStatistics;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnCreateSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnJoinSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStartGame;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSelectAction;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlaceTiles;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnEndTurn;

/**
 * Interface to provide methods for client to server communication
 */

public interface ClientConnect {

    /**
     * Add ClientConnectCallback interface to ClientConnect so that clientCommunication can send data to the Client
     *
     * @param clientConnectCallback the information to be sent to the client back
     */
    void setClientConnectCallback(ClientConnectCallback clientConnectCallback);

    /**
     * Authenticates the users login credentials by sending it to the server.
     * <p>
     * Never returns {@code null}
     *
     * @param username              the user's username
     * @param password              the user's password
     * @param clientConnectCallback the information to be sent to the client back
     * @return {@link ReturnLogin} enum type, depending on error type or success.
     */
    ReturnLoginUser loginUser(String username, String password, ClientConnectCallback clientConnectCallback);

    /**
     * Creates new user dataset by sending it to the server.
     * <p>
     * Never returns {@code null}
     *
     * @param username              the user's username
     * @param password              the user's password
     * @param clientConnectCallback the information to be sent to the client back
     * @return {@link ReturnRegister} enum type, depending on error type or success.
     */
    ReturnRegisterUser registerUser(String username, String password, ClientConnectCallback clientConnectCallback);

    /**
     * Returns statistics of the user.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link ReturnStatisticsState} and the class {@link Statistics}
     */
    ReturnStatistics getUserStatistics();

    /**
     * Creates a new Session.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link ReturnCreateSessionState} and the integer gameId
     */
    ReturnCreateSession createSession();

    /**
     * Adds the client to an existing session.
     * <p>
     * Never returns {@code null}.
     *
     * @param gameID the gameId of the session the client will join
     * @return {@link ReturnJoinSession} enum type, depending on error type or success.
     */
    ReturnJoinSession joinSession(int gameID);


    /**
     * Starts a new game from a session.
     * <p>
     * Never returns {@code null}.
     *
     * @return {@link ReturnStartGame} enum type, depending on error type or success.
     */
    ReturnStartGame startGame();

    /**
     * The client can choose between three actions: "PASS", "SWAP", "PLACE".
     * <p>
     * Never returns {@code null}.
     *
     * @param actionState the selected action of the client
     * @return {@link ReturnSelectAction} enum type, depending on error type or success.
     */
    ReturnSelectAction selectAction(ActionState actionState);
    
    /**
     * The client puts a tile on the board.
     * <p>
     * Never returns {@code null}
     *
     * @param tileWithPosition the tile with the position information
     * @return {@link ReturnPlaceTiles} enum type, depending on error type or success.
     */
    ReturnPlaceTiles placeTiles(TileWithPosition tileWithPosition);

    /**
     * A client completes his turn.
     * <p>
     * Never returns {@code null}
     *
     * @return {@link ReturnEndTurn} enum type, depending on error type or success.
     */
    ReturnEndTurn endTurn();
}
