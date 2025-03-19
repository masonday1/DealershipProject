package javafiles;

import javafiles.customexceptions.ReadWriteException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import java.util.*;

public abstract class FileIOBuilderTemp {
    private static class JSONIOBuilder extends FileIOBuilderTemp{
        JSONIOBuilder(String[] extensions) {
            super(extensions);
        }

        @Override
        protected FileIO createFileIO(String path, char mode) throws ReadWriteException {
            return new JSONIO(path, mode);
        }
    }
    private static class XMLBuilder extends FileIOBuilderTemp{
        XMLBuilder(String[] extensions) {
            super(extensions);
        }

        @Override
        protected FileIO createFileIO(String path, char mode) throws ReadWriteException {
            return new XMLIO(path, mode);
        }
    }

    protected abstract FileIO createFileIO(String path, char mode) throws ReadWriteException;
    
    private final String[] EXTENSIONS;

    private static final List<FileIOBuilderTemp> BUILDERS = new ArrayList<>();
    private static final List<String> BUILDERS_EXTENSIONS = new ArrayList<>();
    private static boolean instantiated = false;


    /**
     * Creates a new {@link FileIOBuilderTemp} that creates {@link FileIO} type {@link FileIO}s
     * when the extension on the path is in extensions.
     *
     * @param extensions The valid path extensions for creating a {@link FileIO} object.
     */
    private FileIOBuilderTemp(String[] extensions) {
        EXTENSIONS = extensions;
        BUILDERS_EXTENSIONS.addAll(Arrays.asList(extensions));
    }

    /**
     * Creates and returns a new {@link FileIO} of type {@link FileIO} with parameters (path, mode).
     * Instead, returns null if the path does not have an appropriate extension.
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file this is (read 'r' or write 'w').
     * @return A new {@link FileIO} of type {@link FileIO} with parameters (path, mode).
     * @throws ReadWriteException When creating {@link FileIO}(path, mode) throws an exception.
     */
    private FileIO build(String path, char mode) throws ReadWriteException {
        if (FileIO.buildable(path, EXTENSIONS)) {
            return createFileIO(path, mode);
        }
        return null;
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in extensions.
     *
     * @param extensions The available extensions of files that can be chosen.
     * @return The selected path to the file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     *
     * @author Christopher Engelhart
     */
    private static File selectFile(String[] extensions) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Choose File", extensions));
        int result = fileChooser.showOpenDialog(null);
        return result == 0 ? fileChooser.getSelectedFile() : null;
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in extensions.
     *
     * @param extensions The available extensions of files that can be chosen.
     * @return The selected path to the file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     */
    private static String selectFilePath(String[] extensions) {
        File file = selectFile(extensions);
        if (file == null) {return null;}
        return file.toString();
    }

    /**
     * Opens a file chooser dialog to allow the user to select a file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with an extension in
     * BUILDERS_EXTENSIONS.
     *
     * @return The selected path to the file if the user selects a file and confirms
     *         the dialog, or null if the user cancels or closes the dialog without
     *         selecting a file.
     */
    public static String selectFilePath() {
        String[] extensions = new String[BUILDERS_EXTENSIONS.size()];
        for (int i = 0; i < extensions.length; i++) {extensions[i] = BUILDERS_EXTENSIONS.get(i);}
        return selectFilePath(extensions);
    }

    /**
     * Creates and returns the appropriate type of {@link FileIO} from the given path
     * and mode. If the creation of the {@link FileIO} is invalid, throws a new
     * {@link ReadWriteException} (such as through invalid path, mode, or overriding
     * Maven files).
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file this is (read 'r' or write 'w').
     * @return The new instance of the {@link FileIO} created.
     * @throws ReadWriteException When the creation of the {@link FileIO} is invalid.
     */
    public static FileIO buildNewFileIO(String path, char mode) throws ReadWriteException {
        mode = FileIO.getLowercaseMode(mode);
        File file = new File(path);
        if (!file.exists() && mode == 'r') {
            throw new ReadWriteException("Path: \"" + path + "\" does not exist, so it can't be read.");
        }

        for (FileIOBuilderTemp builder : BUILDERS) {
            FileIO fileIO = builder.build(path, mode);
            if (fileIO != null) {
                return fileIO;
            }
        }
        throw new ReadWriteException("Extension for \"" + path + "\" not in " +
                BUILDERS_EXTENSIONS.toString() + ".");
    }

    /**
     * Creates and adds the {@link FileIOBuilderTemp}s to a list of {@link FileIOBuilderTemp}s
     * that will be used to build the appropriate type of {@link FileIO}. Function must
     * be called before calling selectFilePath as selectFilePath needs this as setup.
     */
    public static void setupFileIOBuilders() {
        if (!instantiated) {
            BUILDERS.add(new JSONIOBuilder(new String[]{"json"}));
            BUILDERS.add(new XMLBuilder(new String[]{"xml"}));
            instantiated = true;
        }
    }

}

