package javafiles.dataaccessfiles;

import javafiles.customexceptions.ReadWriteException;

public class XMLIOBuilder extends FileIOBuilder {
    XMLIOBuilder(String[] extensions) {
        super(extensions);
    }

    @Override
    protected FileIO createFileIO(String path, char mode) throws ReadWriteException {
        return new XMLIO(path, mode);
    }

    @Override
    protected String[] getExtensions(char mode) {
        if (mode == 'r') {
            return EXTENSIONS;
        } else {
            return new String[]{};
        }
    }
}
