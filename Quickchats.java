package javaapplicationpart1;

import java.util.ArrayList;
import java.util.Random;

public class Quickchats {

    // Instance variables for each message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String messageStatus;

    // ---------- PART 3: the five arrays (populated at runtime, never hard-coded) ----------
    private static ArrayList<Quickchats> sentMessages = new ArrayList<>();
    private static ArrayList<Quickchats> disregardedMessages = new ArrayList<>();
    private static ArrayList<Quickchats> storedMessages = new ArrayList<>();   // filled from the JSON file
    private static ArrayList<String> messageHashes = new ArrayList<>();
    private static ArrayList<String> messageIDs = new ArrayList<>();

    // Constructor
    public Quickchats(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // 1. Ensures message ID is not more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // 2. Ensures recipient cell number is valid and starts with '+'
    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code.";
    }

    // 3. Creates and returns the Message Hash
    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || message == null || message.trim().isEmpty()) {
            return "00:0:INVALID";
        }
        String prefix = messageID.substring(0, 2);

        String trimmedMsg = message.trim().replaceAll("[^a-zA-Z0-9\\s]", "");
        String[] words = trimmedMsg.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0 ? words[words.length - 1] : "";

        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // 4. Allows user to choose if they want to send, store, or disregard the message
    //    Part 3: each choice now also fills the matching array plus the hash and ID arrays.
    //    Choice 3 (store) only registers the hash/ID: the message itself reaches the
    //    Stored Messages array by being read back from the JSON file (see MessageManager).
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                this.messageStatus = "Sent";
                sentMessages.add(this);
                registerHashAndID(this);
                return "Message successfully sent.";
            case 2:
                this.messageStatus = "Disregarded";
                disregardedMessages.add(this);
                registerHashAndID(this);
                return "Press 0 to delete the message.";
            case 3:
                this.messageStatus = "Stored";
                registerHashAndID(this);
                return "Message successfully stored.";
            default:
                this.messageStatus = "Unknown";
                return "Invalid choice";
        }
    }

    // 5. Returns the total number of messages sent
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // 6. Helper method to generate a random 10-digit ID
    public static String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    // 7. Displays full message details
    public static void printMessageDetails(Quickchats m) {
        System.out.println("\n-----------------------------");
        System.out.println("Message ID: " + m.getMessageID());
        System.out.println("Message Hash: " + m.getMessageHash());
        System.out.println("Recipient: " + m.getRecipient());
        System.out.println("Message: " + m.getMessage());
        System.out.println("-----------------------------");
    }

    // ---------- PART 3 helpers ----------

    // Adds the hash and ID to their arrays (skips duplicates)
    public static void registerHashAndID(Quickchats m) {
        if (!messageHashes.contains(m.getMessageHash())) {
            messageHashes.add(m.getMessageHash());
        }
        if (!messageIDs.contains(m.getMessageID())) {
            messageIDs.add(m.getMessageID());
        }
    }

    // Empties all five arrays (used by the unit tests)
    public static void clearAllArrays() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    // Array getters
    public static ArrayList<Quickchats> getSentMessages() { return sentMessages; }
    public static ArrayList<Quickchats> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<Quickchats> getStoredMessages() { return storedMessages; }
    public static ArrayList<String> getMessageHashes() { return messageHashes; }
    public static ArrayList<String> getMessageIDs() { return messageIDs; }

    // Instance getters
    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageStatus() { return messageStatus; }
    public void setMessageStatus(String status) { this.messageStatus = status; }

    // Rebuilds the hash if the object came from JSON without one
    public String getMessageHash() {
        if (messageHash == null) {
            messageHash = createMessageHash();
        }
        return messageHash;
    }
}