package javaFiles;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        Company company = new Company("c_ID", "c_Name");

        readData(company, scanner);

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

                    Dealership dealer = getDealership(company, scanner); // will hold Dealership object
                    // Proceed with enabling or disabling the vehicle receiving status once a valid dealership is found
                    if (dealer != null) {
                        changeReceivingStatus(dealer, scanner);
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
                    readData(company, scanner);
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
     * Generates a formatted list of dealership IDs.
     * <p>
     * This method retrieves all dealerships associated with the given company and
     * creates a string containing their IDs, separated by tabs.  The IDs are arranged
     * with a maximum of 6 IDs per line. If the company has no dealerships,
     * the method returns a message indicating this.
     *
     * @param company The {@link Company} object whose dealership IDs are to be retrieved.
     * @return A string containing the formatted list of dealership IDs, or the
     *         message "No valid Dealerships." if the company has no dealerships.
     */
    private static String getDealershipIDList(Company company) {
        StringBuilder output = new StringBuilder();
        int added = 0;
        int idPerLine = 6;
        for (Dealership dealership : company.get_list_dealerships()) {
            output.append(dealership.getDealerId()).append("\t");
            if (added % idPerLine == idPerLine - 1) {
                output.append("\n");
            }
            added++;
        }
        if (output.isEmpty()) {
            return "No valid Dealerships.";
        }
        return output.toString();
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
     *         null if the user chooses to return to the main menu
     */
    private static Dealership getDealership(Company company, Scanner scanner) {
        String userInput;
        Dealership dealer;
        do {
            System.out.println("Valid ID's:\n" +getDealershipIDList(company));
            System.out.print("Enter the ID of the dealership or back to return to menu: ");
            userInput = scanner.nextLine();

            // If user enters "exit", go back to the main menu
            if (userInput.equalsIgnoreCase("back")) {
                System.out.println("Returning to the main menu...");
                return null; // Exit the method and return to the main menu
            }

            // Try to find the dealership
            dealer = company.find_dealership(userInput);

            if (dealer == null) {
                // Dealership not found, prompt user to retry or exit
                System.out.println("Dealership ID not found.");
                System.out.print("Would you like to try again or return to the main menu? "+
                                   "(Enter any key to retry or 'exit' to go back): ");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    System.out.println("Returning to the main menu...");
                    return null; // Exit the current while loop and return to the main menu
                }
                // Otherwise, the loop will continue to prompt for a valid dealership ID
            }
        } while (dealer == null);

        return dealer;
    }

    /**
     * Changes the vehicle receiving status of a dealership.
     * <p>
     * This method prompts the user to either enable or disable the vehicle receiving
     * status for the specified dealership.  It checks the current status and
     * provides feedback to the user.
     *
     * @param dealer  The {@link Dealership} object whose receiving status is to be changed.
     * @param scanner The {@link Scanner} object used to read user input.
     */
    private static void changeReceivingStatus(Dealership dealer, Scanner scanner) {
        String userInput;
        System.out.println("Enable or disable vehicle receiving status for dealership "
                + dealer.getDealerId() + "? (Enter 'enable' or 'disable')\n" +
                "Currently enabled? (" + dealer.getStatusAcquiringVehicle() + ")");
        userInput = scanner.nextLine();

        if (userInput.equalsIgnoreCase("enable")) {
            // Check if the dealership's vehicle receiving status is already enabled
            if (dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId() + " is already set to receive vehicles.");
            } else {
                // Enable vehicle receiving for the dealership
                dealer.enableReceivingVehicle();
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been enabled.");
            }
        } else if (userInput.equalsIgnoreCase("disable")) {
            // Disable the vehicle receiving status
            if (!dealer.getStatusAcquiringVehicle()) {
                System.out.println("Dealership " + dealer.getDealerId()+ " is already set to not receive vehicles.");
            } else {
                dealer.disableReceivingVehicle();
                System.out.println("Vehicle receiving status for dealership " + dealer.getDealerId() + " has been disabled.");
            }
        } else {
            System.out.println("Invalid input. Please enter 'enable' or 'disable'.");
        }
    }

    /**
     * Opens a JSON file based on the given mode and user input.
     * <p>
     * This method allows the user to choose a JSON file and opens it in the specified mode (read or write).
     * It will validate the file path and mode, and repeatedly prompt the user for a new path if an invalid path is entered.
     * If a valid file path is found, a {@link JSONIO} instance is created with the path and mode passed to it and returned. If the user cancels the file chooser or
     * enters an invalid path multiple times, the method returns null.
     *
     * @param mode A character representing the mode in which the file should be opened ('r' for read, 'w' for write).
     * @param sc   A {@link Scanner} object used to read user input from the console for path selection and retry prompts.
     * @return     A {@link JSONIO} object representing the opened file, or null if no valid file path is selected.
     */
    private static JSONIO openFile(char mode, Scanner sc) {
        String path;
        JSONIO jsonio = null;
        char userInput;

        try {
            mode = JSONIO.getMode(mode);
        } catch (ReadWriteException e) {
            System.out.println("Mode '" + mode + "' is not valid, returning null.");
            return null;
        }

        do {
            System.out.print("Choose Path: ");
            path = JSONIO.selectJsonFilePath();
            try {
                if (path != null) {
                    System.out.println(path + "\n");
                    jsonio = new JSONIO(path, mode);
                } else {
                    System.out.println("File chooser closed. No file opened.");
                    return null;
                }
            } catch (ReadWriteException e) {
                System.out.print("Path \"" + path + "\" is not a valid path.\n" +
                        "Enter new path ('y' / 'n'): ");
                userInput = Character.toLowerCase(sc.next().charAt(0));
                while (userInput != 'y' && userInput != 'n') {
                    System.out.println("Invalid input, try again (only 'y' or 'n' valid): ");
                    userInput = Character.toLowerCase(sc.next().charAt(0));
                }
                if (userInput == 'n') {return null;}
            }
        } while (jsonio == null);
        return jsonio;
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
     * @param company The {@link Company} object that manages the dealerships. This object is used
     *                to find existing dealerships or create new {@link Dealership} objects if
     *                they don't already exist.
     * @param sc  A {@link Scanner} object used by {@link #openFile(char, Scanner)} to read user input from the console for path
     *            selection and retry prompts.
     */
    private static void readData(Company company, Scanner sc) {
        List<Map<String, Object>> data = new ArrayList<>();
        JSONIO jsonio = openFile('r', sc);
        if (jsonio == null) {return;}
        try {
            data.addAll(jsonio.read());
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }

        company.dataToInventory(data);
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
        JSONIO jsonio = openFile('w', sc);
        if (jsonio == null) {return 0;}
        try {
            return jsonio.write(data);
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