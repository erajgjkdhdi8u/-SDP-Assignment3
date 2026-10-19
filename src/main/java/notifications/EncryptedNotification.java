package notifications;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
public class EncryptedNotification extends Notification {
    public EncryptedNotification(MessageSender sender) { super(sender); }
    @Override public void send(String recipient, String message) {
        String encoded = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
        sender.sendMessage(recipient, "Encrypted notification", encoded);
    }
}
