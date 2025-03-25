package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileIOBuilderTest {
    public static boolean isPathNotFoundException(ReadWriteException e) {
        String str = e.getMessage();
        return str.startsWith("Path: \"") && str.endsWith("\" does not exist, so it can't be read.");
    }

    public static boolean isBadCharException(ReadWriteException e) {
        String str = e.getMessage();
        return str.startsWith("Mode '") && str.endsWith("' is not in {'r', 'R', 'w', 'W'}, file not opened.");
    }

    public static boolean isBadExtensionException(ReadWriteException e) {
        String str = e.getMessage();
        return str.startsWith("Extension for \"") && str.endsWith(" is invalid.");
    }

    public static FileIO getFileIOForTest(String partialPath, String extension, char mode, boolean failExpected)
            throws ReadWriteException {
        FileIOBuilder.setupFileIOBuilders();

        String dir = System.getProperty("user.dir");
        String path = dir + "\\src\\test\\resources\\test_inventory_" + partialPath + extension;

        try {
            FileIO fileIO =  FileIOBuilder.buildNewFileIO(path, mode);
            assertFalse(failExpected);
            return fileIO;
        } catch (ReadWriteException e) {
            assertTrue(failExpected);
            throw e;
        }
    }

    protected static List<Map<Key, Object>> getMaps(FileIO fileIO, int mapNum) {
        List<Map<Key, Object>> maps;
        try {
            maps = fileIO.readInventory();
            assertEquals(mapNum, maps.size());
            return maps;
        } catch (ReadWriteException e) {
            fail(e);
        }
        return null;
    }

    protected static void testMaps(List<Map<Key, Object>> maps, List<Map<Key, Object>> testingMaps) {
        assertEquals(maps.size(), testingMaps.size());
        for (int i = 0; i < maps.size(); i++) {
            Map<Key, Object> map = maps.get(i);
            Map<Key, Object> testingMap = testingMaps.get(i);

            if (map.size() != testingMap.size()) {
                Map<Key, Object> maxMap, minMap;
                if (map.size() > testingMap.size()) {
                    maxMap = map;
                    minMap = testingMap;
                } else  {
                    maxMap = testingMap;
                    minMap = map;
                }
                for (Key key: maxMap.keySet()) {
                    if (!minMap.containsKey(key)) {
                        System.out.print("> ");
                    }
                    System.out.println(key.getKey());
                }
            }

            assertEquals(map.size(), testingMap.size());

            for (Key key : map.keySet()) {
                Object mapVal = map.get(key);
                Object testVal = testingMap.get(key);
                if (key.equals(Key.REASON_FOR_ERROR)) {
                    assertNotNull(testVal);
                    continue;
                }
                assertAll(
                        () -> assertEquals(mapVal, testVal),
                        () -> assertNotNull(mapVal),
                        () -> assertNotNull(testVal),
                        () -> assertEquals(mapVal.getClass(), testVal.getClass())
                );
            }
        }
    }

    @Test
    void buildableBadExtensionRead() {
        try {
            FileIO fileIO = getFileIOForTest("r1", ".txt", 'r', true);
            fail(fileIO.toString());
        } catch (ReadWriteException e) {
            assertTrue(isBadExtensionException(e));
        }
    }

    @Test
    void buildableBadExtensionWrite() {
        try {
            FileIO fileIO = getFileIOForTest("w1", ".txt", 'w', true);
            fail(fileIO.toString());
        } catch (ReadWriteException e) {
            assertTrue(isBadExtensionException(e));
        }
    }
}