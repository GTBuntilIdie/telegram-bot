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
    private Long id;
    private long chatId;
    private String messageText;
    private LocalDateTime messageSentTime;

    public NotificationTask() {
    }

    public NotificationTask(long chatId, String messageText, LocalDateTime messageSentTime) {
        this.chatId = chatId;
        this.messageText = messageText;
        messageSentTime = messageSentTime;
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
        return messageSentTime;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageSentTime(LocalDateTime messageSentTime) {
        this.messageSentTime = messageSentTime;
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
