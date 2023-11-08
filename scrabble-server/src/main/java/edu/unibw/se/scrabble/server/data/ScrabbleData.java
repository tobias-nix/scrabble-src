package edu.unibw.se.scrabble.server.data;

import edu.unibw.se.scrabble.common.base.Statistics;

public interface ScrabbleData {
    Statistics getUserStatistics(String username);

    boolean saveUserStatistics(String username, Statistics statistics);
}
