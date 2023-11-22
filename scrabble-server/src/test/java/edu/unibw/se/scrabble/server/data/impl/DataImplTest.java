package edu.unibw.se.scrabble.server.data.impl;

import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.DataTest;

public class DataImplTest extends DataTest {

    private static Data data = new DataHashMap();
    //Die Test für die Implementierungen erben den Test fürs Interface und implementieren die abstrakte Erzeugermethode
    @Override
    protected Data getData() {
        return data;
    }
}
