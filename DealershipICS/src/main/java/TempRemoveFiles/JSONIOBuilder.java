package TempRemoveFiles;

public class JSONIOBuilder {}/*implements FileIOBuilder {
    private JSONIOBuilder() {
        FileIO.setChildBuilder(this);
    }

    @Override
    public FileIO build(String path, char mode) {
        try {
            if (JSONIO.buildable(path, mode)) {
                return new JSONIO(path, mode);
            }
        } catch (ReadWriteException e) {
            return null;
        }
        return null;
    }

    @Override
    public String[] getExtensions() {
        return JSONIO.getExtensions();
    }

    public static void setUp() {
        new JSONIOBuilder();
    }
}*/
