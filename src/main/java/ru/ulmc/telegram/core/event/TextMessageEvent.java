package ru.ulmc.telegram.core.event;

import com.pengrad.telegrambot.model.Update;
import lombok.Data;


public class TextMessageEvent extends UpdateEvent {

    public TextMessageEvent(Update update) {
        super(update);
    }
}
