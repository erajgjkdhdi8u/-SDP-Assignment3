package notifications;
import java.util.ArrayList;
import java.util.List;
public class ScheduledBatchNotification extends Notification {
    private final List<String> recipients;
    public ScheduledBatchNotification(MessageSender sender, List<String> recipients) {
        super(sender);
        this.recipients = new ArrayList<>(recipients);
    }
    public void addRecipient(String recipient) { recipients.add(recipient); }
    @Override public void send(String recipient, String message) {
        if (sender.isAvailable()) sender.sendMessage(recipient, "Batch notification", message);
        else System.out.println(sender.getChannelName() + " is unavailable.");
    }
    public void sendBatch(String message) {
        int successful = 0, failed = 0;
        for (String recipient : recipients) {
            if (sender.isAvailable() && sender.sendMessage(recipient, "Batch notification", message))
                successful++;
            else failed++;
        }
        System.out.println("Batch summary:\nTotal: " + recipients.size() + "\nSuccessful: " + successful + "\nFailed: " + failed);
    }
}
