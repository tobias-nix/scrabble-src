package edu.unibw.se.scrabble.server.logic.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import edu.unibw.se.scrabble.common.base.GameData;
import edu.unibw.se.scrabble.common.base.LanguageSetting;
import edu.unibw.se.scrabble.common.base.PlayerState;
import edu.unibw.se.scrabble.server.logic.impl.ServerLogicImpl.*;

public class Sandbox {
    public static void main(String[] args) {
        //System.out.println(createBagFromLanguageSetting(LanguageSetting.GERMAN));
    }

    /*
    static ArrayList<ScrabbleTile> createBagFromLanguageSetting(LanguageSetting languageSetting) {
        String filePath = switch (languageSetting) {
            case LanguageSetting.ENGLISH ->
                    Objects.requireNonNull(ScrabbleBoard.class.getResource("/letterSetEnglish.csv")).getFile();
            case LanguageSetting.GERMAN ->
                    Objects.requireNonNull(ScrabbleBoard.class.getResource("/letterSetGerman.csv")).getFile();
        };

        ArrayList<ScrabbleTile> newBag = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
                Arrays.stream(lineAsArray).forEach(tileInformationAsArray -> {
                    String[] tileInformationSplit = tileInformationAsArray.split(";");

                    for (int i = 0; i < Integer.parseInt(tileInformationSplit[2]); i++) {
                        newBag.add(new ScrabbleTile(
                                tileInformationSplit[0].charAt(0),
                                Integer.parseInt(tileInformationSplit[1])
                        ));
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        Collections.shuffle(newBag);
        return newBag;
    }*/
}

