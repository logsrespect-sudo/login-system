package javaapplicationpart1;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Fourth main menu option: "Stored Messages".
 * Call StoredMessagesMenu.run(scanner) from your existing main menu.
 */
public class StoredMessagesMenu {

    public static void run(Scanner sc) {
        // Populate the Stored Messages array from the JSON file (no hard-coding)
        try {
            MessageManager.loadStoredMessages(MessageManager.JSON_FILE);
        } catch (IOException e) {
            System.out.println("Could not read " + MessageManager.JSON_FILE + ": " + e.getMessage());
        }

        boolean back = false;
        while (!back) {
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message ID");
            System.out.println("4. Search for all messages to a recipient");
            System.out.println("5. Delete a message using its hash");
            System.out.println("6. Display report of all stored messages");
            System.out.println("7. Back to main menu");
            System.out.print("Choose an option: ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.println(MessageManager.displaySendersAndRecipients());
                    break;
                case "2":
                    System.out.println("Longest message: " + MessageManager.longestMessageText());
                    break;
                case "3":
                    System.out.print("Enter message ID: ");
                    Quickchats found = MessageManager.findByMessageID(sc.nextLine().trim());
                    if (found == null) {
                        System.out.println("Message ID not found.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: " + found.getMessage());
                    }
                    break;
                case "4":
                    System.out.print("Enter recipient: ");
                    List<String> results = MessageManager.searchByRecipient(sc.nextLine().trim());
                    if (results.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        results.forEach(r -> System.out.println("- " + r));
                    }
                    break;
                case "5":
                    System.out.print("Enter message hash: ");
                    System.out.println(MessageManager.deleteByHash(sc.nextLine().trim()));
                    try {   // keep the JSON file in step with the array
                        MessageManager.saveMessagesToJson(Quickchats.getStoredMessages(), MessageManager.JSON_FILE);
                    } catch (IOException e) {
                        System.out.println("Could not update " + MessageManager.JSON_FILE + ": " + e.getMessage());
                    }
                    break;
                case "6":
                    System.out.println(MessageManager.storedMessagesReport());
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}