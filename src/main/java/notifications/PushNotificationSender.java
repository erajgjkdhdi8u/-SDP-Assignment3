package notifications;
public class PushNotificationSender implements MessageSender {
    private boolean available = true;
    public void setAvailable(boolean available) { this.available = available; }
    @Override public boolean isAvailable() { return available; }
    @Override public String getChannelName() { return "Push"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) { System.out.println("[Push] Service unavailable"); return false; }
        if (recipient == null || recipient.isBlank() || body == null) {
            System.out.println("[Push] Invalid message"); return false;
        }
        System.out.println("[Push] Device: " + recipient + "\nNotification: " + title + "\nAlert: " + body);
        return true;
    }
}
