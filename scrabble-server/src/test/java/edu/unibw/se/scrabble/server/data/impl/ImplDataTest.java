package edu.unibw.se.scrabble.server.data.impl;

import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.DataTest;

public class ImplDataTest extends DataTest {

    //Die Test für die Implementierungen erben den Test fürs Interface und implementieren die abstrakte Erzeugermethode
    @Override
    protected Data getData() {
        return null;
    }
}
