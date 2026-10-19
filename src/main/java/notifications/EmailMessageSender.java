package notifications;
public class EmailMessageSender implements MessageSender {
    private boolean available = true;
    public void setAvailable(boolean available) { this.available = available; }
    @Override public boolean isAvailable() { return available; }
    @Override public String getChannelName() { return "Email"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) { System.out.println("[Email] Service unavailable"); return false; }
        if (recipient == null || recipient.isBlank() || body == null) {
            System.out.println("[Email] Invalid message"); return false;
        }
        System.out.println("[Email] To: " + recipient + "\nSubject: " + title + "\n" + body + "\nBest regards, Notification Gateway");
        return true;
    }
}
