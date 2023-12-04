package edu.unibw.se.scrabble.common.base;

import java.io.Serializable;

public record Statistics(int gamesPlayed, int gamesWon, int highestScore, int totalScore) implements Serializable {
}