package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue
    private long id;
    private long chatId;
    private String messageText;
    private LocalDateTime MessageSentTime;

    public NotificationTask() {
    }

    public NotificationTask(long chatId, String messageText, LocalDateTime messageSentTime) {
        this.chatId = chatId;
        this.messageText = messageText;
        MessageSentTime = messageSentTime;
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public String getMessageText() {
        return messageText;
    }

    public LocalDateTime getMessageSentTime() {
        return MessageSentTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
