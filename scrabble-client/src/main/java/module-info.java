module scrabble.client {
    requires scrabble.common;
    requires java.rmi;

    requires javafx.controls;

    requires java.desktop;

    opens edu.unibw.se.scrabble.client.view.impl to javafx.graphics;

    exports edu.unibw.se.scrabble.client.ccom;
}