package edu.unibw.se.scrabble.server.logic;

import edu.unibw.se.scrabble.server.data.ScrabbleData;

public interface ServerLogic {

    /** Returns an interface for connecting to the Game Server.
     * Never returns {@code null}.
     *
     * @return the interface ServerConnect ({@link ServerConnect})
     */
    ServerConnect getServerConnect();

    /**
     * Add ScrabbleData interface to ServerLogic so server can persistently save data to the database.
     *
     * @param scrabbleData ScrabbleData interface
     */
    void setScrabbleData(ScrabbleData scrabbleData);
}
