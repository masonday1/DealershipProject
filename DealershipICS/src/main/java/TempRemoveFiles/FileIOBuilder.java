package TempRemoveFiles;

import javaFiles.FileIO;

public interface FileIOBuilder {

    static void setUp() {
        // create an instance of the class and adds it to FileIO.childBuilders
    }

    FileIO build(String path, char mode);
    String[] getExtensions();
}
