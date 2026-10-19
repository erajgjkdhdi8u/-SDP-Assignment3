package notifications;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();
        SmsMessageSender sms = new SmsMessageSender();
        PushNotificationSender push = new PushNotificationSender();

        System.out.println("=== 1. Urgent + Telegram ===");
        new UrgentNotification(telegram).send("@student1", "Your lesson starts soon!");

        System.out.println("\n=== 2. Encrypted + Email ===");
        new EncryptedNotification(email).send("student@example.com", "This message is private.");

        System.out.println("\n=== 3. Batch + SMS ===");
        ScheduledBatchNotification batch = new ScheduledBatchNotification(
            sms, List.of("+77010000001", "+77010000002", "", "+77010000004"));
        batch.sendBatch("Reminder: class begins at 9:00.");

        System.out.println("\n=== 4. Runtime switch Telegram -> Email ===");
        Notification changeable = new UrgentNotification(telegram);
        changeable.send("@student1", "First notification");
        changeable.setSender(email);
        changeable.send("student@example.com", "Second notification");

        System.out.println("\n=== 5. Telegram unavailable -> SMS fallback ===");
        telegram.setAvailable(false);
        new UrgentNotification(telegram, sms).send("+77010000001", "Check your schedule!");
        telegram.setAvailable(true);

        System.out.println("\n=== 6. Marketing + Push ===");
        new MarketingNotification(push).send("device-1001", "Get 20% off today!");

        System.out.println("\n=== Extra: Kafka ===");
        new MarketingNotification(new KafkaEventSender()).send("analytics-service", "Promotion viewed");
    }
}
