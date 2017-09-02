package ru.ulmc.telegram.core.event;

import com.pengrad.telegrambot.model.Update;
import lombok.Data;


public class BotCommandEvent extends UpdateEvent {

    public BotCommandEvent(Update update) {
        super(update);
    }
}
