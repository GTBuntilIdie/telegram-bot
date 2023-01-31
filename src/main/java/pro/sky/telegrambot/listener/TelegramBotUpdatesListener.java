package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private NotificationTaskRepository notificationTaskRepository;
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository,
                                      TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String messageText = update.message().text();
            String welcomeAnswer = "Hello!";
            Long chatId = update.message().chat().id();
            if (messageText.equals("/start")) {
                SendMessage message = new SendMessage(chatId, welcomeAnswer);
                SendResponse response = telegramBot.execute(message);
                logger.info("the bot said hello");
            }
            else {
                Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
                Matcher matcher = pattern.matcher(messageText);
                if (matcher.matches()) {
                    String date = matcher.group(1);
                    String item = matcher.group(3);
                    logger.info("Notification task message (date: {}, message: {})", date, item);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    LocalDateTime localDate = LocalDateTime.parse(date, formatter);
                    NotificationTask notificationTask = new NotificationTask(chatId, item, localDate);
                    notificationTaskRepository.save(notificationTask);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void collectNotificationTaskRepository() {
        Collection<NotificationTask> suitableNotificationTasks = notificationTaskRepository
                .findAllByMessageSentTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        suitableNotificationTasks.forEach(notificationTask -> sendMessage(notificationTask.getChatId(),
                notificationTask.getMessageText()));

    }
    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        SendResponse response = telegramBot.execute(message);
        if (response.isOk()) {
            logger.info("Message was not sent: {}, message: {}", message, response.errorCode());
        }
    }

}
