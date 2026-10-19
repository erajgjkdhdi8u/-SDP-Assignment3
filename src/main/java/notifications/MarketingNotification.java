package notifications;
public class MarketingNotification extends Notification {
    public MarketingNotification(MessageSender sender) { super(sender); }
    @Override public void send(String recipient, String message) {
        sender.sendMessage(recipient, "Special offer", "[PROMO] " + message + "\nReply STOP to unsubscribe.");
    }
}
