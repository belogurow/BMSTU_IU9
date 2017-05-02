package com.belogurow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by alexbelogurow on 25.04.17.
 */
public class LexAnalyzer {

    private static Integer[][] table = {
            // ws  Dig   *    /    e    l    i    f  Letter  X
      /*0*/ {  1,   2,   8,   9,   6,   5,   3,   5,   5,   13},
      /*1*/ {  1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   -1},
      /*2*/ { -1,   2,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   13},
      /*3*/ { -1,  -1,  -1,  -1,   5,   5,   5,   4,   5,   13},
      /*4*/ { -1,   5,  -1,  -1,   5,   5,   5,   5,   5,   13},
      /*5*/ { -1,   5,  -1,  -1,   5,   5,   5,   5,   5,   13},
      /*6*/ { -1,   5,  -1,  -1,   5,   7,   5,   5,   5,   13},
      /*7*/ { -1,   5,  -1,  -1,   5,   5,   3,   5,   5,   13},
      /*8*/ { -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   13},
      /*9*/ { -1,  -1,  10,  -1,  -1,  -1,  -1,  -1,  -1,   13},
     /*10*/ { 10,  10,  11,  10,  10,  10,  10,  10,  10,   10},
     /*11*/ { 10,  10,  11,  12,  10,  10,  10,  10,  10,   10},
     /*12*/ { -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   13},
     /*13*/ { -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,  -1,   -1}
    };

    private static int numOfPos(Position curPos) {
        if (curPos.isEOF()) {
            return -1;
        } else if (curPos.isWhitespace()) {
            return 0;
        } else if (curPos.isDecimalDigit()) {
            return 1;
        } else if (curPos.getCode() == '*') {
            return 2;
        } else if (curPos.getCode() == '/') {
            return 3;
        } else if (curPos.getCode() == 'e') {
            return 4;
        } else if (curPos.getCode() == 'l') {
            return 5;
        } else if (curPos.getCode() == 'i') {
            return 6;
        } else if (curPos.getCode() == 'f') {
            return 7;
        } else if (Character.isLetter(curPos.getCode())) {
            return 8;
        } else
            return 9;
    }


    public static void main(String[] args) throws IOException {
        String example = new String(Files.readAllBytes(Paths.get(args[0])));

        example = example.concat(" ").toLowerCase();
        Position curPos = new Position(example);

        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<Error> errors = new ArrayList<>();

        while (!curPos.isEOF()) {
            Position startPos, endPos;
            int state, prevState;

            state = prevState = 0;
            startPos = new Position(curPos);

            while (!curPos.isEOF() && state != -1) {
                int number = numOfPos(curPos);
                prevState = state;
                state = table[state][number];
                if (state != -1) {
                    curPos = curPos.next();
                }
            }

            if (curPos.isEOF()) {
                curPos = curPos.prev();
            }

            if (prevState != 0) { // пропускаем начало файла
                endPos = new Position(curPos);

                if (prevState == 10 || prevState == 11) { // если оказались внутри комментария
                    errors.add(new Error("unclosed comment", curPos));
                    prevState = 12;
                }

                if (prevState == 13) { // встретили неизвестный токен
                    errors.add(new Error("unknown token", curPos));
                }
                if (prevState != 1) { //пропускаем пробел
                    tokens.add(new Token(DomainTag.values()[prevState],
                            example.substring(startPos.getIndex(), endPos.getIndex()), startPos, endPos));
                }

            }
        }

        if (!tokens.isEmpty()) {
            System.out.println("TOKENS:");
        }
        for (Token token: tokens) {
            System.out.println(token);
        }

        if (!errors.isEmpty()) {
            System.out.println("\nERRORS:");
        }
        for (Error error: errors) {
            System.out.println(error);
        }
    }
}
