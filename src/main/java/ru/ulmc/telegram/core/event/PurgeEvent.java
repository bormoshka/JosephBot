package ru.ulmc.telegram.core.event;

import com.pengrad.telegrambot.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.ulmc.telegram.data.entity.MessageWrap;

import java.util.TreeSet;

@AllArgsConstructor
public class PurgeEvent {
    @Getter
    private Chat chat;
    @Getter
    private TreeSet<MessageWrap> set;
}
