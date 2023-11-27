package edu.unibw.se.scrabble.common.scom;

import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnRegisterUser;
import edu.unibw.se.scrabble.common.base.ReturnValues.ReturnLoginUser;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * NetworkConnect Interface for requesting authentication for user data from the server
 *
 * @author Seegerer
 */
public interface NetworkConnect extends Remote {

    /**
     * Authenticates the users login credentials by checking with userdata in the database.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @param password the user's password
     * @param toClient {@link ToClient} object so server is able to communicate back to client
     * @return {@link ReturnLoginNetwork} object consisting of {@link ReturnLoginUser} state and {@link ToServer} object
     */
    ReturnLoginNetwork loginUser(String username, String password, ToClient toClient) throws RemoteException;
    class ReturnLoginNetwork {
        public static ReturnLoginNetwork INVALID_USERNAME =
                new ReturnLoginNetwork(ReturnLoginUser.INVALID_USERNAME);
        public static ReturnLoginNetwork INVALID_PASSWORD =
                new ReturnLoginNetwork(ReturnLoginUser.INVALID_PASSWORD);
        public static ReturnLoginNetwork FAILURE =
                new ReturnLoginNetwork(ReturnLoginUser.FAILURE);
        public static ReturnLoginNetwork NETWORK_FAILURE =
                new ReturnLoginNetwork(ReturnLoginUser.NETWORK_FAILURE);
        public static ReturnLoginNetwork DATABASE_FAILURE =
                new ReturnLoginNetwork(ReturnLoginUser.DATABASE_FAILURE);
        public static ReturnLoginNetwork USERNAME_NOT_IN_DATABASE =
                new ReturnLoginNetwork(ReturnLoginUser.USERNAME_NOT_IN_DATABASE);
        public static ReturnLoginNetwork WRONG_PASSWORD =
                new ReturnLoginNetwork(ReturnLoginUser.WRONG_PASSWORD);
        public final ReturnLoginUser state;
        public final ToServer toServer;

        private ReturnLoginNetwork(ReturnLoginUser state) {
            this.state = state;
            this.toServer = null;
        }

        public ReturnLoginNetwork(ReturnLoginUser state, ToServer toServer) {
            this.state = state;
            if (this.state != ReturnLoginUser.SUCCESSFUL) {
                this.toServer = null;
            } else {
                this.toServer = toServer;
            }

        }
    }

    /**
     * Creates new user dataset in database if username is not already taken.
     * <p>
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @param password the user's password
     * @return {@link ReturnRegisterUser} enum depending on success or error of action
     */
    ReturnRegisterUser registerUser(String username, String password);
}
