package edu.unibw.se.scrabble.common.base;

import java.io.Serializable;

public record TileWithPosition(char letter, int row, int column) implements Serializable {
}
