package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

public class JSONIOBuilder extends FileIOBuilder {
    JSONIOBuilder(String[] extensions) {
        super(extensions);
    }

    @Override
    protected FileIO createFileIO(String path, char mode) throws ReadWriteException {
        return new JSONIO(path, mode);
    }
}
