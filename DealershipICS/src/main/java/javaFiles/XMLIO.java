package javaFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that reads and writes to XML files
 *
 * @author Dylan Browne
 */
public class XMLIO extends FileIO {
    public XMLIO(String path, char mode) throws ReadWriteException {
        super(path, mode);
        if (path.endsWith("pom.xml")) {
            throw new ReadWriteException("Can't read or write to Maven's pom.xml file.\n" +
                    "If this is not Maven's pom.xml file, rename it and try again.");
        }
    }

    public List<Map<String, Object>> readInventory() throws ReadWriteException {
        System.out.println("Not implemented (readInventory).");
        return new ArrayList<>();
    }

    public int writeInventory(List<Map<String, Object>> maps) throws ReadWriteException {
        System.out.println("Not implemented (writeInventory).");
        return 0;
    }

    public List<Map<String, Object>> readDealerships() throws ReadWriteException {
        System.out.println("Not implemented (readDealerships).");
        return null;
    }
    public int writeDealerships(List<Map<String, Object>> maps) throws ReadWriteException {
        System.out.println("Not implemented (writeDealerships).");
        return 0;
    }
}
