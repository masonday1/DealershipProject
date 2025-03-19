package javafiles;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

import java.lang.reflect.InvocationTargetException;

import java.util.*;

public class FileIOBuilder<Build extends FileIO> {
    private final String[] EXTENSIONS;
    private final Class<Build> CLASS; // used to get around inability to use static
    private static final Class<?>[] CONSTRUCTOR_PARAMS = {String.class, char.class};

    private static final List<FileIOBuilder<?>> BUILDERS = new ArrayList<>();
    private static final List<String> BUILDERS_EXTENSIONS = new ArrayList<>();

    /**
     * Creates a new {@link FileIOBuilder} that creates {@link Build} type {@link FileIO}s
     * when the extension on the path is in extensions.
     *
     * @param extensions The valid path extensions for creating a {@link Build} object.
     * @param clazz The {@link Class} of the {@link Build} this {@link FileIOBuilder} creates.
     */
    private FileIOBuilder(String[] extensions, Class<Build> clazz) {
        CLASS = clazz;
        EXTENSIONS = extensions;
    }

    /**
     * Creates and returns a new {@link FileIO} of type {@link Build} with parameters (path, mode).
     * Instead, returns null if the path does not have an appropriate extension.
     *
     * @param path The path of the file to be opened or created.
     * @param mode A char representation of the type of file this is (read 'r' or write 'w').
     * @return A new {@link FileIO} of type {@link Build} with parameters (path, mode).
     * @throws ReadWriteException When creating {@link Build}(path, mode) throws an exception.
     */
    private FileIO build(String path, char mode) throws ReadWriteException {
        try {
            if (Build.buildable(path, EXTENSIONS)) {
                return CLASS.getDeclaredConstructor(CONSTRUCTOR_PARAMS).newInstance(path, mode);
            }
        } catch (InvocationTargetException e) {
            throw (ReadWriteException) e.getCause();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            System.out.println("Reflection error on \"" + path + "\": '"
                               + mode + "', returning null.");
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

        for (FileIOBuilder<?> builder : BUILDERS) {
            FileIO fileIO = builder.build(path, mode);
            if (fileIO != null) {
                return fileIO;
            }
        }
        throw new ReadWriteException("Extension for \"" + path + "\" not in " +
                                      BUILDERS_EXTENSIONS.toString() + ".");
    }

    /**
     * Creates and adds a single instance of {@link FileIOBuilder} with the appropriate
     * checks to a List of {@link FileIOBuilder}s that will be used to build the appropriate
     * type of {@link FileIO}. Function must be called before calling selectFilePath as
     * selectFilePath needs this as setup, and is only called by setupFileIOBuilders.
     *
     * @param extensions An array of the acceptable extensions for the path that is
     *                   used to create the {@link FileIO}s build by the {@link FileIOBuilder}.
     * @param clazz The {@link Class}<{@link FileIO}> that the {@link FileIOBuilder} is creating.
     */
    private static <Build extends FileIO> void setupFileIOBuilder(String[] extensions, Class<Build> clazz) {
        FileIOBuilder<Build> newBuilder = new FileIOBuilder<>(extensions, clazz);
        try {
            for (FileIOBuilder<?> builder : BUILDERS) {
                if (builder.CLASS == clazz) {
                    throw new ReadWriteException(clazz.getName() + " builder already created.");
                }
            }
            BUILDERS.add(newBuilder);
            Collections.addAll(BUILDERS_EXTENSIONS, newBuilder.EXTENSIONS);
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates and adds the {@link FileIOBuilder}s to a list of {@link FileIOBuilder}s
     * that will be used to build the appropriate type of {@link FileIO}. Function must
     * be called before calling selectFilePath as selectFilePath needs this as setup.
     */
    public static void setupFileIOBuilders() {
        setupFileIOBuilder(new String[]{"json"}, JSONIO.class);
        setupFileIOBuilder(new String[]{"xml"}, XMLIO.class);
    }

}
