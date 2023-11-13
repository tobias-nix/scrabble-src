package edu.unibw.se.scrabble.client.ccom;

import edu.unibw.se.scrabble.common.scom.NetworkConnect;

/**
 * Interface for connecting clients to a server
 *
 * @author Seegerer
 */
public interface ClientCommunication {

    /**
     * Returns an interface for connecting to a client.
     * <p>
     * Never returns {@code null}.
     *
     * @return the interface ClientConnect ({@link ClientConnect})
     */
    ClientConnect getClientConnect();

    /**
     * Add NetworkConnect interface to ClientCommunication so Client can connect to network.
     *
     * @param networkConnect NetworkConnect interface
     */
    void setNetworkConnect(NetworkConnect networkConnect);
}
