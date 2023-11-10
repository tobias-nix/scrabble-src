package edu.unibw.se.scrabble.server.data;

import edu.unibw.se.scrabble.common.base.Statistics;

/**
 * Interface for game data management. It is possible to get and save the user's statistic.
 */
public interface ScrabbleData {

    /**
     * Method to get statistics of a user from the database
     * Never returns {@code null}.
     *
     * @param username the user's username
     * @return {@link Statistics}-Object with statistics of user
     */
    Statistics getUserStatistics(String username);

    /**
     * Method for saving statistics of a user to the database
     *
     * @param username   the user's username
     * @param statistics statistics of user
     * @return true, if user's statistics are successfully saved in database otherwise false
     */
    boolean saveUserStatistics(String username, Statistics statistics);
}

