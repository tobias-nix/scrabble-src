package edu.unibw.se.scrabble.server.scom;

import edu.unibw.se.scrabble.common.scom.NetworkConnect;
import edu.unibw.se.scrabble.server.auth.Credentials;
import edu.unibw.se.scrabble.server.logic.ServerConnect;

public interface ServerCommunication {

    /** Returns an interface for communication through a network.
     * Never returns {@code null}.
     *
     * @return the interface NetworkConnect ({@link NetworkConnect})
     */
    NetworkConnect getNetworkConnect();

    /**
     * Add ServerConnect interface to ServerCommunication so ServerCommunication can forward data to ServerLogic.
     *
     * @param serverConnect ServerConnect interface
     */
    void setServerConnect(ServerConnect serverConnect);

    /**
     * Add Credentials interface to ServerCommunication so server can ask for authentication checks from Authentication.
     *
     * @param credentials Credentials interface
     */
    void setCredentials(Credentials credentials);
}
