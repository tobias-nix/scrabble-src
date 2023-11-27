package edu.unibw.se.scrabble.server.logic.impl;

import edu.unibw.se.scrabble.server.logic.ServerLogic;
import edu.unibw.se.scrabble.server.logic.ServerLogicTest;

public class ImplServerLogicTest extends ServerLogicTest {
    private final ServerLogic serverLogic = new ServerLogicImpl();
    @Override
    public ServerLogic getServerLogic() {
        return serverLogic;
    }
}
