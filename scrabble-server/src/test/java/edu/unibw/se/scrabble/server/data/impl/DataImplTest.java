package edu.unibw.se.scrabble.server.data.impl;

import edu.unibw.se.scrabble.server.data.Data;
import edu.unibw.se.scrabble.server.data.DataTest;
import edu.unibw.se.scrabble.server.data.impl.hashMap.DataHashMap;

/**
 @author Bößendörfer
 */

public class DataImplTest extends DataTest {

    private static Data data = new DataHashMap();

    //Die Test für die Implementierungen erben den Test fürs Interface und implementieren die abstrakte Erzeugermethode
    @Override
    protected Data getData() {
        return data;
    }
}
