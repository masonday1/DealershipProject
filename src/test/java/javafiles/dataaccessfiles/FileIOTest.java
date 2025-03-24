package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileIOTest {

    @Test
    void getLowercaseModeBadChar() {
        char ch = 'x';
        String message = "Mode '" + ch + "' is not in {'r', 'R', 'w', 'W'}, file not opened.";
        try {
            FileIO.getLowercaseMode(ch);
            fail("No ReadWriteException");
        } catch (ReadWriteException e) {
            assertEquals(message, e.getMessage());
        }

    }

    @Test
    void getLowercaseModeGoodLowerR() {
        char ch = 'r';
        char ans;
        try {
            ans = FileIO.getLowercaseMode(ch);
            assertEquals(ch, ans);
        } catch (ReadWriteException e) {
            fail(e);
        }

    }

    @Test
    void getLowercaseModeGoodLowerW() {
        char ch = 'w';
        char ans;
        try {
            ans = FileIO.getLowercaseMode(ch);
            assertEquals(ch, ans);
        } catch (ReadWriteException e) {
            fail(e);
        }

    }

    @Test
    void getLowercaseModeGoodUpperR() {
        char ch = 'R';
        char ans;
        try {
            ans = FileIO.getLowercaseMode(ch);
            assertEquals(Character.toLowerCase(ch), ans);
        } catch (ReadWriteException e) {
            fail(e);
        }

    }

    @Test
    void getLowercaseModeGoodUpperW() {
        char ch = 'W';
        char ans;
        try {
            ans = FileIO.getLowercaseMode(ch);
            assertEquals(Character.toLowerCase(ch), ans);
        } catch (ReadWriteException e) {
            fail(e);
        }

    }

}