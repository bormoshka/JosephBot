package ru.ulmc.telegram.core.event;

import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateEvent {
    protected Update update;
}
