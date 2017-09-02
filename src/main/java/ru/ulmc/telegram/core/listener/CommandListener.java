package ru.ulmc.telegram.core.listener;

import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.ulmc.telegram.core.Client;
import ru.ulmc.telegram.core.event.BotCommandEvent;
import ru.ulmc.telegram.core.exception.UnknownCommandException;
import ru.ulmc.telegram.core.service.CommandsRouter;

@Component
public class CommandListener {
    private final Client client;
    private final CommandsRouter commandsRouter;

    @Autowired
    public CommandListener(Client client, CommandsRouter commandsRouter) {
        this.client = client;
        this.commandsRouter = commandsRouter;
    }

    @EventListener
    public void onEvent(BotCommandEvent event) {
        Update update = event.getUpdate();
        try {
            commandsRouter.route(update.message().text(), update);
        } catch (UnknownCommandException e) {

        }
    }
}
