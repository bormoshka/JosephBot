package ru.ulmc.telegram.data.entity;

import com.pengrad.telegrambot.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Delegate;

@AllArgsConstructor
public class MessageWrap implements Comparable<MessageWrap> {
    @Delegate
    private Message message;
    @Getter
    private boolean isSticker;

    @Override
    public int compareTo(MessageWrap o) {
        return message.messageId().compareTo(o.message.messageId());
    }
}
