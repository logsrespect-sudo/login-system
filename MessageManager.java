package javaapplicationpart1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Part 3 logic: loads the stored-messages JSON file into an array and
 * provides every operation behind the "Stored Messages" menu option.
 */
public class MessageManager {

    // Change this to the file name you used in Part 2
    public static final String JSON_FILE = "storedMessages.json";

    private static final String LINE = "-----------------------------";

    // ---------- JSON ----------

    // Reads the JSON file into the Stored Messages array
    public static void loadStoredMessages(String path) throws IOException {
        Type listType = new TypeToken<ArrayList<Quickchats>>() { }.getType();
        List<Quickchats> loaded;
        try (Reader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8)) {
            loaded = new Gson().fromJson(reader, listType);
        }
        Quickchats.getStoredMessages().clear();
        if (loaded != null) {
            for (Quickchats m : loaded) {
                m.setMessageStatus("Stored");
                Quickchats.getStoredMessages().add(m);
                Quickchats.registerHashAndID(m);
            }
        }
    }

    // Writes a list of messages to a JSON file (also used after a delete)
    public static void saveMessagesToJson(List<Quickchats> messages, String path) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            gson.toJson(messages, writer);
        }
    }

    // ---------- 2a. Sender and recipient of all stored messages ----------
    public static String displaySendersAndRecipients() {
        StringBuilder sb = new StringBuilder();
        if (Quickchats.getStoredMessages().isEmpty()) {
            return "No stored messages.\n";
        }
        for (Quickchats m : Quickchats.getStoredMessages()) {
            sb.append("Sender: You\n");               // the app user is always the sender
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
            sb.append(LINE).append("\n");
        }
        return sb.toString();
    }

    // ---------- 2b. Longest stored message ----------
    public static Quickchats getLongestMessage() {
        Quickchats longest = null;
        for (Quickchats m : Quickchats.getStoredMessages()) {
            if (longest == null || m.getMessage().length() > longest.getMessage().length()) {
                longest = m;
            }
        }
        return longest;
    }

    public static String longestMessageText() {
        Quickchats longest = getLongestMessage();
        return longest == null ? "No stored messages." : longest.getMessage();
    }

    // ---------- 2c. Search by message ID (sent + stored) ----------
    public static Quickchats findByMessageID(String id) {
        for (Quickchats m : sentAndStored()) {
            if (m.getMessageID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    // ---------- 2d. All messages for one recipient (sent + stored) ----------
    public static List<String> searchByRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        for (Quickchats m : sentAndStored()) {
            if (m.getRecipient().equals(recipient)) {
                results.add(m.getMessage());
            }
        }
        return results;
    }

    // ---------- 2e. Delete using the message hash ----------
    public static String deleteByHash(String hash) {
        Quickchats target = null;
        for (Quickchats m : sentAndStored()) {
            if (m.getMessageHash().equalsIgnoreCase(hash)) {
                target = m;
                break;
            }
        }
        if (target == null) {
            return "No message found with hash: " + hash;
        }

        Quickchats.getStoredMessages().remove(target);
        Quickchats.getSentMessages().remove(target);
        Quickchats.getMessageHashes().remove(target.getMessageHash());
        Quickchats.getMessageIDs().remove(target.getMessageID());

        // The expected output has no full stop inside the quotes
        String text = target.getMessage().trim();
        if (text.endsWith(".")) {
            text = text.substring(0, text.length() - 1);
        }
        return "Message: \"" + text + "\" successfully deleted.";
    }

    // ---------- 2f. Reports ----------
    // Full details of all stored messages (menu option f)
    public static String storedMessagesReport() {
        return buildReport("STORED MESSAGES REPORT", Quickchats.getStoredMessages(), true);
    }

    // Hash, recipient and message of all sent messages (the "Display Report" unit test)
    public static String sentMessagesReport() {
        return buildReport("SENT MESSAGES REPORT", Quickchats.getSentMessages(), false);
    }

    private static String buildReport(String title, List<Quickchats> list, boolean fullDetails) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== ").append(title).append(" =====\n");
        if (list.isEmpty()) {
            sb.append("No messages.\n");
        }
        for (Quickchats m : list) {
            if (fullDetails) {
                sb.append("Message ID: ").append(m.getMessageID()).append("\n");
                sb.append("Message Number: ").append(m.getMessageNumber()).append("\n");
            }
            sb.append("Message Hash: ").append(m.getMessageHash()).append("\n");
            sb.append("Recipient: ").append(m.getRecipient()).append("\n");
            sb.append("Message: ").append(m.getMessage()).append("\n");
            sb.append(LINE).append("\n");
        }
        return sb.toString();
    }

    private static List<Quickchats> sentAndStored() {
        List<Quickchats> all = new ArrayList<>(Quickchats.getSentMessages());
        all.addAll(Quickchats.getStoredMessages());
        return all;
    }
}