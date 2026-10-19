package notifications;
public class UrgentNotification extends Notification {
    private MessageSender fallbackSender;
    public UrgentNotification(MessageSender sender) { super(sender); }
    public UrgentNotification(MessageSender sender, MessageSender fallbackSender) {
        super(sender);
        this.fallbackSender = fallbackSender;
    }
    public void setFallbackSender(MessageSender fallbackSender) { this.fallbackSender = fallbackSender; }
    @Override public void send(String recipient, String message) {
        String text = "[URGENT] " + message;
        if (sender.isAvailable() && sender.sendMessage(recipient, "Urgent notification", text)) return;
        System.out.println("Primary provider " + sender.getChannelName() + " failed.");
        if (fallbackSender != null && fallbackSender.isAvailable()
                && fallbackSender.sendMessage(recipient, "Urgent notification", text)) {
            System.out.println("Sent using fallback: " + fallbackSender.getChannelName());
            return;
        }
        System.out.println("Urgent notification could not be sent.");
    }
}
