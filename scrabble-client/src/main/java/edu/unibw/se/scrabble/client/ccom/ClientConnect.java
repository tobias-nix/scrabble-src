package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.base.ActionState;
import edu.unibw.se.scrabble.common.base.PlayerVote;
import edu.unibw.se.scrabble.common.base.TileWithPosition;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnLoginUser;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnRegisterUser;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStatistics;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnCreateSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnJoinSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStartGame;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSelectAction;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlaceTile;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSwapTile;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnEndTurn;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSendPlayerVote;

/**
 * Interface to provide methods for client to server communication
 *
 * @author Nix
 */

public interface ClientConnect {

    /**
     * Add ClientConnectCallback interface to ClientConnect so that clientCommunication can send data to the Client
     *
     * @param clientConnectCallback {@link ClientConnectCallback} the information to be sent to the client back
     */
    void setClientConnectCallback(ClientConnectCallback clientConnectCallback);

    /**
     * Authenticates the users login credentials by sending it to the server.
     * <p>
     * Never returns {@code null}
     *
     * @param username              the user's username
     * @param password              the user's password
     * @return {@link ReturnLoginUser} enum type, depending on error type or success.
     */
    ReturnLoginUser loginUser(String username, String password);

    /**
     * Creates new user dataset by sending it to the server.
     * <p>
     * Never returns {@code null}
     *
     * @param username              the user's username
     * @param password              the user's password
     * @return {@link ReturnRegisterUser} enum type, depending on error type or success.
     */
    ReturnRegisterUser registerUser(String username, String password);

    /**
     * Returns statistics of the user.
     * <p>
     * Never returns {@code null}.
     *
     * @return {@link ReturnStatistics} record containing player statistics
     */
    ReturnStatistics getUserStatistics();

    /**
     * Creates a new Session.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link ReturnCreateSession} and the integer gameId
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
     * @param actionState enum type, the selected action of the client
     * @return {@link ReturnSelectAction} enum type, depending on error type or success.
     */
    ReturnSelectAction selectAction(ActionState actionState);
    
    /**
     * The client puts a tile on the board.
     * <p>
     * Never returns {@code null}
     *
     * @param tileWithPosition record, the tile with the position information
     * @return {@link ReturnPlaceTile} enum type, depending on error type or success.
     */
    ReturnPlaceTile placeTile(TileWithPosition tileWithPosition);

    /**
     * The client puts a tile on swap bench.
     * <p>
     * Never returns {@code null}
     *
     * @param letter the letter of the tile a player wants to swap
     * @return {@link ReturnSwapTile} enum type, depending on error type or success.
     */
    ReturnSwapTile swapTile(char letter);

    /**
     * A client completes his turn.
     * <p>
     * Never returns {@code null}
     *
     * @return {@link ReturnEndTurn} enum type, depending on error type or success.
     */
    ReturnEndTurn endTurn();


    /**
     * A client sends his voting result.
     * <p>
     * Never returns {@code null}
     *
     * @param playerVote Player vote - rejected or confirmed
     * @return {@link ReturnSendPlayerVote} enum type, depending on error type or success.
     */
    ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote);
}
