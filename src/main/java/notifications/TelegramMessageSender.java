package notifications;
public class TelegramMessageSender implements MessageSender {
    private boolean available = true;
    public void setAvailable(boolean available) { this.available = available; }
    @Override public boolean isAvailable() { return available; }
    @Override public String getChannelName() { return "Telegram"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) { System.out.println("[Telegram] Service unavailable"); return false; }
        if (recipient == null || recipient.isBlank() || body == null) {
            System.out.println("[Telegram] Invalid message"); return false;
        }
        System.out.println("[Telegram] To: " + recipient + "\n*" + title + "*\n" + body);
        return true;
    }
}
