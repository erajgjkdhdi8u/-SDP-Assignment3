package notifications;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
class NotificationTest {
    private String capture(Runnable task) {
        PrintStream old = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(bytes, true, StandardCharsets.UTF_8));
            task.run();
        } finally {
            System.setOut(old);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }
    @Test void telegramSendWorks() {
        assertTrue(new TelegramMessageSender().sendMessage("@alex", "News", "Hello"));
    }
    @Test void telegramFormatting() {
        assertTrue(capture(() -> new TelegramMessageSender().sendMessage("@alex", "News", "Hello")).contains("*News*"));
    }
    @Test void emailContainsSubjectAndSignature() {
        String output = capture(() -> new EmailMessageSender().sendMessage("a@b.com", "Homework", "Done"));
        assertTrue(output.contains("Subject: Homework"));
        assertTrue(output.contains("Best regards"));
    }
    @Test void telegramUnavailableReturnsFalse() {
        TelegramMessageSender t = new TelegramMessageSender();
        t.setAvailable(false);
        assertFalse(t.sendMessage("@alex", "News", "Hello"));
    }
    @Test void urgentAddsPrefix() {
        assertTrue(capture(() -> new UrgentNotification(new TelegramMessageSender()).send("@alex", "Hello")).contains("[URGENT] Hello"));
    }
    @Test void fallbackUsesSms() {
        TelegramMessageSender t = new TelegramMessageSender();
        t.setAvailable(false);
        String output = capture(() -> new UrgentNotification(t, new SmsMessageSender()).send("+77010000001", "Hello"));
        assertTrue(output.contains("Sent using fallback: SMS"));
    }
    @Test void unavailableFallbackFails() {
        TelegramMessageSender t = new TelegramMessageSender(); t.setAvailable(false);
        SmsMessageSender s = new SmsMessageSender(); s.setAvailable(false);
        assertTrue(capture(() -> new UrgentNotification(t, s).send("+77010000001", "Hello")).contains("could not be sent"));
    }
    @Test void sms160Allowed() {
        assertTrue(new SmsMessageSender().sendMessage("123", "Hi", "a".repeat(160)));
    }
    @Test void sms161Rejected() {
        assertFalse(new SmsMessageSender().sendMessage("123", "Hi", "a".repeat(161)));
    }
    @Test void smsEmptyRecipientRejected() {
        assertFalse(new SmsMessageSender().sendMessage("", "Hi", "Hello"));
    }
    @Test void base64Used() {
        assertTrue(capture(() -> new EncryptedNotification(new EmailMessageSender()).send("a@b.com", "Hello")).contains("SGVsbG8="));
    }
    @Test void batchCountsResults() {
        ScheduledBatchNotification batch = new ScheduledBatchNotification(new SmsMessageSender(), List.of("123", "", "456"));
        String output = capture(() -> batch.sendBatch("Hello"));
        assertTrue(output.contains("Successful: 2"));
        assertTrue(output.contains("Failed: 1"));
    }
    @Test void batchUnavailableCountsAllFailed() {
        SmsMessageSender sms = new SmsMessageSender(); sms.setAvailable(false);
        ScheduledBatchNotification batch = new ScheduledBatchNotification(sms, List.of("123", "456"));
        assertTrue(capture(() -> batch.sendBatch("Hello")).contains("Failed: 2"));
    }
    @Test void runtimeSwitch() {
        Notification n = new UrgentNotification(new TelegramMessageSender());
        String first = capture(() -> n.send("@alex", "One"));
        n.setSender(new EmailMessageSender());
        String second = capture(() -> n.send("a@b.com", "Two"));
        assertTrue(first.contains("[Telegram]") && second.contains("[Email]"));
    }
    @Test void pushWorks() {
        PushNotificationSender p = new PushNotificationSender();
        assertEquals("Push", p.getChannelName());
        assertTrue(p.sendMessage("device-123", "Title", "Hello"));
    }
    @Test void marketingWithPush() {
        assertTrue(capture(() -> new MarketingNotification(new PushNotificationSender()).send("device-1", "Discount")).contains("[PROMO] Discount"));
    }
    @Test void marketingWithEmail() {
        assertTrue(capture(() -> new MarketingNotification(new EmailMessageSender()).send("a@b.com", "Discount")).contains("[Email]"));
    }
    @Test void kafkaEvent() {
        assertTrue(capture(() -> new KafkaEventSender().sendMessage("analytics", "View", "Viewed")).contains("Publishing event"));
    }
}
