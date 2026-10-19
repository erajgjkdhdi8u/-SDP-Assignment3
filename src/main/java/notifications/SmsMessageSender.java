package notifications;
public class SmsMessageSender implements MessageSender {
    private boolean available = true;
    private static final int MAX_LENGTH = 160;
    public void setAvailable(boolean available) { this.available = available; }
    @Override public boolean isAvailable() { return available; }
    @Override public String getChannelName() { return "SMS"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) { System.out.println("[SMS] Service unavailable"); return false; }
        if (recipient == null || recipient.isBlank() || body == null) {
            System.out.println("[SMS] Invalid message"); return false;
        }
        if (body.length() > MAX_LENGTH) {
            System.out.println("[SMS] Message is too long (max 160 characters)"); return false;
        }
        System.out.println("[SMS] To: " + recipient + "\n" + body);
        return true;
    }
}
