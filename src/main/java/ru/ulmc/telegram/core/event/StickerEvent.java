package ru.ulmc.telegram.core.event;

import com.pengrad.telegrambot.model.Update;
import lombok.Data;

public class StickerEvent extends UpdateEvent {

    public StickerEvent(Update update) {
        super(update);
    }
}
