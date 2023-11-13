package edu.unibw.se.scrabble.server.scom;

import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;

/**
 * Interface for component Server Communication which is used to establish RMI connection between Client and Server.
 *
 * @author Seegerer
 */
public interface ServerCommunication {

    /** Returns an interface for communication through a network.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface {@link NetworkConnect}
     */
    NetworkConnect getNetworkConnect();

    /**
     * Add ServerConnect interface to ServerCommunication so ServerCommunication can forward data to ServerLogic.
     *
     * @param serverConnect the interface {@link ServerConnect}
     */
    void setServerConnect(ServerConnect serverConnect);

    /**
     * Add Credentials interface to ServerCommunication so server can ask for authentication checks from Authentication.
     *
     * @param credentials the interface {@link Credentials}
     */
    void setCredentials(Credentials credentials);
}
