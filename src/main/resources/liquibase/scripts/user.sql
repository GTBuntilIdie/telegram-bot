-- liquibase formatted sql

-- changeset tishmaev:1
CREATE TABLE notification_task (
    id SERIAL,
    chatId SERIAL,
    messageText TEXT,
    MessageSentTime TIMESTAMp
)

