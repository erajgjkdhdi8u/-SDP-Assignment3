package notifications;
public class KafkaEventSender implements MessageSender {
    private boolean available = true;
    public void setAvailable(boolean available) { this.available = available; }
    @Override public boolean isAvailable() { return available; }
    @Override public String getChannelName() { return "Kafka"; }
    @Override public boolean sendMessage(String recipient, String title, String body) {
        if (!isAvailable()) { System.out.println("[Kafka] Service unavailable"); return false; }
        if (recipient == null || recipient.isBlank() || body == null) {
            System.out.println("[Kafka] Invalid message"); return false;
        }
        System.out.println("[Kafka] Publishing event to topic: notifications\nRecipient: " + recipient + "\nEvent type: " + title + "\nEvent data: " + body);
        return true;
    }
}
