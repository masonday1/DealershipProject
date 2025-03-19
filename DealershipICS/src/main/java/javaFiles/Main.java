package javaFiles;

import java.util.*;

public class Main {

    public static void main(String[] args) throws ReadWriteException {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        Company company = new Company("c_ID", "c_Name");

        FileIOBuilder.setupFileIOBuilders();

        company.dataToInventory(readData(scanner));

        while (true) {
            // Prompt user for the following:
            System.out.println(
                    """
                            Select one of the following actions:\s
                            1) Send vehicles to dealership.
                            2) Check pending vehicle deliveries.\s
                            3) Change dealership vehicle receiving status.\s
                            4) Write dealership inventory to file.
                            5) Read a json file.
                            6) Print Company Inventory.
                            7) Exit program."""
            );

            userInput = scanner.nextLine();

            switch (userInput) {
                case "1": // send vehicles in queue to dealership(s)
                    // if carInventory is empty, print message and return to menu
                    System.out.println("Likely to be removed (as pending removed), not functional at the moment");

                    writeCompanyData(company);
                    System.out.println("Sending vehicles to dealership...");
                    continue;
                case "2":
                    // checking pending vehicle deliveries
                    System.out.println("Likely to be removed (as pending removed), not functional at the moment");
                    printPending(company);
                    System.out.println("Checking pending vehicle deliveries...");
                    continue;
                case "3":
                    System.out.println("Changing dealership vehicle receiving status...");

                    int dealerIndex = getDealershipIndex(company, scanner); // will hold index of Dealership object
                    // Proceed with enabling or disabling the vehicle receiving status once a valid dealership is found
                    if (dealerIndex != -1) {
                        changeReceivingStatus(company, dealerIndex, scanner);
                    }

                    // After completing the dealership status change process, return to the main menu
                    continue; // Exit Case 3 and go back to the main menu
                case "4":
                    // writing dealership inventory to file
                    int itemsWritten =  writeData(company.getDataMap(), scanner);
                    System.out.println("Wrote " + itemsWritten + " items to file");
                    continue;
                case "5":
                    // reading another JSON file
                    company.dataToInventory(readData(scanner));
                    System.out.println("Reading JSON file...");
                    continue;

                case "6":
                    company.printInventory();
                    continue;
                case "7": // exit program
                    System.out.println("Exiting program...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please select a valid option.");
                    continue;
            }

            break;
        }
        scanner.close();
    }

    /**
     * Prompts the user to select a dealership by ID.
     * <p>
     * This method displays a list of valid dealership IDs, prompts the user to enter
     * a dealership ID, and attempts to find the corresponding {@link Dealership}
     * object within the given company. It continues to prompt the user until a valid
     * dealership ID is entered or the user chooses to return to the main menu.
     *
     * @param company The {@link Company} object containing the dealerships.
     * @param scanner The {@link Scanner} object used to read user input.
     * @return The selected {@link Dealership} object if a valid ID is entered, or
     *         -1 if the user chooses to return to the main menu
     */
    private static int getDealershipIndex(Company company, Scanner scanner) {
        String userInput;
        int dealerIndex;
        do {
            System.out.println("Valid ID's:\n" + company.getDealershipIDList());
            System.out.print("Enter the ID of the dealership or back to return to menu: ");
            userInput = scanner.nextLine();

            // If user enters "exit", go back to the main menu
            if (userInput.equalsIgnoreCase("back")) {
                System.out.println("Returning to the main menu...");
                return -1; // Exit the method and return to the main menu
            }

            // Try to find the dealership
            dealerIndex = company.getDealershipIndex(userInput);

            if (dealerIndex == -1) {
                // Dealership not found, prompt user to retry or exit
                System.out.println("Dealership ID not found.");
                System.out.print("Would you like to try again or return to the main menu? "+
                                   "(Enter any key to retry or 'exit' to go back): ");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Returning to the main menu...");
                    return -1; // Exit the current while loop and return to the main menu
                }
                // Otherwise, the loop will continue to prompt for a valid dealership ID
            }
        } while (dealerIndex == -1);

        return dealerIndex;
    }

    /**
     * Changes the Vehicle receiving status of a Dealership.
     * <p>
     * This method prompts the user to either enable or disable the Vehicle receiving
     * status for the specified Dealership.  It checks the current status and
     * provides feedback to the user.
     *
     * @param company The {@link Company} object that contains the dealerships.
     * @param dealerIndex The index of the Dealership in company's list_dealerships list
     * @param scanner The {@link Scanner} object used to read user input.
     */
    private static void changeReceivingStatus(Company company, int dealerIndex, Scanner scanner) {
        String userInput;
        System.out.println(company.changeReceivingStatusIntroString(dealerIndex));

        boolean notChanged;
        do {
            userInput = scanner.nextLine();
            notChanged = company.changeReceivingStatus(dealerIndex, userInput);
        } while (notChanged);
    }

    /**
     * Opens a JSON file based on the given mode and user input.
     * <p>
     * This method allows the user to choose a JSON file and opens it in the specified mode (read or write).
     * It will validate the file path and mode, and repeatedly prompt the user for a new path if an invalid path is entered.
     * If a valid file path is found, a {@link FileIO} instance is created with the path and mode passed to it and returned. If the user cancels the file chooser or
     * enters an invalid path multiple times, the method returns null.
     *
     * @param mode A character representing the mode in which the file should be opened ('r' for read, 'w' for write).
     * @param sc   A {@link Scanner} object used to read user input from the console for path selection and retry prompts.
     * @return     A {@link FileIO} object representing the opened file, or null if no valid file path is selected.
     */
    private static FileIO openFile(char mode, Scanner sc) {
        String path;
        FileIO fileIO = null;
        char userInput;

        try {
            mode = FileIO.getLowercaseMode(mode);
        } catch (ReadWriteException e) {
            System.out.println("Mode '" + mode + "' is not valid, returning null.");
            return null;
        }

        do {
            System.out.print("Choose Path: ");
            path = FileIOBuilder.selectFilePath();
            try {
                if (path != null) {
                    System.out.println(path + "\n");
                    fileIO = FileIOBuilder.buildNewFileIO(path, mode);
                } else {
                    System.out.println("File chooser closed. No file opened.");
                    return null;
                }
            } catch (ReadWriteException e) {
                System.out.print(e.getMessage() + "\n\nEnter new path ('y' / 'n'): ");
                userInput = Character.toLowerCase(sc.next().charAt(0));
                while (userInput != 'y' && userInput != 'n') {
                    System.out.println("Invalid input, try again (only 'y' or 'n' valid): ");
                    userInput = Character.toLowerCase(sc.next().charAt(0));
                }
                if (userInput == 'n') {return null;}
            }
        } while (fileIO == null);
        return fileIO;
    }

    /**
     * Reads data from a JSON file and populates an inventory.
     * <p>
     * This method opens a JSON file in "read" mode ('r') using the provided {@link Scanner}
     * and a {@link JSONIO} object. If the file cannot be opened (e.g., file not found, invalid
     * permissions), the method returns without doing anything. Otherwise, it reads the JSON
     * data, which is expected to be a list of maps, where each map represents a vehicle.
     * This data is then used to populate the provided inventory map and associate
     * vehicles with their respective dealerships within the given company.
     *
     * @param sc  A {@link Scanner} object used by {@link #openFile(char, Scanner)} to read user input from the console for path
     *            selection and retry prompts.
     */
    private static List<Map<String, Object>> readData(Scanner sc) {
        // TODO: Check if data list is still needed, or if only readInventory is needed
        List<Map<String, Object>> data = new ArrayList<>();
        FileIO fileIO = openFile('r', sc);
        if (fileIO == null) {return null;}
        try {
            data.addAll(fileIO.readInventory());
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }

        return data;
    }

    /**
     * Writes data to the JSON file contained by {@link JSONIO} object.
     * <p>
     * This method opens a JSON file contained in {@link JSONIO} object
     * in write ("w") mode and writes data in the form of a {@link List}
     * of {@link Map} which consists of String keys with Object values to the file.
     * If the file cannot be opened or an error occurs during the write operation, an error
     * message is printed to the console, and the method returns 0. If data is successfully
     * written, the method returns the number of entries written in the file.
     *
     * @param data A {@link List} of {@link Map} objects representing the data to be written
     *  *             to the JSON file. Each Map represents data from a vehicle.
     * @param sc A {@link Scanner} object used by {@link #openFile(char, Scanner)} to read user input from the console for path
     *      *            selection and retry prompts.
     * @return The number of items successfully written to the file. Returns 0 if no data
     *  *         is provided, the file cannot be opened, or an error occurs during writing.
     */
    private static int writeData(List<Map<String, Object>> data, Scanner sc) {
        if (data == null) {
            System.out.println("No data to write.");
            return 0;
        }
        FileIO fileIO = openFile('w', sc);
        if (fileIO == null) {return 0;}
        try {
            return fileIO.writeInventory(data);
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /** TODO: Edit description
     * This method processes the vehicle inventory and updates the dealership's incoming vehicles.
     * It checks each vehicle in the provided inventory, finds the corresponding dealership, and if
     * the dealership is acquiring vehicles, it adds the vehicle to the dealership's incoming
     * vehicle list. After processing, it removes the accepted vehicles from the inventory
     *
     * @param company The {@link Company} object that contains the dealerships
     */
    private static void writeCompanyData(Company company) {
        System.out.println("Likely to be removed (as pending removed), not functional at the moment" + company);
    }

    /** TODO: Edit description
     * Prints information about pending vehicle deliveries and dealership status.
     * <p>
     * This method iterates through the provided inventory of vehicles and prints
     * details about each vehicle, including its associated dealership and the
     * dealership's current vehicle receiving status (accepting or not accepting vehicles).
     *
     * @param company   The {@link Company} object used to look up dealership information
     *                  based on the dealership ID.
     */
    private static void printPending(Company company) {
        // if this is used, it will call a method in company that calls a method in dealership
        // that prints out all of the
        System.out.println("Likely to be removed (as pending removed), not functional at the moment."  + company);
    }
}