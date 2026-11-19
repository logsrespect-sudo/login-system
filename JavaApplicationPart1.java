
package javaapplicationpart1;

import java.util.Scanner;

public class JavaApplicationPart1 {

    public String name;
    public String surname;
    public String username;
    public String password;
    public String cellPhoneNumber;

    public static void main(String[] args) {

        String name;
        String surname;
        String username;
        String cellPhoneNumber;
        String password;

        Scanner inputDevice = new Scanner(System.in);

        // Enter name
        System.out.print("Please enter your name: ");
        name = inputDevice.nextLine();

        // Enter surname
        System.out.print("Please enter your surname: ");
        surname = inputDevice.nextLine();

        // Register username
        while (true) {
            System.out.print("Please enter username: ");
            username = inputDevice.nextLine();

            if (checkUserName(username)) {
                System.out.println("Username successfully captured");
                break;
            } else {
                System.out.println("Username is not correctly formatted.");
                System.out.println("Username must contain an underscore.");
                System.out.println("Username must not be more than 5 characters.");
            }
        }

        // Register password
        while (true) {
            System.out.print("Please enter password: ");
            password = inputDevice.nextLine();

            if (checkPassword(password)) {
                System.out.println("Password successfully captured");
                break;
            } else {
                System.out.println("Password is not correctly formatted.");
                System.out.println("Password must contain at least 8 characters.");
                System.out.println("Password must contain a capital letter.");
                System.out.println("Password must contain a number.");
                System.out.println("Password must contain a special character.");
            }
        }

        // Register cell phone number
        while (true) {
            System.out.print("Please enter cell phone number: ");
            cellPhoneNumber = inputDevice.nextLine();

            if (checkCellPhoneNumber(cellPhoneNumber)) {
                System.out.println("Cell phone number successfully added.");
                break;
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }

        // Registration successful
        System.out.println();
        System.out.println("Registration successfully completed!");
        System.out.println("Welcome " + name + " " + surname);

        // Create user object
        JavaApplicationPart1 user = new JavaApplicationPart1();
        user.name = name;
        user.surname = surname;
        user.username = username;
        user.password = password;
        user.cellPhoneNumber = cellPhoneNumber;

        // Login check using your login class
        boolean loginSuccess = login.loginUser(inputDevice, user);

        if (!loginSuccess) {
            System.out.println("Login failed. Exiting application.");
            inputDevice.close();
            return;
        }

        // ==================== PART 2: QUICKCHAT ====================
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // Ask how many messages the user wants to enter
        int numberOfMessages = 0;
        while (numberOfMessages <= 0) {
            System.out.print("How many messages would you like to enter? ");
            try {
                numberOfMessages = Integer.parseInt(inputDevice.nextLine().trim());
            } catch (NumberFormatException e) {
                numberOfMessages = 0;
            }
            if (numberOfMessages <= 0) {
                System.out.println("Please enter a whole number greater than 0.");
            }
        }

        boolean running = true;
        boolean messagesEntered = false;

        // While loop: keeps the application running until the user quits
        while (running) {

            System.out.println();
            System.out.println("========== QUICKCHAT MENU ==========");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            String option = inputDevice.nextLine().trim();

            switch (option) {

                case "1":
                    if (messagesEntered) {
                        System.out.println("You have already entered all "
                                + numberOfMessages + " messages.");
                        break;
                    }

                    // FOR loop: only the chosen number of messages can be entered
                    for (int i = 0; i < numberOfMessages; i++) {

                        System.out.println("\n===== Message " + (i + 1)
                                + " of " + numberOfMessages + " =====");

                        String messageID = Quickchats.generateMessageID();

                        // Recipient: ask until the number is valid
                        String recipient;
                        while (true) {

                            System.out.print("Enter recipient cell number: ");
                            recipient = inputDevice.nextLine().trim();

                            Quickchats check = new Quickchats(messageID, i, recipient, "");
                            String result = check.checkRecipientCell();
                            System.out.println(result);

                            if (result.equals("Cell phone number successfully captured.")) {
                                break;
                            }
                        }

                        // Message text: ask until it is 250 characters or less
                        String messageText;
                        while (true) {

                            System.out.print("Enter your message: ");
                            messageText = inputDevice.nextLine();

                            if (messageText.length() <= 250) {
                                break;
                            }
                            System.out.println("Please enter a message of less than 250 characters.");
                        }

                        Quickchats newMessage = new Quickchats(messageID, i, recipient, messageText);
                        System.out.println("Message ID generated: " + messageID);
                        System.out.println("Message Hash: " + newMessage.createMessageHash());

                        // Send, disregard or store
                        System.out.println("\n1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message to send later");

                        int choice = 0;
                        while (choice < 1 || choice > 3) {
                            System.out.print("Choose an option: ");
                            String entry = inputDevice.nextLine().trim();
                            choice = entry.matches("[123]") ? Integer.parseInt(entry) : 0;
                        }

                        System.out.println(newMessage.SentMessage(choice));

                        // Full details are shown only for messages that were sent
                        if (choice == 1) {
                            Quickchats.printMessageDetails(newMessage);
                        }
                    }

                    messagesEntered = true;
                    System.out.println("\nTotal messages sent: " + Quickchats.returnTotalMessages());
                    break;

                case "2":
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat.");
                    break;

                         
               case "4":
                   StoredMessagesMenu.run(inputDevice);
          break;

                default:
                 System.out.println("Invalid option. Please enter 1, 2, 3 or 4");
           break;
            }
        }

        inputDevice.close();
    }

    // Check username
    public static boolean checkUserName(String username) {
        return username.length() <= 5 && username.contains("_");
    }

    // Check cell phone number
    public static boolean checkCellPhoneNumber(String cellPhoneNumber) {
        return cellPhoneNumber.startsWith("+")
                && cellPhoneNumber.length() <= 13;
    }

    // Check password
    public static boolean checkPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)) {
                hasCapitalLetter = true;
            }

            if (Character.isDigit(ch)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(ch)) {
                hasSpecialCharacter = true;
            }   
        }

        return hasCapitalLetter && hasNumber && hasSpecialCharacter;
    }
}




