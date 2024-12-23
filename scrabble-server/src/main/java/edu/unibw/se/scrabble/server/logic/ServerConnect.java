package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.common.base.*;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStatistics;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnCreateSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnJoinSession;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStartGame;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSelectAction;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnPlaceTile;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSwapTile;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnEndTurn;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnStatisticsState;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnSendPlayerVote;

/**
 * Interface ServerConnect which provides methods to play the game scrabble or to navigate in the main menu.
 *
 * @author Kompalka
 */
public interface ServerConnect {
    /**
     * Add ServerConnectCallback Interface to ServerConnect so Server can send data back to clients.
     *
     * @param serverConnectCallback the interface {@link ServerConnectCallback}
     */
    void setServerConnectCallback(ServerConnectCallback serverConnectCallback);

    /**
     * Gets statistics of a given username from the database.
     *
     * @param username the user's username
     * @return record {@link ReturnStatistics} consisting of {@link ReturnStatisticsState}return state and
     * {@link Statistics} Object
     */
    ReturnStatistics getUserStatistics(String username);

    /**
     * Creates a new session for the player if we have less than 10 aktive sessions.
     * <p>
     * Never returns {@code null}.
     *
     * @param languageSetting
     * @param username        the user's username
     * @return {@link ReturnCreateSession} record, containing state-enum and gameID
     */
    ReturnCreateSession createSession(LanguageSetting languageSetting, String username);

    /**
     * Connects the player to the session if the gameID exists and it is enough space.
     * <p>
     * Never returns {@code null}.
     *
     * @param gameId   the ID of the game
     * @param username the user's username
     * @return {@link ReturnJoinSession} enum type, depending on error type or success.
     */
    ReturnJoinSession joinSession(int gameId, String username);

    /**
     * Starts the game if it has at least 2 player in the session.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @return {@link ReturnStartGame} enum type, depending on error type or success.
     */
    ReturnStartGame startGame(String username);

    /**
     * Selects one of the action States.
     * <p>
     * Never returns {@code null}.
     *
     * @param actionState {@link ActionState} includes SWAP, PASS and PLACE
     * @param username the user's username
     * @return {@link ReturnSelectAction} enum type,  depending on error type or success.
     */
    ReturnSelectAction selectAction(ActionState actionState, String username);

    /**
     * Places a tile of a player if it's a valid position.
     * <p>
     * Never returns {@code null}.
     *
     * @param tileWithPosition a tile with the exact position on the board (row and column)
     * @param username the user's username
     * @return {@link ReturnPlaceTile} enum type, depending on error type or success.
     */
    ReturnPlaceTile placeTile(TileWithPosition tileWithPosition, String username);

    /**
     * The client puts a tile on swap bench while the game is in action state SWAP.
     * <p>
     * Never returns {@code null}
     *
     * @param letter the letter of the tile a player wants to swap
     * @param username the user's username
     * @return {@link ReturnSwapTile} enum type, depending on error type or success.
     */
    ReturnSwapTile swapTile(char letter, String username);

    /**
     * Ends the turn of a player.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @return {@link ReturnEndTurn} enum type successful
     */
    ReturnEndTurn endTurn(String username);

    /**
     * A client sends his voting result.
     * <p>
     * Never returns {@code null}
     *
     * @param playerVote Player vote - rejected or confirmed
     * @param username
     * @return {@link ReturnValues.ReturnSendPlayerVote} enum type, depending on error type or success.
     */
    ReturnSendPlayerVote sendPlayerVote(PlayerVote playerVote, String username);

    void informAboutUserLogin(String username);
}
