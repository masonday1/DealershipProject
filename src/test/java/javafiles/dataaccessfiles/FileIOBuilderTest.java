package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.BadExtensionException;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileIOBuilderTest {
    public static boolean isSameCauseType(Object expectedException, Object testedException) {
        assertInstanceOf(ReadWriteException.class, expectedException);
        Throwable causeTarget = ((ReadWriteException) expectedException).getCause();

        assertInstanceOf(ReadWriteException.class, testedException);
        Throwable cause = ((ReadWriteException) testedException).getCause();

        return causeTarget.getClass().equals(cause.getClass());
    }

    public static FileIO getFileIOForTest(String partialPath, String folder, String extension,
                                          char mode, boolean failExpected) throws ReadWriteException {
        FileIOBuilder.setupFileIOBuilders();

        String dir = System.getProperty("user.dir");
        String path = dir + "\\src\\test\\resources\\"+ folder +"\\test_inventory_" + partialPath + extension;

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

    private static void printBadMap(Map<Key, Object> map, Map<Key, Object> testingMap) {
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
            System.out.println(key.getKey() + ": " + maxMap.get(key));
        }
    }

    protected static void testMaps(List<Map<Key, Object>> maps, List<Map<Key, Object>> testingMaps) {
        assertEquals(maps.size(), testingMaps.size());
        for (int i = 0; i < maps.size(); i++) {
            Map<Key, Object> map = maps.get(i);
            Map<Key, Object> testingMap = testingMaps.get(i);

            if (map.size() != testingMap.size()) {printBadMap(map, testingMap);}
            assertEquals(map.size(), testingMap.size());

            for (Key key : map.keySet()) {
                Object mapVal = map.get(key);
                Object testVal = testingMap.get(key);

                if (key.equals(Key.REASON_FOR_ERROR)) {
                    assertTrue(isSameCauseType(mapVal, testVal));
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
            FileIO fileIO = getFileIOForTest("r1", "",".txt", 'r', true);
            fail(fileIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }

    @Test
    void buildableBadExtensionWrite() {
        try {
            FileIO fileIO = getFileIOForTest("w1", "", ".txt", 'w', true);
            fail(fileIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertTrue(FileIOBuilderTest.isSameCauseType(new ReadWriteException(cause), e));
        }
    }
}