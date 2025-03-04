package TempRemoveFiles;

public class XMLIOBuilder /*implements FileIOBuilder {
    private XMLIOBuilder() {
        FileIO.setChildBuilder(this);
    }

    @Override
    public FileIO build(String path, char mode) {
        try {
            if (XMLIO.buildable(path, mode)) {
                return new XMLIO(path, mode);
            }
        } catch (ReadWriteException e) {
            return null;
        }
        return null;
    }

    @Override
    public String[] getExtensions() {
        return XMLIO.getExtensions();
    }

    public static void setUp() {
        new XMLIOBuilder();
    }
}*/{}
