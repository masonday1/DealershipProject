package javafiles.dataaccessfiles;

import javafiles.Key;
import javafiles.customexceptions.BadExtensionException;
import javafiles.customexceptions.ReadWriteException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileIOBuilderTest {
    private static final List<String> pathsRun = new ArrayList<>();
    private static String lastPath = null;

    /**
     * Used to ensure that two files don't get evaluated twice in different tests, while allowing
     * for the same test to use the same path.
     */
    static void updatePathsRun() {
        if (lastPath == null) {return;}
        pathsRun.add(lastPath);
        lastPath = null;
    }

    @AfterEach
    void updatePathsRunAfterEach() {
        updatePathsRun();
    }


    /**
     * Returns the path of the {@link FileIO} to be created from the given information.
     *
     * @param partialPath The name of the file after "test_inventory_" and before the extension.
     * @param folder The folder within src/test/resources the file is in.
     * @param extension The extension of the file.
     * @return the full path of the file.
     */
    public static String getPath(String partialPath, String folder, String extension) {
        String dir = System.getProperty("user.dir");
        String path = dir + "\\src\\test\\resources\\" + folder + "\\test_" + partialPath + extension;

        if (pathsRun.contains(path)) {fail("Path \"" + path + "\" already tested");}
        lastPath = path;

        return path;
    }

    /**
     * Returns a {@link FileIO} for testing. If retrieving a {@link FileIO} is expected to fail, it
     * throws a {@link ReadWriteException} instead. If the {@link FileIO} is created when it is not
     * expected to be or vise versa, the test fails.
     *
     * @param partialPath The ending part of the file before the extension.
     * @param folder The folder that the file to read is in.
     * @param extension The extension of the file.
     * @param mode The mode of the {@link FileIO} being created.
     * @param failExpected If the creation of the {@link FileIO} is expected to fail.
     * @return the newly created {@link FileIO}.
     * @throws ReadWriteException If the creation of the {@link FileIO} was expected to fail and did.
     */
    public static FileIO getFileIOForTest(String partialPath, String folder, String extension,
                                          char mode, boolean failExpected) throws ReadWriteException {
        FileIOBuilder.setupFileIOBuilders();

        String path = getPath(partialPath, folder, extension);

        try {
            FileIO fileIO =  FileIOBuilder.buildNewFileIO(path, mode);
            assertFalse(failExpected);
            return fileIO;
        } catch (ReadWriteException e) {
            assertTrue(failExpected);
            throw e;
        }
    }

    /**
     * Takes a {@link FileIO} and returns the {@link List} of {@link Map}s created from
     * reading the file. Fails if it can not read the file or the number of maps read is
     * incorrect.
     *
     * @param fileIO The {@link FileIO} being read.
     * @param mapNum The expected number of {@link Map}s read
     * @return a {@link List} of all {@link Map}s read.
     */
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

    /**
     * Prints a representation of the difference between two {@link Map} that are supposed to be the same,
     * but have different numbers of items. The test fails immediately after.
     *
     * @param map The expected {@link Map}.
     * @param testingMap The calculated {@link Map}.
     */
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

    /**
     * Returns weather or not two {@link ReadWriteException} have the same cause.
     * Fails if either {@link Object} is not a {@link ReadWriteException}.
     *
     * @param expectedException The expected value of the exception.
     * @param testedException The tested value.
     */
    public static void assertSameCauseType(Object expectedException, Object testedException) {
        assertInstanceOf(ReadWriteException.class, expectedException);
        Throwable causeTarget = ((ReadWriteException) expectedException).getCause();
        assertNotNull(causeTarget);

        assertInstanceOf(ReadWriteException.class, testedException);
        Throwable cause = ((ReadWriteException) testedException).getCause();
        assertNotNull(cause);

        assertEquals(causeTarget.getClass(), cause.getClass());
    }

    /**
     * Tests if all {@link Map}s in testingMaps are the same as the expected {@link Map}s in maps.
     *
     * @param maps The {@link List} of the {@link Map}s that are expected.
     * @param testingMaps The {@link List} of {@link Map}s that are being tested.
     */
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
                    assertSameCauseType(mapVal, testVal);
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

    // Expected: Creation of FileIO fails and an exception is thrown.
    @Test
    void buildableBadExtensionRead() {
        try {
            FileIO fileIO = getFileIOForTest("r_bad_extension", "",".txt", 'r', true);
            fail(fileIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertSameCauseType(new ReadWriteException(cause), e);
        }
    }

    // Expected: Creation of FileIO fails and an exception is thrown.
    @Test
    void buildableBadExtensionWrite() {
        try {
            FileIO fileIO = getFileIOForTest("w_bad_extension", "", ".txt", 'w', true);
            fail(fileIO.toString());
        } catch (ReadWriteException e) {
            BadExtensionException cause = new BadExtensionException("Bad extension.");
            assertSameCauseType(new ReadWriteException(cause), e);
        }
    }
}